package cupcake.nikky.imperium.tower.mount;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.item.Items;

@Getter
@RequiredArgsConstructor
public enum MountType {

    HORSE_MOUNT(MountSettings.builder()
            .title("Белая лошадь")
            .type(EntityType.HORSE)
            .entitySpawnHandler((entity, player) -> {
                ((Horse) entity).setStyle(Horse.Style.WHITE);
                ((Horse) entity).setColor(Horse.Color.WHITE);
                ((Horse) entity).setAdult();
                ((Horse) entity).setCanPickupItems(false);
                ((Horse) entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                ((Horse) entity).setOwner(player);
            })
            .speed(125)
            .icon(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.HORSE)).build())
            .build()),
    SKELETON_HORSE(MountSettings.builder()
            .title("Скелет-лошадь")
            .type(EntityType.SKELETON_HORSE)
            .entitySpawnHandler((entity, player) -> {
                ((SkeletonHorse) entity).setCanPickupItems(false);
                ((SkeletonHorse) entity).setAdult();
                ((SkeletonHorse) entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                ((SkeletonHorse) entity).setOwner(player);
            })
            .speed(150)
            .icon(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.SKELETON_HORSE)).build())
            .mountRequirement(MountRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("SKELETON_HORSE"),
                    "&6Покупка в магазине"))
            .build()),
    ZOMBIE_HORSE(MountSettings.builder()
            .title("Зомби-лошадь")
            .type(EntityType.ZOMBIE_HORSE)
            .entitySpawnHandler((entity, player) -> {
                ((ZombieHorse) entity).setCanPickupItems(false);
                ((ZombieHorse) entity).setAdult();
                ((ZombieHorse) entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                ((ZombieHorse) entity).setOwner(player);
            })
            .speed(150)
            .icon(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.ZOMBIE_HORSE)).build())
            .mountRequirement(MountRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("ZOMBIE_HORSE"),
                    "&6Покупка в магазине"))
            .build()),
    GREY_HORSE(MountSettings.builder()
            .title("Серая лошадь")
            .type(EntityType.HORSE)
            .entitySpawnHandler((entity, player) -> {
                ((Horse) entity).setStyle(Horse.Style.NONE);
                ((Horse) entity).setColor(Horse.Color.GRAY);
                ((Horse) entity).setAdult();
                ((Horse) entity).setCanPickupItems(false);
                ((Horse) entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                ((Horse) entity).setOwner(player);
            })
            .speed(150)
            .icon(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.HORSE)).build())
            .mountRequirement(MountRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("GREY_HORSE"),
                    "&6Покупка в магазине"))
            .build()),
    BLACK_HORSE(MountSettings.builder()
            .title("Черная лошадь")
            .type(EntityType.HORSE)
            .entitySpawnHandler((entity, player) -> {
                ((Horse) entity).setStyle(Horse.Style.NONE);
                ((Horse) entity).setColor(Horse.Color.BLACK);
                ((Horse) entity).setAdult();
                ((Horse) entity).setCanPickupItems(false);
                ((Horse) entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                ((Horse) entity).setOwner(player);
            })
            .speed(150)
            .icon(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.HORSE)).build())
            .mountRequirement(MountRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("BLACK_HORSE"),
                    "&6Покупка в магазине"))
            .build()),
    BROWN_HORSE(MountSettings.builder()
            .title("Коричневая лошадь")
            .type(EntityType.HORSE)
            .entitySpawnHandler((entity, player) -> {
                ((Horse) entity).setStyle(Horse.Style.NONE);
                ((Horse) entity).setColor(Horse.Color.BROWN);
                ((Horse) entity).setAdult();
                ((Horse) entity).setCanPickupItems(false);
                ((Horse) entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                ((Horse) entity).setOwner(player);
            })
            .speed(150)
            .icon(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.HORSE)).build())
            .mountRequirement(MountRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("BROWN_HORSE"),
                    "&6Покупка в магазине"))
            .build()),
    DARK_BROWN_HORSE(MountSettings.builder()
            .title("Темно-коричневая лошадь")
            .type(EntityType.HORSE)
            .entitySpawnHandler((entity, player) -> {
                ((Horse) entity).setStyle(Horse.Style.NONE);
                ((Horse) entity).setColor(Horse.Color.DARK_BROWN);
                ((Horse) entity).setAdult();
                ((Horse) entity).setCanPickupItems(false);
                ((Horse) entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                ((Horse) entity).setOwner(player);
            })
            .speed(150)
            .icon(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.HORSE)).build())
            .mountRequirement(MountRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("DARK_BROWN_HORSE"),
                    "&6Покупка в магазине"))
            .build());

    private final MountSettings settings;

    public void apply(@NotNull final Player player) {
        Fantasy.userService().findById(player.getUniqueId()).filter(user -> user.isLoaded() && !user.isInvalidated()).ifPresent(user -> {
            player.getWorld().spawn(player.getLocation(), getSettings().getType().getEntityClass(), entity -> {
                entity.setMetadata("mount", new FixedMetadataValue(Fantasy.getJavaPlugin(), true));
                entity.setCustomNameVisible(false);
                entity.setCustomName(getSettings().getTitle());

                val data = new MountData(entity, this);
                user.setMountData(data);

                ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(Math.min(Math.max(0.2F * (1 + user.findAttribute(ImperiumAttribute.MOVEMENT_SPEED).findValueWithLimit() / 100), .01) * getSettings().getSpeed() / 100, 1F));

                if (getSettings().getEntitySpawnHandler() != null)
                    getSettings().getEntitySpawnHandler().accept(entity, player);

                entity.addPassenger(player);
            });
        });
    }

}
