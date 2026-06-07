package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import com.destroystokyo.paper.PaperConfig;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.PlayerAttackReader;
import cupcake.nikky.imperium.tower.attack.damage.listener.BukkitDamageListener;
import cupcake.nikky.imperium.tower.aura.FantasyAuraManager;
import cupcake.nikky.imperium.tower.block.RegenerationRunnable;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.box.world.CaseWorldProvider;
import cupcake.nikky.imperium.tower.buff.BuffScheduler;
import cupcake.nikky.imperium.tower.chat.ChatsInitializer;
import cupcake.nikky.imperium.tower.command.GeneralCommands;
import cupcake.nikky.imperium.tower.command.ImperiumCommands;
import cupcake.nikky.imperium.tower.command.listener.HelpCommandListener;
import cupcake.nikky.imperium.tower.connection.ConnectionListener;
import cupcake.nikky.imperium.tower.craft.exact.RecipeInitializer;
import cupcake.nikky.imperium.tower.user.death.DeathListener;
import cupcake.nikky.imperium.tower.entity.monster.listener.MonsterListener;
import cupcake.nikky.imperium.tower.entity.monster.listener.StickSpawnerInteract;
import cupcake.nikky.imperium.tower.filter.listener.ItemFilterListener;
import cupcake.nikky.imperium.tower.harvest.fishing.FishingListener;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectListener;
import cupcake.nikky.imperium.tower.item.listener.ItemListener;
import cupcake.nikky.imperium.tower.harvest.mining.MiningListener;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.mount.MountListener;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.corpse.CorpsePoints;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionPingBulkPacket;
import cupcake.nikky.imperium.tower.resourcepack.ResourcePack;
import cupcake.nikky.imperium.tower.quest.exact.*;
import cupcake.nikky.imperium.tower.quest.listener.QuestListener;
import cupcake.nikky.imperium.tower.user.FantasyUser;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.target.TargetController;
import cupcake.nikky.imperium.tower.world.GeneralListener;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.val;
import me.func.mod.conversation.ModTransfer;
import net.cristalix.paper.DefaultChunkData;
import net.cristalix.paper.biome.EmptyChunkBiome;
import net.cristalix.paper.light.FixedChunkLight;
import net.diamondden.fix.ItemNbtDataFixer;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.SpigotConfig;
import ru.cristalix.wada.Wada;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class GeneralEnableProcess implements BootstrapProcess {

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        new ImperiumCommands(javaPlugin);
        new GeneralCommands(javaPlugin);

        Fantasy.runAsyncTimer(() -> {
            val listUuid = Fantasy.userService().findAll().stream().filter(user -> user.isLoaded() && !user.isInvalidated()).map(User::getUniqueId).toList();
            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new SessionPingBulkPacket(listUuid));
        }, 20L * 4L, 20L * 2L);

        Fantasy.runAsyncTimer(new BuffScheduler(), 20);

        Fantasy.runAsyncTimer(() -> {
            if (!Fantasy.monsterProvider().isTypesLoaded()) return;
            Fantasy.monsterProvider().forEach((uuid, monster) -> monster.onAsyncTick());
        }, 10, 10);
        Fantasy.runAsyncTimer(() -> Fantasy.monsterProvider().respawnTracker().update(), 10, Fantasy.monsterProvider().respawnTracker().interval());

        val targetController = new TargetController();
        Fantasy.runAsyncTimer(() -> Fantasy.userService().findAll().stream().filter(user -> !user.isInvalidated() && user.isLoaded()).forEach(user -> {
            targetController.run(user);
            user.asyncTick();
        }), FantasyUser.ASYNC_TICK);

        Fantasy.runTimer(new RegenerationRunnable(), RegenerationRunnable.TICK);

        Fantasy.runAsyncTimer(() -> {
            val counter = new AtomicInteger();

            Fantasy.userService().findAll().stream().filter(user -> !user.isInvalidated() && user.isLoaded()).forEach(user -> {
                user.saveAllData().whenComplete((unused, throwable) -> {
                    if (throwable != null) Fantasy.runLater(throwable::printStackTrace);
                });

                counter.addAndGet(1);
            });

            if (counter.get() > 0) Bukkit.getLogger().info("All user data saved (user amount: " + counter.get() + ")");
        }, 20L * 60L * 5L, 20L * 60L * 5L);

        new PlayerAttackReader();

        HandlerList.unregisterAll((Listener) Wada.getInstance().getAuraManager());
        FantasyAuraManager.get().register(Fantasy.getJavaPlugin());
        Wada.getInstance().getCustomNameTag().enable(true);
        ZoneList.processZones();

        CorpsePoints.getInstance().initialize(MapServiceProcess.GAME_WORLD);
        Fantasy.battlePassProvider().initialize();
        CaseWorldProvider.getInstance().initialize();

        ConnectionListener.getInstance().register(javaPlugin);
        new BukkitDamageListener().register(javaPlugin);
        new DeathListener().register(javaPlugin);
        new QuestListener().register(javaPlugin);
        new GeneralListener().register(javaPlugin);
        new MonsterListener().register(javaPlugin);
        new ItemListener().register(javaPlugin);
        new MountListener().register(javaPlugin);

        new MiningListener().register(javaPlugin);
        new FishingListener().register(javaPlugin);

        new StickSpawnerInteract().register(javaPlugin);
        new AspectListener().register(javaPlugin);
        new ItemFilterListener().register(javaPlugin);
        new HelpCommandListener().register(javaPlugin);

        new StartWorldFragmentQuestInitializer();
        new MiddleWorldFragmentsQuestInitializer();
        new EndWorldFragmentsQuestInitializer();
        new PolygonQuestInitializer();
        new DailyQuestInitializer();
        new EventQuestInitializer();

        NPCs.initializeAll();
        Fantasy.treeProvider().loadScheme();

        new RecipeInitializer();

        ChatsInitializer.getInstance().initialize();

        Stream.of(ResourcePack.values()).forEach(ResourcePack::findSha1);

        CraftPlayer.DEFAULT_HIDE_PLAYERS = true;

        DefaultChunkData.chunkLight = new FixedChunkLight((byte) -1);
        DefaultChunkData.biome = new EmptyChunkBiome();

        Chunk.DISABLE_LIGHT_PROCESSING = true;

        EntityHuman.DISABLE_ATTACK_CRIT = true;
        EntityHuman.DISABLE_ATTACK_SWEEP = true;

        EntityLiving.DISABLE_CRAMMING = true;

        ItemNbtDataFixer.DISABLE_LACH_CRASH_FIX = true;

        PaperConfig.disableKnockback = true;
        PaperConfig.enablePlayerCollisions = false;
        PaperConfig.enableWorldSave = false;
        PaperConfig.delayBetweenEntitiesMovements = 0;
        PaperConfig.betterPathfinders = false;
        PaperConfig.savePlayerData = false;

        SpigotConfig.restartOnCrash = true;
        SpigotConfig.movedWronglyThreshold = 1.5;
        SpigotConfig.movedTooQuicklyMultiplier = 400.0;

        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.miscTrackingRange = 16;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.animalTrackingRange = 20;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.monsterTrackingRange = 20;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.otherTrackingRange = 16;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.playerTrackingRange = 20;

        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.itemMerge = 1;

        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.saveStructureInfo = false;

        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().ticksPerAnimalSpawns = 0L;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().ticksPerMonsterSpawns = 0L;

        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.disableTeleportationSuffocationCheck = true;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.enableTreasureMaps = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateCanyon = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateCaves = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateDungeon = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateFortress = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateMineshaft = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateFlatBedrock = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateMonument = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateStronghold = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateTemple = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.generateVillage = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.disableChestCatDetection = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.scanForLegacyEnderDragon = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.armorStandTick = false;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().paperConfig.villageSiegesEnabled = false;

        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.viewDistance = 6;

        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.miscActivationRange = 12;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.animalActivationRange = 24;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.monsterActivationRange = 24;
        ((CraftWorld)MapServiceProcess.GAME_WORLD).getHandle().spigotConfig.waterActivationRange = 16;

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doMobLoot", "false");
            world.setGameRuleValue("mobGriefing", "false");
        }

        ((CraftServer) javaPlugin.getServer()).getHandle().getServer().setAllowFlight(true);
    }

}
