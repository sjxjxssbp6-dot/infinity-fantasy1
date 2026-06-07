package cupcake.nikky.imperium.tower.item.exact.shard;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.Sessional;
import cupcake.nikky.imperium.tower.entity.monster.shard.ShardingMonster;
import cupcake.nikky.imperium.tower.item.DefaultImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.settings.exact.FragmentSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.metadata.MetadataType;
import cupcake.nikky.imperium.tower.zone.Zone;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.anfanik.steda.api.utility.InventoryUtility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.border.WorldBorder;
import ru.cristalix.wada.util.Color;

import java.util.List;

@Getter
@Setter
public abstract class ShardItem extends DefaultImperiumItem {

    private MonsterType monsterType;
    private Zone spawnZone;
    private String shardName;
    private String beaconName;

    public ShardItem(final short durability, @NotNull final String name, @NotNull final String beaconName) {
        super(ItemRare.NONE, ItemType.SHARD_BREAKTHROUGH, Material.STICK, durability, name, List.of(
                "Используется для прорыва существа из Грани:",
                "&6" + name,
                " ",
                "&r\uE3FF &7Использование осколка"
        ));
        this.shardName = name;
        this.beaconName = beaconName;

        getLootSettings()
                .dropEntityLevel(LevelStorage.getInstance().getBreakThroughLevel())
                .weightApplier(data -> totalSize -> totalSize * 10)
                .maxAmountHandler(data -> 1)
                .lootGeneratorSettings()
                .corruptionSettings()
                .minCorrupt(data -> 0)
                .maxCorrupt(data -> Math.max(data.getLevel() - LevelStorage.getInstance().getStartShardEdgeLevel(), 0) * 2 / 10 * 20);

        getDescriptionSettings().itemLevelPattern("&fУровень монстра: %s%d").showItemLevel(true).showItemType(true);
        getLootSettings().lootGeneratorSettings().maxItemLevel(LevelStorage.getInstance().getBreakThroughLevel() + 15);

        getGeneralSettings().npcTradePrice(500);

        getItemWatcherRegistry().registerWatcher(ItemWatcher.LEVEL);
        getItemWatcherRegistry().registerWatcher(ItemWatcher.CORRUPTED);

        this.getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial())) return;
            player.setCooldown(getMaterial(), 10);

            val user = player.<User>getBungeePlayer();
            if (user == null) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Ошибка получения клиента.", "Попробуйте позже.");
                return;
            }

            if (user.getMetadataStorage().findExactData(MetadataType.SESSION_MONSTER) >= 1) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Достигнут лимит сущностей");
                return;
            }

            val playerLocation = player.getLocation();

            if (spawnZone != null && !spawnZone.isIn(playerLocation)) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.NOTIFICATION,
                        String.format("&cВы пытаетесь создать прорыв в неподходящем месте. &6Место: %s (x = ~%.1f | z = ~%.1f)", beaconName, spawnZone.centerX(), spawnZone.centerZ()));
                return;
            }

            if (InventoryUtility.removeFromMainHand(player.getInventory(), 1)) {
                val data = getMonsterType().getMonsterData().copy();
                data.setLevel(getGeneralSettings().itemLevel());

                val instance = Fantasy.monsterProvider().createInstance(getMonsterType(), data, playerLocation);
                if (instance instanceof Sessional sessional) {
                    sessional.setSession(user.getSession());
                    user.getMetadataStorage().registerType(MetadataType.SESSION_MONSTER, user.getMetadataStorage().findExactData(MetadataType.SESSION_MONSTER) + 1);

                    val border = new WorldBorder(playerLocation.clone(), 24, Color.RED.clone());
                    sessional.setWorldBorder(border);

                    border.addPlayers(player);
                    border.setRadius(8, 45);
                }

                if (instance instanceof ShardingMonster shardingMonster) {
                    shardingMonster.setIncreaseItemQuantity(this.findSettings(FragmentSettings.class).getFragmentItemQuantity());
                    shardingMonster.setIncreaseExperience(this.findSettings(FragmentSettings.class).getFragmentExperienceIncrease());

                    shardingMonster.setIncreaseHealth(this.getGearSettings().corrupted());
                    shardingMonster.setIncreaseDamage(this.getGearSettings().corrupted() / 2D);
                    shardingMonster.setCorrupt(this.getGearSettings().corrupted());
                }

                instance.naturallySpawn(false);

                instance.getTargetTracker().goalTarget(player);
            }
        });
    }

}