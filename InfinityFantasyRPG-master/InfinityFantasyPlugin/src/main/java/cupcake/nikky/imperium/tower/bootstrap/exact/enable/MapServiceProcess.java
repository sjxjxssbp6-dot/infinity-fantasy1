package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.block.BlockProvider;
import cupcake.nikky.imperium.tower.box.world.CaseWorldProvider;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.map.BukkitWorldLoader;
import ru.cristalix.core.map.IMapService;
import ru.cristalix.core.map.MapService;

import java.util.Locale;

public class MapServiceProcess implements BootstrapProcess {

    public static World GAME_WORLD;

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        val coreApi = CoreApi.get();
        coreApi.registerService(IMapService.class, new MapService());
        IMapService mapService = IMapService.get();

        mapService.getLatestMapByGameTypeAndMapName("newRPG", "50maps").map(mapListDataItem -> {
            val mapVersion = mapListDataItem.getLatest();

            mapService.loadMap(mapVersion, BukkitWorldLoader.INSTANCE).whenComplete((loadedMap, throwable) -> {
                if (loadedMap == null || throwable != null) {
                    throwable.printStackTrace();
                    Bukkit.shutdown();
                    return;
                }

                Bukkit.getWorlds().forEach(world -> {
                    if (world.getUID().equals(loadedMap.getWorld().getUID())) return;

                    val craftWorld = (CraftWorld) world;
                    craftWorld.server.worlds.remove(world.getName().toLowerCase(Locale.ENGLISH));
                    craftWorld.server.console.worlds.remove(craftWorld.getHandle());
                });

                GAME_WORLD = loadedMap.getWorld();
                GAME_WORLD.getEntitiesByClass(Item.class).forEach(Entity::remove);
                //GAME_WORLD.setAutoSave(false);
                ((CraftWorld) GAME_WORLD).getHandle().spigotConfig.itemDespawnRate = 20 * 60;
                ((CraftWorld) GAME_WORLD).getHandle().paperConfig.autoSavePeriod = 20 * 60 * 5;

                Fantasy.monsterProvider().locationProvider().asyncLoad().whenComplete((v, throwable1) -> {
                    if (throwable1 != null) {
                        Fantasy.runLater(throwable1::printStackTrace);
                        return;
                    }

                    Fantasy.runLater(() -> {
                        Fantasy.monsterProvider().locationProvider().loadSpawnLocations();
                        Fantasy.monsterProvider().loadTypes();
                    });
                }).join();

                CraftServer server = (CraftServer) Bukkit.getServer();

                Fantasy.runLater(() -> {
                    BlockProvider.getInstance().findAll().forEach((blockLocation, material) -> {
                        val block = GAME_WORLD.getBlockAt(blockLocation.toBlockLocation());
                        block.setType(material);
                    });
                    CaseWorldProvider.getInstance().getCases().forEach((location, paired) -> {
                        location.toBlockLocation().getBlock().setTypeAndDataFast(Material.ENDER_CHEST.getId(), (byte) switch (paired.getSecond()) {
                            case SOUTH -> 3;
                            case WEST -> 4;
                            case EAST -> 5;
                            case null, default -> 2;
                        });
                    });
                }, 20);

                AdvancementDataWorld.REGISTRY.a();

                val minecraftServer = MinecraftServer.getServer();
                server.clearRecipes();

                minecraftServer.v.playerFileData = new IPlayerFileData() {
                    public void save(EntityHuman entityHuman) {
                    }

                    @Nullable
                    public NBTTagCompound load(EntityHuman entityHuman) {
                        return null;
                    }

                    public String[] getSeenPlayers() {
                        return new String[0];
                    }
                };
            }).join();

            return mapVersion;
        }).ifPresent(version -> Bukkit.getLogger().info("Loaded map version: " + version.getFile()));

    }

}
