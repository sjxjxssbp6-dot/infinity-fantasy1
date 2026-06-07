package cupcake.nikky.imperium.tower.command;

import com.github.neitexx.bukkit.command.CommandBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment.AilmentType;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.statistic.Statistics;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import lombok.val;
import me.anfanik.steda.api.menu.Menu;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.MathUtility;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.item.Items;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ImperiumCommands {

    public ImperiumCommands(@NotNull final JavaPlugin javaPlugin) {
        CommandBuilder.get()
                .addCommandName("imperium", "imp")
                .addAccessCheck(commandSender -> commandSender instanceof Player && PermissionUtility.isOperator(((Player) commandSender)) || commandSender instanceof ConsoleCommandSender)
                .setExecutor((commandSender, strings) -> {
                    val message = new String[] {
                            "&7/imp autorestart - насильный авторестарт через минуту",
                            "&7/imp item - генерация модификаторов предмету в руке для указанного уровня",
                            "&7/imp filter - добавить время расширенному фильтру предметов",
                            "&7/imp monsterspawn - получить предмет для установки нового спавна монстра",
                            "&7/imp mlocationupdate - обновить спавн-локации монстров в базе данных",
                            "&7/imp clear - удалить все незарегестрированные сущности в небольшом радиусе",
                    };

                    if (commandSender instanceof Player player) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, message);
                        return Optional.empty();
                    } else {
                        TextUtility.colorize(Arrays.asList(message)).forEach(Bukkit.getLogger()::info);
                        return Optional.empty();
                    }
                })
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("stats")
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .setExecutor((commandSender, strings) -> {
                            var player = (Player) commandSender;
                            if (strings.length != 2) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИспользование: /imperium stats [index]");
                            }

                            var user = player.<User>getBungeePlayer();
                            var index = NumberUtility.parseInteger(strings[1]);

                            user.findStatisticSettings().invalidate(Statistics.get(index));

                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fДанная статистика (индекс %d) была сброшена".formatted(index));
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("ailment")
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .setExecutor((commandSender, strings) -> {
                            var player = (Player) commandSender;
                            Fantasy.particleProvider().sendAilment(player, player.getEntityId(), AilmentType.values()[MathUtility.randomInRange(0, AilmentType.values().length - 1)], Instant.now().toEpochMilli() + 4 * 1000L);
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fСтатус выдан");
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("tree")
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .setExecutor((commandSender, strings) -> {
                            val player = (Player) commandSender;

                            Fantasy.runLater(() -> Fantasy.treeProvider().openScheme(player), 4);

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("buff")
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .setExecutor((commandSender, strings) -> {
                            val player = (Player) commandSender;

                            if (strings.length < 2) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИспользование: /imperium buff [name]");
                                return Optional.empty();
                            }

                            BuffType type;

                            try {
                                type = BuffType.valueOf(strings[1]);
                            } catch (IllegalArgumentException e) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cТакого баффа не существует");
                                return Optional.empty();
                            }

                            Fantasy.buffService().appendBuff(player.getUniqueId(), type, 10, 10);

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("move")
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .setExecutor((commandSender, strings) -> {
                            val player = (Player) commandSender;

                            if (strings.length < 2) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИспользование: /imperium move [beacon]");
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&ebeacons: " + Arrays.toString(BeaconObjects.values()));
                                return Optional.empty();
                            }

                            try {
                                val beacon = BeaconObjects.valueOf(strings[1].toUpperCase());
                                beacon.forceTeleportToRespawn(player);
                            } catch (Exception e) {
                                return Optional.empty();
                            }

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("item")
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .setExecutor((commandSender, strings) -> {
                            val player = (Player) commandSender;

                            if (strings.length != 2) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИспользование: /imperium item [level]");
                                return Optional.empty();
                            }

                            val level = NumberUtility.parseInteger(strings[1]);
                            if (level <= 0) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИспользование: /imperium item [level]");
                                return Optional.empty();
                            }

                            val imperiumItem = Fantasy.itemProvider().find(player.getInventory().getItemInMainHand());
                            val filter = Arrays.asList(ItemType.ARMOR, ItemType.WEAPON);

                            if (imperiumItem == null || !filter.contains(imperiumItem.getImperiumType())) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cПредмет в руке не может иметь модификаторов");
                                return Optional.empty();
                            }

                            val levels = new int[] {0, 6, 12};
                            val rares = Arrays.asList(ItemRare.COMMON, ItemRare.RARE, ItemRare.EPIC);

                            int goal = 0;
                            for (int i = 0; i < levels.length; i++) {
                                if (levels[i] <= level) goal = i;
                            }

                            goal = goal == 0 ? 0 : MathUtility.randomInRange(0, goal);

                            imperiumItem.getModifierSettings().findAllSpecificModifiers()
                                    .forEach(specificModifier -> imperiumItem.getModifierSettings().removeSpecificModifier(specificModifier.getModifierId()));

                            if (imperiumItem.getRare() != ItemRare.UNIQUE) ModifierProvider.getInstance().randomizeItemRarity(imperiumItem, level, rares.subList(0, goal + 1));
                            ModifierProvider.getInstance().createRandomizedBaseModifiers(imperiumItem);
                            ModifierProvider.getInstance().createRandomizedDirectionModifiers(imperiumItem);
                            if (imperiumItem.getRare() != ItemRare.UNIQUE) ModifierProvider.getInstance().createRandomizedExtendedModifiers(imperiumItem);

                            player.getInventory().setItemInMainHand(NbtUtility.clearSessionTags(imperiumItem.createNew()));
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("mlocationupdate")
                        .addAccessCheck(commandSender -> commandSender instanceof ConsoleCommandSender ||
                                commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .setExecutor((commandSender, strings) -> {
                            val player = (Player) commandSender;

                            Fantasy.monsterProvider().locationProvider().asyncUpdateLocations();

                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Локации монстров обновлены");
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player) ||
                                commandSender instanceof ConsoleCommandSender)
                        .addCommandName("autorestart", "arestart", "ar")
                        .setExecutor((commandSender, strings) -> {
                            Fantasy.autoRestartProvider().startRestartProcess(1);
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .addCommandName("monsterspawn")
                        .setExecutor((commandSender, strings) -> {
                            val page = strings.length == 1 ? 1 : NumberUtility.parseInteger(strings[1]);
                            val player = (Player) commandSender;
                            val menuBuilder = Menu.builder()
                                    .title("&eМонстры")
                                    .chestRows(6);
                            val provider = new StaticContentProvider();
                            val slot = new AtomicInteger(0);

                            val monsters = MonsterType.values();
                            for (int i = (page - 1) * 54; i < page * 54; i++) {
                                if (i == monsters.length) break;
                                val value = monsters[i];

                                provider.setItem(slot.getAndIncrement(), MenuItem.createStatic(
                                        Items.builder().type(Material.MONSTER_EGG)
                                                .displayName(value.getMonsterData().getName())
                                                .meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(value.getMonsterData().getEntityType()))
                                                .build(),
                                        (session, menuClick) -> {
                                            val stick = ItemBuilder.fromMaterial(Material.STICK)
                                                    .setName(value.getMonsterData().getName())
                                                    .build();
                                            player.getInventory().addItem(NbtUtility.applyTagCustomize(stick, nbtTagCompound -> nbtTagCompound.setString("monster", value.name())));
                                            player.closeInventory();
                                        }));
                            }

                            menuBuilder.contentProvider(provider).build().open(player);
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                        .addCommandName("filter")
                        .setExecutor((commandSender, strings) -> {
                            val player = (Player) commandSender;

                            if (strings.length != 2) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7/imp filter [time in seconds]");
                                return Optional.empty();
                            }

                            val time = TimeUnit.SECONDS.toMillis(NumberUtility.parseInteger(strings[1]));
                            player.<User>getBungeePlayer().findDonateSettings().donateData().updateLootFilter(time);
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7Установлен расширенный фильтр предметов на &e+" + TimeUnit.MILLISECONDS.toSeconds(time) + "с");
                            return Optional.empty();
                        })
                        .buildCommand())
                .build().register(javaPlugin);
    }

}
