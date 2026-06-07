package cupcake.nikky.imperium.tower.handler;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.*;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeContainer;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.PacketService;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.buff.BuffCategory;
import cupcake.nikky.imperium.tower.data.*;
import cupcake.nikky.imperium.tower.data.analyze.ItemAnalyzeStorage;
import cupcake.nikky.imperium.tower.data.buff.BuffData;
import cupcake.nikky.imperium.tower.data.buff.BuffStorage;
import cupcake.nikky.imperium.tower.data.quest.*;
import cupcake.nikky.imperium.tower.data.trade.TradeContainer;
import cupcake.nikky.imperium.tower.data.trade.TradeStorage;
import cupcake.nikky.imperium.tower.hud.teleport.TeleportContainer;
import cupcake.nikky.imperium.tower.hud.teleport.TeleportContext;
import dev.xdark.clientapi.event.network.PluginMessage;
import dev.xdark.clientapi.item.ItemTools;
import dev.xdark.clientapi.resource.ResourceLocation;
import dev.xdark.feder.NetUtil;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.util.ModTransfer;

import java.util.ArrayList;
import java.util.List;

public class PluginMessageHandler {

    public PluginMessageHandler(@NotNull final FantasyMod mod) {
        Enginex.registerEvent(PluginMessage.class, pluginMessage -> {
            val questColumn = mod.getQuestColumn();
            val buffSelection = mod.getBuffContext();
            val targetBar = mod.getTargetBar();

            val data = pluginMessage.getData();
            val channel = pluginMessage.getChannel();

            switch (channel) {
                case "fantasy:packet": {
                    val packetName = NetUtil.readUtf8(data);

                    ClientPacket packet;
                    try {
                        packet = (ClientPacket) Class.forName(packetName, true, FantasyMod.class.getClassLoader()).newInstance();
                    } catch (Exception e) {
                        Enginex.sendChatMessage(e.getClass().getSimpleName() + ": " + packetName);
                        break;
                    }

                    packet.clientRead(data);
                    PacketService.getInstance().handle(packet);
                    break;
                }
                case "imp:add-item-analyze": {
                    val containerGson = NetUtil.readUtf8(data);
                    final ItemAnalyzeContainer container = FantasyMod.gson().fromJson(containerGson, ItemAnalyzeContainer.class);
                    ItemAnalyzeStorage.getInstance().registerContainer(container);

                    mod.getItemAnalyzeContext().update();
                    break;
                }
                case "imp:item-al-open": {
                    if(Enginex.getCurrentScreen() == null)
                        mod.getItemAnalyzeContext().open();
                    break;
                }
                case "imp:item-al-click": {
                    if (ItemAnalyzeStorage.getInstance().isShouldClear()) {
                        ItemAnalyzeStorage.getInstance().clear();
                        ItemAnalyzeStorage.getInstance().setShouldClear(false);
                    }

                    val index = NetUtil.readVarInt(data);
                    mod.getItemAnalyzeContext().getInventoryContext().resetColors();
                    mod.getItemAnalyzeContext().getInventoryContext().updateItemColor(index, 1);
                    mod.getItemAnalyzeContext().setLastActiveIndex(index);
                    break;
                }
                case "imp:tp-open": {
                    val size = NetUtil.readVarInt(data);

                    TeleportStorage.getInstance().invalidateAll();

                    for (int i = 0; i < size; i++) {
                        TeleportStorage.getInstance().registerTeleport(
                                NetUtil.readVarInt(data),
                                new TeleportContainer(
                                        NetUtil.readUtf8(data),
                                        ResourceLocation.of(NetUtil.readUtf8(data)),
                                        NetUtil.readUtf8(data),
                                        NetUtil.readVarInt(data) == 1,
                                        NetUtil.readVarInt(data)));
                    }

                    mod.getTeleportContext().update();
                    mod.getTeleportContext().open();
                    break;
                }
                case "imp:tp-close": {
                    if (Enginex.getCurrentScreen() instanceof TeleportContext) {
                        mod.getTeleportContext().close();
                    }
                    break;
                }
                case "imp:craft-view": {
                    val state = NetUtil.readVarInt(data);
                    if (state == 0) {
                        CraftViewStorage.getInstance().craftStack(null);
                        CraftViewStorage.getInstance().state(0);
                    } else {
                        CraftViewStorage.getInstance().craftStack(ItemTools.read(data));
                        CraftViewStorage.getInstance().state(1);
                    }
                    break;
                }
                case "imp:quest-register": {
                    val id = NetUtil.readVarInt(data);
                    val title = NetUtil.readUtf8(data);
                    val size = NetUtil.readVarInt(data);

                    List<String> description = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        description.add(NetUtil.readUtf8(data));
                    }

                    QuestStorage.getInstance().registerQuest(QuestData.of(title, id, description.toArray(new String[0])));
                    questColumn.update();
                    break;
                }
                case "imp:quest-unregister": {
                    val id = NetUtil.readVarInt(data);

                    QuestStorage.getInstance().unregisterQuest(id);
                    questColumn.update();
                    break;
                }
                case "imp:quest-update": {
                    val id = NetUtil.readVarInt(data);
                    val size = NetUtil.readVarInt(data);

                    List<String> description = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        description.add(NetUtil.readUtf8(data));
                    }

                    val quest = QuestStorage.getInstance().findById(id);
                    if (quest != null) {
                        quest.setDescription(description.toArray(new String[0]));
                        questColumn.update();
                    }
                    break;
                }
                case "imp:buff-register": {
                    val type = NetUtil.readVarInt(data);
                    val id = NetUtil.readVarInt(data);
                    val buffCategory = BuffCategory.values()[NetUtil.readVarInt(data)];
                    val duration = NetUtil.readVarInt(data);
                    val stack = NetUtil.readVarInt(data);
                    val image = FantasyImage.findByName(NetUtil.readUtf8(data));
                    val title = NetUtil.readUtf8(data);
                    val size = NetUtil.readVarInt(data);
                    val description = new String[size];

                    for (int i = 0; i < size; i++) {
                        description[i] = NetUtil.readUtf8(data);
                    }

                    val storage = type == 0 ? BuffStorage.getInstance() : BuffStorage.getMonsterInstance();

                    storage.registerBuff(new BuffData(id, buffCategory, stack, duration, image, title, description));

                    if (type == 0) buffSelection.reinitialize();
                    else if (type == 1) {
                        targetBar.getBuffContext().reinitialize();
                        targetBar.update();
                    }

                    break;
                }
                case "imp:buff-unregister": {
                    val type = NetUtil.readVarInt(data);
                    val id = NetUtil.readVarInt(data);

                    val storage = type == 0 ? BuffStorage.getInstance() : BuffStorage.getMonsterInstance();

                    storage.unregisterBuff(id);

                    if (type == 0) buffSelection.reinitialize();
                    else if (type == 1) {
                        targetBar.getBuffContext().reinitialize();
                        targetBar.update();
                    }

                    break;
                }
                case "imp:buff-update": {
                    val type = NetUtil.readVarInt(data);
                    val id = NetUtil.readVarInt(data);
                    val duration = NetUtil.readVarInt(data);
                    val stack = NetUtil.readVarInt(data);
                    val descriptionSize = NetUtil.readVarInt(data);

                    val storage = type == 0 ? BuffStorage.getInstance() : BuffStorage.getMonsterInstance();

                    val description = new String[descriptionSize];

                    for (int i = 0; i < descriptionSize; i++) {
                        description[i] = NetUtil.readUtf8(data);
                    }

                    val buff = storage.findById(id);
                    if (buff != null) {
                        buff.setStack(stack);
                        buff.setDuration(duration);
                        buff.setDescription(description);

                        if (type == 0) buffSelection.update();
                        else if (type == 1) targetBar.update();
                    }
                    break;
                }
                case "imp:buff-clear": {
                    val type = NetUtil.readVarInt(data);

                    val storage = type == 0 ? BuffStorage.getInstance() : BuffStorage.getMonsterInstance();
                    storage.getBuffs().forEach(buff -> storage.unregisterBuff(buff.getId()));

                    if (type == 0) buffSelection.reinitialize();
                    else if (type == 1) {
                        targetBar.getBuffContext().reinitialize();
                        targetBar.update();
                    }

                    break;
                }
                case "imp:target-ent-reg": {
                    val name = NetUtil.readUtf8(data);
                    val health = NetUtil.readVarInt(data);
                    val maxHealth = NetUtil.readVarInt(data);
                    val level = NetUtil.readVarInt(data);

                    MonsterStorage.getInstance().setName(name);
                    MonsterStorage.getInstance().setHealth(health);
                    MonsterStorage.getInstance().setMaxHealth(maxHealth);
                    MonsterStorage.getInstance().setLevel(level);
                    MonsterStorage.getInstance().setTarget(true);
                    targetBar.update();
                    break;
                }
                case "imp:target-ent-upd": {
                    val isTarget = NetUtil.readVarInt(data) == 1;
                    if (!isTarget) {
                        MonsterStorage.getInstance().setTarget(false);
                        targetBar.update();
                        return;
                    }

                    val health = NetUtil.readVarInt(data);

                    MonsterStorage.getInstance().setHealth(health);
                    MonsterStorage.getInstance().setTarget(true);
                    targetBar.update();
                    break;
                }
                case "imp:quest-list": {
                    QuestListStorage.getInstance().clear();

                    val amount = NetUtil.readVarInt(data);

                    for (int i = 0; i < amount; i++) {
                        val id = NetUtil.readVarInt(data);
                        val state = QuestState.of(NetUtil.readVarInt(data));
                        val canReceive = NetUtil.readVarInt(data) == 1;
                        val quest = ItemTools.read(data);

                        QuestListStorage.getInstance().addQuest(id, new QuestListData(quest, state, canReceive));
                    }

                    QuestListStorage.getInstance().setUpdated(true);

                    if (Enginex.getCurrentScreen() == null)
                        mod.getQuestContext().open();

                    break;
                }
                case "imp:quest-cont": {
                    QuestContextStorage.getInstance().clear();
                    val transfer = new ModTransfer(data);

                    QuestContextStorage.getInstance().setTitle(transfer.readString());
                    QuestContextStorage.getInstance().setDescription(transfer.readString());
                    QuestContextStorage.getInstance().setGoals(transfer.readString());
                    QuestContextStorage.getInstance().setId(transfer.readInt());

                    val size = transfer.readInt();
                    for (int i = 0; i < size; i++) {
                        QuestContextStorage.getInstance().addReward(transfer.readItem());
                    }

                    QuestContextStorage.getInstance().setQuestState(QuestState.of(transfer.readInt()));
                    QuestContextStorage.getInstance().setCanReceive(transfer.readInt());
                    mod.getQuestContext().update();
                    break;
                }
                case "imp:trade-create": {
                    TradeStorage.getInstance().clearAll();

                    val transfer = new ModTransfer(data);
                    val title = transfer.readString();
                    val amountTrades = transfer.readInt();

                    TradeStorage.getInstance().clearAll();
                    TradeStorage.getInstance().setTitle(title);

                    for (int i = 0; i < amountTrades; i++) {
                        TradeStorage.getInstance().addTrade(TradeContainer.read(transfer));
                    }

                    mod.getTradeContext().updateAll();
                    mod.getTradeContext().open();
                    break;
                }
                case "imp:trade-upd": {
                    val transfer = new ModTransfer(data);
                    val size = transfer.readInt();

                    for (int i = 0; i < size; i++) {
                        val id = transfer.readInt();
                        val count = transfer.readInt();

                        val tradeContainer = TradeStorage.getInstance().findTrade(id);
                        tradeContainer.currentCount(count);
                    }

                    mod.getTradeContext().getSellContext().getInventoryContext().fillItems();
                    mod.getTradeContext().updateLimits();
                    break;
                }
                case "imp:trade-sell": {
                    val index = NetUtil.readVarInt(data);
                    val flag = NetUtil.readVarInt(data);
                    val fullPrice = NetUtil.readVarInt(data);

                    mod.getTradeContext().getSellContext().getInventoryContext().updateItemColor(index, flag);
                    mod.getTradeContext().getSellContext().updatePrice(fullPrice);
                    break;
                }
                case "imp:npc-glow": {
                    val entityId = NetUtil.readVarInt(data);
                    val state = NetUtil.readVarInt(data);

                    val spawnedEntity = Enginex.getWorld().getEntity(entityId);
                    if (spawnedEntity == null) return;

                    if (state == 1) {
                        spawnedEntity.clearGlowColor();
                        spawnedEntity.setGlowing(false);
                    } else if (state == 0) {
                        spawnedEntity.setGlowing(true);
                        spawnedEntity.setGlowColor(1900399);
                    }
                    break;
                }
            }
        }, 1);
    }

}
