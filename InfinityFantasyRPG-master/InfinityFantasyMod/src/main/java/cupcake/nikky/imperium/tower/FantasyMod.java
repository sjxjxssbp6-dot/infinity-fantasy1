package cupcake.nikky.imperium.tower;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyze;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeContainer;
import cupcake.nikky.imperium.tower.client.protocol.analyze.adapter.ItemAnalyzeAdapter;
import cupcake.nikky.imperium.tower.client.protocol.analyze.adapter.ItemAnalyzeContainerAdapter;
import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.PacketService;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.data.*;
import cupcake.nikky.imperium.tower.data.analyze.ItemAnalyzeStorage;
import cupcake.nikky.imperium.tower.data.buff.BuffStorage;
import cupcake.nikky.imperium.tower.data.pandora.PandoraStorage;
import cupcake.nikky.imperium.tower.data.prompt.cursor.CursorPromptStorage;
import cupcake.nikky.imperium.tower.data.quest.QuestContextStorage;
import cupcake.nikky.imperium.tower.data.quest.QuestListStorage;
import cupcake.nikky.imperium.tower.data.quest.QuestStorage;
import cupcake.nikky.imperium.tower.data.trade.TradeStorage;
import cupcake.nikky.imperium.tower.handler.*;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import cupcake.nikky.imperium.tower.handler.key.KeyPressHandler;
import cupcake.nikky.imperium.tower.handler.TooltipHandler;
import cupcake.nikky.imperium.tower.hud.*;
import cupcake.nikky.imperium.tower.hud.analyze.ItemAnalyzeContext;
import cupcake.nikky.imperium.tower.hud.box.CaseContext;
import cupcake.nikky.imperium.tower.hud.buff.BuffContext;
import cupcake.nikky.imperium.tower.hud.TargetBar;
import cupcake.nikky.imperium.tower.hud.craftbench.CraftBenchContext;
import cupcake.nikky.imperium.tower.hud.debug.AttributeDebugScreen;
import cupcake.nikky.imperium.tower.hud.guide.GuideContext;
import cupcake.nikky.imperium.tower.hud.navigation.NavigationContext;
import cupcake.nikky.imperium.tower.handler.ItemPromptHandler;
import cupcake.nikky.imperium.tower.hud.prompt.ItemPromptContext;
import cupcake.nikky.imperium.tower.hud.prompt.cursor.CursorPromptContext;
import cupcake.nikky.imperium.tower.hud.quest.column.QuestColumn;
import cupcake.nikky.imperium.tower.hud.quest.context.QuestContextList;
import cupcake.nikky.imperium.tower.hud.teleport.TeleportContext;
import cupcake.nikky.imperium.tower.hud.trade.TradeContext;
import cupcake.nikky.imperium.tower.particle.Particles;
import cupcake.nikky.imperium.tower.settings.UserSettings;
import cupcake.nikky.imperium.tower.texture.TextureLoader;
import cupcake.nikky.imperium.tower.tooltip.FantasyTooltip;
import cupcake.nikky.imperium.tower.tooltip.position.TooltipDefaultPosition;
import dev.xdark.clientapi.event.block.BlockLeftClick;
import dev.xdark.clientapi.event.gui.ScreenDisplay;
import dev.xdark.clientapi.event.input.KeyPress;
import dev.xdark.clientapi.event.input.RightClick;
import dev.xdark.clientapi.event.lifecycle.GameLoop;
import dev.xdark.clientapi.event.network.PluginMessage;
import dev.xdark.clientapi.event.render.*;
import dev.xdark.clientapi.settings.KeyBinding;
import gg.cristalix.particle.system.ParticleSystem;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.clientapi.JavaMod;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.*;
import ru.cristalix.enginex.util.ModTransfer;

import java.util.Arrays;

@Getter
public class FantasyMod extends JavaMod {

    @Getter @Accessors(fluent = true)
    private static Gson gson;
    @Getter @Accessors(fluent = true)
    private static TooltipHandler tooltipHandler;

