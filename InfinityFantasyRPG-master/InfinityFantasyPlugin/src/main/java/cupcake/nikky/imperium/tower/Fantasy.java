package cupcake.nikky.imperium.tower;

import com.github.NeitExx.bukkit.configuration.api.service.ConfigurationService;
import com.github.NeitExx.bukkit.configuration.api.service.DefaultConfigurationService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cupcake.nikky.imperium.tower.attack.damage.provider.DamageProvider;
import cupcake.nikky.imperium.tower.battlepass.BattlePassProvider;
import cupcake.nikky.imperium.tower.buff.service.ImperiumBuffService;
import cupcake.nikky.imperium.tower.bukkit.BukkitProvider;
import cupcake.nikky.imperium.tower.client.ClientProvider;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyze;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeContainer;
import cupcake.nikky.imperium.tower.client.protocol.analyze.adapter.ItemAnalyzeAdapter;
import cupcake.nikky.imperium.tower.client.protocol.analyze.adapter.ItemAnalyzeContainerAdapter;
import cupcake.nikky.imperium.tower.combat.CombatProvider;
import cupcake.nikky.imperium.tower.craft.RecipeProvider;
import cupcake.nikky.imperium.tower.edge.EdgeCoreRegistry;
import cupcake.nikky.imperium.tower.indicator.DamageIndicatorSettings;
import cupcake.nikky.imperium.tower.particle.ParticleProvider;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseService;
import cupcake.nikky.imperium.tower.protocol.injector.ProtocolProvider;
import cupcake.nikky.imperium.tower.indicator.DamageIndicator;
import cupcake.nikky.imperium.tower.item.provider.ItemProvider;
import cupcake.nikky.imperium.tower.logging.ImperiumLoggerService;
import cupcake.nikky.imperium.tower.logging.LoggerService;
import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import cupcake.nikky.imperium.tower.trade.npc.service.TradeNpcService;
import cupcake.nikky.imperium.tower.quest.service.ImperiumQuestService;
import cupcake.nikky.imperium.tower.server.restart.AutoRestartProvider;
import cupcake.nikky.imperium.tower.block.RegenerationProvider;
import cupcake.nikky.imperium.tower.entity.monster.MonsterProvider;
import cupcake.nikky.imperium.tower.session.SessionProvider;
import cupcake.nikky.imperium.tower.tree.provider.TreeProvider;
import cupcake.nikky.imperium.tower.user.service.ImperiumUserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.core.data.CompressedBinaryData;
import ru.cristalix.core.data.SimpleBinaryData;
import ru.cristalix.core.data.adapter.CompressedBinaryDataAdapter;
import ru.cristalix.core.data.adapter.SimpleBinaryDataAdapter;

