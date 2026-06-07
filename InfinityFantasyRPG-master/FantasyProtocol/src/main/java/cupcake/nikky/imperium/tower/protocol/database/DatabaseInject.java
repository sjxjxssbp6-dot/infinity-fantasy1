package cupcake.nikky.imperium.tower.protocol.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadPoolExecutor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatabaseInject {

    @Getter
    private static final DatabaseInject instance = new DatabaseInject();
    private boolean injected;

    public DatabaseService inject(@NotNull final JavaPlugin javaPlugin, @NotNull final ThreadPoolExecutor executor) {
        if (injected) return null;

        val databaseLoader = new DatabaseLoader(javaPlugin);

        val databaseService = databaseLoader.load(executor);
        if (databaseService == null) {
            Bukkit.getLogger().warning("database provider is null. Server is shutting down.");
            javaPlugin.getServer().shutdown();
            return null;
        }

        injected = true;

        return databaseService;
    }

    public void executeInitQueries(@NotNull final DatabaseService service) {
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS users" +
//                "(uuid VARCHAR(60) NOT NULL, " +
//                "level INTEGER(255), " +
//                "impLevel INTEGER(255), " +
//                "exp DOUBLE(100, 1), " +
//                "impExp DOUBLE(100, 1), " +
//                "location VARCHAR(72), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS currency" +
//                "(uuid VARCHAR(60) NOT NULL, " +
//                "gran INTEGER(255), " +
//                "mine INTEGER(255), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS bans" +
//                "(uuid VARCHAR(60) NOT NULL, " +
//                "start BIGINT(255), " +
//                "duration BIGINT(255), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS attributes" +
//                "(uuid VARCHAR(60) NOT NULL, " +
//                "currentMana DOUBLE(100, 1), " +
//                "health DOUBLE(100, 1), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS monster_location" +
//                "(name VARCHAR(45) NOT NULL, " +
//                "data TEXT(65535));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS user_fraction" +
//                "(uuid VARCHAR(45) NOT NULL, " +
//                "fraction VARCHAR(100), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS " + TradeDuration.WEEK.getTableName() +
//                "(uuid VARCHAR(45) NOT NULL, " +
//                "data TEXT(65535), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS tree_storage" +
//                "(uuid VARCHAR(36) NOT NULL, " +
//                "data TEXT(16383), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS corpse_storage" +
//                "(uuid VARCHAR(36) NOT NULL, " +
//                "data TEXT(16383), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS filter_storage" +
//                "(uuid VARCHAR(36) NOT NULL, " +
//                "data TEXT(16383), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS time_1" + // bina
//                "(uuid VARCHAR(36) NOT NULL, " +
//                "time BIGINT(255), " +
//                "level INTEGER(255), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS time_2" + // hochma
//                "(uuid VARCHAR(36) NOT NULL, " +
//                "time BIGINT(255), " +
//                "level INTEGER(255), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS storages" +
//                "(uuid VARCHAR(60) NOT NULL, " +
//                "playerStorage TEXT(65535), " +
//                "globalStorage TEXT(65535), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS achievements" +
//                "(uuid VARCHAR(36) NOT NULL, " +
//                "data TEXT(16383), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS monster_respawn" +
//                "(keyLocation VARCHAR(100), " +
//                "server VARCHAR(255), " +
//                "data TEXT(16383));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS donate" +
//                "(uuid VARCHAR(60) NOT NULL, " +
//                "data TEXT(16383), " + // donate currency, donate subscription (premium) etc.
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS buffs" +
//                "(uuid VARCHAR(60), " +
//                "serialized TEXT(16383), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS quests" +
//                "(uuid VARCHAR(60), " +
//                "serializedPass TEXT(16383), " +
//                "serializedInProgress TEXT(16383), " +
//                "PRIMARY KEY (uuid));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS dailyQuests" +
//                "(uuid VARCHAR(60), id INTEGER(255));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS auction" +
//                "(slotUuid VARCHAR(60), sellerUuid VARCHAR(60), result INTEGER(255), itemStack VARCHAR(255), " +
//                "itemObject VARCHAR(36), price INTEGER(36), timeSet BIGINT(255), timeUnset BIGINT(255));");
//
//        service.getSyncQueryExecutor().execute("CREATE TABLE IF NOT EXISTS resourcepacks(uuid VARCHAR(60), type VARCHAR(36));");
    }

}
