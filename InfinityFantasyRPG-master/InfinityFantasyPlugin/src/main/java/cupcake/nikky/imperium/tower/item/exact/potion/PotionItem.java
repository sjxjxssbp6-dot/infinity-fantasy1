package cupcake.nikky.imperium.tower.item.exact.potion;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.val;
import me.anfanik.steda.api.utility.InventoryUtility;
import me.anfanik.steda.api.utility.MathUtility;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class PotionItem extends DefaultImperiumItem {

    @Getter
    private final String generalName;
    @Getter
    private final BuffType potionBuff;

    private final AdvancedSounds[] potionUsageSoundEffects = new AdvancedSounds[] {AdvancedSounds.POTION_USAGE_1, AdvancedSounds.POTION_USAGE_2};

    public PotionItem(final int durability, @NotNull final String name, @NotNull final List<String> additionalDescription) {
        this(durability, name, additionalDescription, null, -1);
    }

    public PotionItem(final int durability, @NotNull final String name, @NotNull final List<String> additionalDescription, @Nullable final BuffType buffType) {
        this(durability, name, additionalDescription, buffType, -1);
    }

    public PotionItem(final int durability, @NotNull final String name, @NotNull final List<String> additionalDescription,
                      @Nullable final BuffType buffType, final int cooldown) {
        this(durability, name, additionalDescription, buffType, cooldown, ItemType.POTION);
    }

    public PotionItem(final int durability, @NotNull final String name, @NotNull final List<String> additionalDescription,
                      @Nullable final BuffType buffType, final int cooldown, @NotNull final ItemType type) {
        super(ItemRare.NONE, type, Material.IRON_NUGGET, (short) durability, name, additionalDescription);

        this.generalName = name;
        this.potionBuff = buffType;

        getItemWatcherRegistry().registerWatcher(ItemWatcher.LEVEL);
        getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);

        this.getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial()) || cooldown > 0 && !this.validateCooldown(player)) return;

            val itemLevel = getGeneralSettings().itemLevel();

            if (player.<User>getBungeePlayer().getLevel() < itemLevel) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Данное зелье будет доступно с " + itemLevel + " уровня");
                return;
            }

            if (InventoryUtility.removeFromMainHand(player.getInventory(), 1)) {
                val buff = this.getPotionBuff();
                val buffName = buff.name();

                Fantasy.buffService().findById(player.getUniqueId()).ifPresent(entity ->
                        entity.getAll().stream().filter(currentBuff -> currentBuff.getBuffType().name().startsWith(buffName.substring(0, buffName.length() - 2)))
                                .forEach(buff1 -> Fantasy.buffService().invalidateBuff(player.getUniqueId(), buff1.getBuffType())));

                Fantasy.buffService().appendBuff(player.getUniqueId(), buff);
                Fantasy.bukkitProvider().sound(player, getRandomPotionUsage());

                this.applyCooldown(player, cooldown);

                player.setCooldown(getMaterial(), 5);
            }
        });
    }

    private AdvancedSounds getRandomPotionUsage() {
        return potionUsageSoundEffects[MathUtility.randomInRange(0, potionUsageSoundEffects.length - 1)];
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        getGeneralSettings().npcTradePrice(getGeneralSettings().itemLevel() * (getGeneralSettings().itemLevel() == 4 ? 40 : 60));
    }

    protected boolean validateCooldown(@NotNull final Player player) {
        val metadata = player.getMetadata(this.getClass().getSimpleName() + "_cooldown");
        if (metadata.isEmpty()) return true;

        val leftSeconds = this.getLeftCooldownTime(player);

        if (leftSeconds > 0) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Зелье будет доступно через " +
                    leftSeconds + " " + TextUtility.transformViaAmount((int) leftSeconds, "секунду", "секунды", "секунд"));
        }

        return leftSeconds <= 0;
    }

    public void removeCooldown(@NotNull final Player player) {
        player.removeMetadata(this.getClass().getSimpleName() + "_cooldown", Fantasy.getJavaPlugin());
    }

    protected long getLeftCooldownTime(@NotNull final Player player) {
        val metadata = player.getMetadata(this.getClass().getSimpleName() + "_cooldown");
        if (metadata.isEmpty()) return 0;
        return TimeUnit.MILLISECONDS.toSeconds(metadata.getFirst().asLong() - Instant.now().toEpochMilli());
    }

    protected void applyCooldown(@NotNull final Player player, final int cooldown) {
        if (cooldown <= 0) return;

        player.setMetadata(this.getClass().getSimpleName() + "_cooldown",
                new FixedMetadataValue(Fantasy.getJavaPlugin(), Instant.now().toEpochMilli() + TimeUnit.SECONDS.toMillis(cooldown)));
    }

    @Override
    public ItemRare getRare() {
        return this.findPotionLevel() > 14 ? ItemRare.EPIC : ItemRare.RARE;
    }

    @Override
    public String getName() {
        return switch (this.findPotionLevel()) {
            case 1 -> this.getGeneralName() + " I";
            case 2 -> this.getGeneralName() + " II";
            case 3 -> this.getGeneralName() + " III";
            default -> this.getGeneralName() + " ???";
        };
    }

    public int findPotionLevel() {
        val itemLevel = getGeneralSettings().itemLevel();
        return itemLevel == 4 ? 1 : itemLevel == 19 ? 2 : itemLevel == 44 ? 3 : -1;
    }

    public boolean isExistPotion(final int level) {
        return level == 4 || level == 19 || level == 44;
    }

}