import java.util.concurrent.*;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Fantasy {

    @Getter private static JavaPlugin javaPlugin;

    @Getter @Accessors(fluent = true) private static DatabaseService databaseProvider;
    @Getter private static final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    @Getter private static final ThreadPoolExecutor chatExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
            1, new ThreadFactoryBuilder().setNameFormat("Fantasy Chat Thread - %1$d").build()
    );
    @Getter private static final ThreadPoolExecutor attackExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
            2, new ThreadFactoryBuilder().setNameFormat("Fantasy Attack Thread - %1$d").build()
    );
    @Getter private static final ThreadPoolExecutor respawnAndBeaconExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
            1, new ThreadFactoryBuilder().setNameFormat("Fantasy Respawn & Beacon Thread - %1$d").build()
    );
    @Getter private static final ThreadPoolExecutor monsterDeathExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
            1, new ThreadFactoryBuilder().setNameFormat("Fantasy Monster Death Thread - %1$d").build()
    );

    @Getter @Accessors(fluent = true)
    private static final ProtocolProvider protocolProvider = new ProtocolProvider();
    @Getter @Accessors(fluent = true)
    private static BattlePassProvider battlePassProvider;
    @Getter @Accessors(fluent = true)
    private static final ImperiumUserService userService = new ImperiumUserService();
    @Getter @Accessors(fluent = true)
    private static final ItemProvider itemProvider = new ItemProvider();
    @Getter @Accessors(fluent = true)
    private static final MonsterProvider monsterProvider = new MonsterProvider();
    @Getter @Accessors(fluent = true)
    private static final CombatProvider combatProvider = new CombatProvider();
    @Getter @Accessors(fluent = true)
    private static final ImperiumBuffService buffService = new ImperiumBuffService();
    @Getter @Accessors(fluent = true)
    private static final AutoRestartProvider autoRestartProvider = new AutoRestartProvider();
    @Getter @Accessors(fluent = true)
    private static final ImperiumQuestService questService = new ImperiumQuestService();
    @Getter @Accessors(fluent = true)
    private static final BukkitProvider bukkitProvider = new BukkitProvider();
    @Getter @Accessors(fluent = true)
    private static final ClientProvider clientProvider = new ClientProvider();
    @Getter @Accessors(fluent = true)
    private static final ParticleProvider particleProvider = new ParticleProvider();
    @Getter @Accessors(fluent = true)
    private static final RegenerationProvider regenerationProvider = new RegenerationProvider();
    @Getter @Accessors(fluent = true)
    private static final RecipeProvider recipeProvider = new RecipeProvider();
    @Getter @Accessors(fluent = true)
    private static SessionProvider sessionProvider;
    @Getter @Accessors(fluent = true)
    private static final TreeProvider treeProvider = new TreeProvider();
    @Getter @Accessors(fluent = true)
    private static LoggerService loggerService;
    @Getter @Accessors(fluent = true)
    private static final ConfigurationService configurationService = new DefaultConfigurationService();
    @Getter @Accessors(fluent = true)
    private static final DamageProvider damageProvider = new DamageProvider();
    @Getter @Accessors(fluent = true)
    private static TradeNpcService tradeNpcService;
    @Getter @Accessors(fluent = true)
    private static Gson gson;
    @Getter @Accessors(fluent = true)
    private static GameServer gameServer;
    @Getter @Accessors(fluent = true)
    private static final Season currentSeason = new Season("Фракции", "1.2", "https://telegra.ph/Infinity-Fantasy-Obnovlenie-12-09-05");

    public static final boolean DEBUG = false;

    public static void initializeProject(@NotNull final JavaPlugin plugin, @Nullable final DatabaseService service, @Nullable final GameServer server) {
        if (javaPlugin != null) throw new RuntimeException("Attempt to init project with not null javaplugin");

        executorService.setMaximumPoolSize(executorService.getCorePoolSize() * 2);
        executorService.setKeepAliveTime(30L, TimeUnit.SECONDS);
        executorService.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("Fantasy Thread - %1$d").build());
        executorService.allowCoreThreadTimeOut(true);
        executorService.prestartAllCoreThreads();

        attackExecutor.setKeepAliveTime(30L, TimeUnit.SECONDS);
        chatExecutor.setKeepAliveTime(30L, TimeUnit.SECONDS);
        respawnAndBeaconExecutor.setKeepAliveTime(30L, TimeUnit.SECONDS);
        monsterDeathExecutor.setKeepAliveTime(30L, TimeUnit.SECONDS);

        javaPlugin = plugin;
        databaseProvider = service;
        sessionProvider = new SessionProvider();
        gameServer = server;
        loggerService = new ImperiumLoggerService();

        initializeGson();

        Fantasy.autoRestartProvider().initialize();

        tradeNpcService = new TradeNpcService().initialize(databaseProvider(), gson());
        battlePassProvider = new BattlePassProvider();
        EdgeCoreRegistry.getInstance().initialize();
    }

    private static void initializeGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ItemAnalyze.class, new ItemAnalyzeAdapter())
                .registerTypeAdapter(ItemAnalyzeContainer.class, new ItemAnalyzeContainerAdapter())
                //.registerTypeAdapter(SpecificModifier.class, new SpecificModifierAdapter())
                .registerTypeAdapter(CompressedBinaryData.class, new CompressedBinaryDataAdapter())
                .registerTypeAdapter(SimpleBinaryData.class, new SimpleBinaryDataAdapter())
                .create();
    }

    public static ItemBuilder.MaterialItemBuilder newItemBuilder(@NotNull final Material material) {
        return ItemBuilder.fromMaterial(material);
    }

    public static ItemBuilder.ItemStackItemBuilder newItemBuilder(@NotNull final ItemStack itemStack) {
        return ItemBuilder.fromItem(itemStack);
    }

    public static DamageIndicator newDamageIndicator(@NotNull final LivingEntity entity, @NotNull final DamageIndicatorSettings damageIndicatorSettings) {
        return new DamageIndicator(entity.getEntityId(), damageIndicatorSettings);
    }

    public static BukkitTask runTimer(@NotNull final Runnable runnable, final int ticks) {
        return Bukkit.getScheduler().runTaskTimer(getJavaPlugin(), runnable, 0L, ticks);
    }

    public static BukkitTask runTimer(@NotNull final Runnable runnable, final int delay, final int ticks) {
        return Bukkit.getScheduler().runTaskTimer(getJavaPlugin(), runnable, delay, ticks);
    }

    public static BukkitTask runLater(@NotNull final Runnable runnable, final int ticks) {
        return Bukkit.getScheduler().runTaskLater(getJavaPlugin(), runnable, ticks);
    }

    public static BukkitTask runLater(@NotNull final Runnable runnable) {
        return Bukkit.getScheduler().runTask(getJavaPlugin(), runnable);
    }

    public static CompletableFuture<Void> runAsync(@NotNull final Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executorService);
    }

    public static BukkitTask runAsyncBukkit(@NotNull final Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, runnable);
    }

    public static BukkitTask runAsyncTimer(@NotNull final Runnable runnable, final long ticks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(getJavaPlugin(), runnable, 0L, ticks);
    }

    public static BukkitTask runAsyncTimer(@NotNull final Runnable runnable, final long delay, final long ticks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(getJavaPlugin(), runnable, delay, ticks);
    }

    public static <U> CompletableFuture<U> supplyAsync(@NotNull final Supplier<U> runnable) {
        return CompletableFuture.supplyAsync(runnable, executorService);
    }

    public static void runAsyncLater(@NotNull final Runnable runnable, final int ticks) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(getJavaPlugin(), runnable, ticks);
    }

}
