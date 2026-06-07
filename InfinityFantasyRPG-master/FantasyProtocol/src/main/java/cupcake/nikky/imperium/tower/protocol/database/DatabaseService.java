package cupcake.nikky.imperium.tower.protocol.database;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class DatabaseService {

    private final ThreadPoolExecutor executor;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private final Cache<UUID, Document> cachedData = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterAccess(3, TimeUnit.SECONDS)
            .build();

    public DatabaseService initialize(@NotNull final Properties properties) {
        val user = properties.getProperty("user");
        val password = properties.getProperty("password");
        val hosts = properties.getProperty("hosts");
        val database = properties.getProperty("database");

        val serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        val settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(String.format("mongodb://%s:%s@%s/?replicaSet=cri0", user, password, hosts)))
                .serverApi(serverApi)
                .retryWrites(true)
                .retryReads(true)
                .build();

        this.mongoClient = MongoClients.create(settings);
        this.mongoDatabase = mongoClient.getDatabase(database);

        return this;
    }

    public MongoCollection<Document> collection(@NotNull final Collections collections) {
        return mongoDatabase().getCollection(collections.getCollectionName());
    }

    public CompletableFuture<MongoCollection<Document>> asyncCollection(@NotNull final Collections collections) {
        return CompletableFuture.supplyAsync(() -> mongoDatabase().getCollection(collections.getCollectionName()), executor);
    }

    public CompletableFuture<FindIterable<Document>> asyncFind(@NotNull final Collections collections, @NotNull final Bson filters) {
        return CompletableFuture.supplyAsync(() -> this.collection(collections).find(filters), executor);
    }

    public CompletableFuture<FindIterable<Document>> asyncFind(@NotNull final Collections collections, @NotNull final Objects id) {
        return this.asyncFind(collections, Filters.eq("_id", id));
    }

    public CompletableFuture<Document> findUser(@NotNull final UUID uuid) {
        val cachedUser = this.cachedData.getIfPresent(uuid);
        if (cachedUser != null) return CompletableFuture.supplyAsync(() -> cachedUser, executor);

        return this.asyncFind(Collections.USERS, Filters.eq("_id", uuid.toString())).thenApplyAsync(documents -> {
            val userData = documents.first();
            val document = userData == null ? new Document() : userData;

            this.cachedData.put(uuid, document);
            return document;
        });
    }

    public CompletableFuture<Void> updateUser(@NotNull final UUID uuid, @NotNull final List<Bson> updates) {
        return this.asyncCollection(Collections.USERS).thenAcceptAsync(documentMongoCollection ->
                documentMongoCollection.updateOne(Filters.eq("_id", uuid.toString()), updates, new UpdateOptions().upsert(true))
        );
    }

    public CompletableFuture<Void> updateUser(@NotNull final UUID uuid, @NotNull final Bson updates) {
        return this.asyncCollection(Collections.USERS).thenAcceptAsync(documentMongoCollection ->
                documentMongoCollection.updateOne(Filters.eq("_id", uuid.toString()), updates, new UpdateOptions().upsert(true))
        );
    }

    public CompletableFuture<Void> updateAndInvalidateUser(@NotNull final UUID uuid, @NotNull final List<Bson> updates) {
        if (updates.isEmpty()) return CompletableFuture.completedFuture(null);
        return this.asyncCollection(Collections.USERS).thenAcceptAsync(documentMongoCollection -> {
                documentMongoCollection.updateOne(Filters.eq("_id", uuid.toString()), updates, new UpdateOptions().upsert(true));
                this.cachedData.invalidate(uuid);
        });
    }

    public CompletableFuture<Void> updateAndInvalidateUser(@NotNull final UUID uuid, @NotNull final Bson updates) {
        return this.asyncCollection(Collections.USERS).thenAcceptAsync(documentMongoCollection -> {
            documentMongoCollection.updateOne(Filters.eq("_id", uuid.toString()), updates, new UpdateOptions().upsert(true));
            this.cachedData.invalidate(uuid);
        });
    }

}
