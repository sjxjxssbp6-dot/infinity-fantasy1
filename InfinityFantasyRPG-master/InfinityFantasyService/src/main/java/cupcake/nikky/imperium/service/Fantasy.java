package cupcake.nikky.imperium.service;

import com.github.NeitExx.bukkit.configuration.api.service.ConfigurationService;
import com.github.NeitExx.bukkit.configuration.api.service.DefaultConfigurationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cupcake.nikky.imperium.service.fraction.FractionService;
import cupcake.nikky.imperium.service.restart.RestartProvider;
import cupcake.nikky.imperium.tower.protocol.ban.BanService;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseService;
import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import cupcake.nikky.imperium.tower.protocol.top.UserGeneralScoreSearcher;
import cupcake.nikky.imperium.tower.protocol.top.UserTimeSearcher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Fantasy {

    @Getter
    private static DatabaseService databaseService;
    @Getter @Accessors(fluent = true)
    private static final ConfigurationService configurationService = new DefaultConfigurationService();
    @Getter @Accessors(fluent = true)
    private static final FractionService fractionService = new FractionService();
    @Getter @Accessors(fluent = true)
    private static final RestartProvider restartProvider = new RestartProvider();
    @Getter @Accessors(fluent = true)
    private static BanService banService;

    @Getter
    @Accessors(fluent = true)
    private static Gson gson;
    @Getter @Accessors(fluent = true)
    private static JavaPlugin javaPlugin;
    @Getter @Accessors(fluent = true)
    private static GameServer gameServer;

    public static void initialize(@NotNull final JavaPlugin javaPlugin, @NotNull final GameServer gameServer, @NotNull final DatabaseService service) {
        if (databaseService != null) return;

        Fantasy.gameServer = gameServer;
        Fantasy.javaPlugin = javaPlugin;

        initializeGson();

        databaseService = service;
        banService = new BanService(databaseService());

        Fantasy.restartProvider().initialize(javaPlugin);
        UserTimeSearcher.getInstance().initialize(databaseService);
        UserGeneralScoreSearcher.getInstance().initialize(databaseService);
    }

    private static void initializeGson() {
        gson = new GsonBuilder().create();
    }

}
