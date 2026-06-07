package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseLoader;
import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import cupcake.nikky.imperium.tower.protocol.game.GameState;
import lombok.val;
import me.func.atlas.Atlas;
import me.func.mod.Anime;
import me.func.mod.Kit;
import me.func.mod.conversation.ModLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.datasync.EntityDataParameters;
import ru.cristalix.core.realm.IRealmService;
import ru.cristalix.wada.Wada;

public class InitProjectProcess implements BootstrapProcess {

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        Anime.include(Kit.STANDARD, Kit.MULTI_CHAT, Kit.NPC, Kit.EXPERIMENTAL, Kit.LOOTBOX, Kit.BATTLEPASS);
        Wada.initialize(javaPlugin);

        EntityDataParameters.register();

        val currentRealmId = IRealmService.get().getCurrentRealmInfo().getRealmId();
        val currentGameServer = new GameServer(currentRealmId, GameState.CONNECTED);

        val folder = currentGameServer.isTest() ? "test" : "production";
        ModLoader.load(Atlas.download("https://storage.c7x.ru/neitexx/" + folder + "/InfinityFantasyMod-bundle.jar", "mods"));
        Atlas.download("https://storage.c7x.ru/neitexx/" + folder + "/database.properties", javaPlugin.getDataFolder().getPath());

        val databaseService = new DatabaseLoader(javaPlugin).load(Fantasy.getExecutorService());
        Fantasy.initializeProject(javaPlugin, databaseService, currentGameServer);

        javaPlugin.getServer().setWhitelist(false);
    }

}