    private NavigationContext navigationContext;
    private QuestColumn questColumn;
    private BuffContext buffContext;
    private TargetBar targetBar;
    private HotBarContext hotBarContext;
    private AttributeContext attributeContext;
    private QuestContextList questContext;
    private TradeContext tradeContext;
    private TeleportContext teleportContext;
    private GuideContext guideContext;
    private ItemAnalyzeContext itemAnalyzeContext;
    private ItemPromptContext itemPromptContext;
    private CursorPromptContext cursorPromptContext;
    private AttackSpeedContext attackSpeedContext;
    private LevelUpTitle levelUpTitle;
    @Setter private TeleportIndicator currentIndicator;
    private StoragePagesContext storagePagesContext;
    private TradePlayerContext tradePlayerContext;
    private IncubatorContext incubatorContext;
    private CraftBenchContext craftBenchContext;
    private BindInfoContext bindInfoContext;
    private SettingsContext settingsContext;
    private NotificationContext notificationContext;
    private CaseContext caseContext;
    private TreeIndicator treeIndicator;

    @Getter @Accessors(fluent = true)
    private static FantasyTooltip customTooltip;

    private static final boolean DEBUG = false;

    @Override
    @SuppressWarnings("all")
    public void onLoad() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ItemAnalyze.class, new ItemAnalyzeAdapter())
                .registerTypeAdapter(ItemAnalyzeContainer.class, new ItemAnalyzeContainerAdapter())
                .create();
        tooltipHandler = new TooltipHandler();

        this.onDisable.add(() -> {
            val settings = SettingsStorage.getInstance().getSettings();
            for (SettingsType value : SettingsType.values()) {
                UserSettings.getInstance().getData().put(value.getConfigKey(), settings.getOrDefault(value, true) ? "1" : "0");
            }

            UserSettings.getInstance().saveCurrentSettings();

            Particles.invalidateAll();

            ItemAnalyzeStorage.getInstance().clear();
            BuffStorage.getInstance().clear();
            BuffStorage.getMonsterInstance().clear();
            CursorPromptStorage.getInstance().clear();
            QuestListStorage.getInstance().clear();
            QuestContextStorage.getInstance().clear();
            TradeStorage.getInstance().clearAll();
            AttributeStorage.getInstance().clear();
            CraftViewStorage.getInstance().clear();
            EconomyStorage.getInstance().clear();
            InventoryOverlayTextStorage.getInstance().text(new String[0]);
            MonsterStorage.getInstance().clear();
            NavigationStorage.getInstance().clear();
            TeleportStorage.getInstance().invalidateAll();
            UserMetadata.getInstance().clear();
            QuestStorage.getInstance().clear();
            PlayerInventoryStorage.getInstance().clear();
            FragmentStorage.getInstance().clear();
            IncubatorStorage.getInstance().clear();
            CraftBenchStorage.getInstance().reset();
            SettingsStorage.getInstance().clear();
            PandoraStorage.getInstance().clear();

            PluginMessage.BUS.unregisterAll(Enginex.getListener());
            BlockLeftClick.BUS.unregisterAll(Enginex.getListener());
            HealthRender.BUS.unregisterAll(Enginex.getListener());
            ArmorRender.BUS.unregisterAll(Enginex.getListener());
            ExpBarRender.BUS.unregisterAll(Enginex.getListener());
            HungerRender.BUS.unregisterAll(Enginex.getListener());
            PotionsRender.BUS.unregisterAll(Enginex.getListener());
            VehicleHealthRender.BUS.unregisterAll(Enginex.getListener());
            ItemNameRender.BUS.unregisterAll(Enginex.getListener());
            GameLoop.BUS.unregisterAll(Enginex.getListener());
            KeyPress.BUS.unregisterAll(Enginex.getListener());
            ItemTooltipRender.BUS.unregisterAll(Enginex.getListener());
            TooltipRender.BUS.unregisterAll(Enginex.getListener());
            RightClick.BUS.unregisterAll(Enginex.getListener());
            ScreenDisplay.BUS.unregisterAll(Enginex.getListener());

            PacketService.getInstance().clear();

            this.uninitialize();
        });
    }

    public <T extends ClientPacket> void sendPacket(@NotNull final T packet) {
        val transfer = new ModTransfer().writeString(packet.getClass().getName());
        packet.clientWrite(transfer.getBuffer());
        transfer.send("fantasy:packet");
    }

    @Override
    public void asyncOnLoad() {
        TextureLoader.getInstance().loadAll();
        ParticleSystem.initialize(this);

        Particles.loadAll();

        customTooltip = new FantasyTooltip(new TooltipDefaultPosition()).enableEvents();

        val userSettings = UserSettings.getInstance();
        userSettings.loadCurrentSettings();

        for (SettingsType value : SettingsType.values()) {
            SettingsStorage.getInstance().getSettings().put(value, userSettings.getData().getOrDefault(value.getConfigKey(), "1").equals("1"));
        }

        this.initializeElements();

        tooltipHandler.initialize(this);

        new ClientPacketHandler(this);
        new DefaultRenderCancelling();
        new GameLoopEventHandler(clientApi, this);
        new PluginMessageHandler(this);
        new KeyPressHandler(this);
        new ItemPromptHandler(customTooltip);
        new BlockClickHandler(this);
        new CraftViewHandler();

        this.registerKetBindings();

        if (DEBUG) new AttributeDebugScreen(DEBUG);

        clientApi.clientConnection().sendPayload("imp:loaded", Unpooled.buffer());
        this.sendPacket(new SettingsPacket(new Object2BooleanOpenHashMap<>(SettingsStorage.getInstance().getSettings())));
    }

    private void registerKetBindings() {
        for (KeyHandler value : KeyHandler.values()) {
            val found = clientApi.settingsManager().getBindingByName(value.container().description());
            if (found == null) {
                clientApi.settingsManager().registerBinding(KeyBinding.Builder.builder()
                        .keyCode(value.container().defaultKey())
                        .category(value.container().category())
                        .description(value.container().description())
                        .build());
            }
        }
    }

    private void initializeElements() {
        this.navigationContext = new NavigationContext(this);
        this.questColumn = new QuestColumn(this);
        this.buffContext = new BuffContext(0, 1);
        this.targetBar = new TargetBar();
        this.hotBarContext = new HotBarContext();
        this.attributeContext = new AttributeContext();
        this.questContext = new QuestContextList();
        this.tradeContext = new TradeContext();
        this.teleportContext = new TeleportContext();
        this.guideContext = new GuideContext();
        this.itemAnalyzeContext = new ItemAnalyzeContext();
        this.itemPromptContext = new ItemPromptContext();
        this.cursorPromptContext = new CursorPromptContext();
        this.attackSpeedContext = new AttackSpeedContext();
        this.levelUpTitle = new LevelUpTitle();
        this.storagePagesContext = new StoragePagesContext(this);
        this.tradePlayerContext = new TradePlayerContext(this);
        this.incubatorContext = new IncubatorContext();
        this.craftBenchContext = new CraftBenchContext(this);
        this.bindInfoContext = new BindInfoContext();
        this.settingsContext = new SettingsContext(this);
        this.notificationContext = new NotificationContext(this);
        this.caseContext = new CaseContext(this);
        this.treeIndicator = new TreeIndicator(this);

        Overlay.add(hotBarContext, questColumn, buffContext, targetBar, cursorPromptContext, attackSpeedContext, levelUpTitle, bindInfoContext, treeIndicator);
        PostOverlay.add(storagePagesContext, notificationContext);
        InventoryContext.add(attributeContext, navigationContext, itemPromptContext, incubatorContext);
    }

    private void uninitialize() {
        this.navigationContext = null;
        this.questColumn = null;
        this.buffContext = null;
        this.targetBar = null;
        this.hotBarContext = null;
        this.attributeContext = null;
        this.questContext = null;
        this.tradeContext = null;
        this.teleportContext = null;
        this.guideContext = null;
        this.itemAnalyzeContext = null;
        this.itemPromptContext = null;
        this.cursorPromptContext = null;
        this.attackSpeedContext = null;
        this.levelUpTitle = null;
        this.customTooltip = null;
        this.currentIndicator = null;
        this.storagePagesContext = null;
        this.tradePlayerContext = null;
        this.incubatorContext = null;
        this.craftBenchContext = null;
        this.bindInfoContext = null;
        this.settingsContext = null;
        this.notificationContext = null;
        this.caseContext = null;
        this.treeIndicator = null;

        tooltipHandler = null;
        gson = null;
    }

}
