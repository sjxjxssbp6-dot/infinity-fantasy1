package cupcake.nikky.imperium.tower.calendar;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.func.mod.Anime;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.daily.DailyRewardMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarProvider {

    @Getter
    private static final CalendarProvider instance = new CalendarProvider();

    private final Int2ObjectMap<CalendarReward> rewards = new Int2ObjectOpenHashMap<>();

    private CalendarProvider() {
        var granIcon = ItemBuilder.fromMaterial(Material.CLAY_BALL)
                .nbtBuilder()
                .setTag("other", "bag1")
                .apply()
                .build();
        var firmIcon = ItemBuilder.fromMaterial(Material.EMERALD)
                .nbtBuilder()
                .setTag("rid", String.valueOf(1001))
                .apply()
                .build();

        this.rewards.put(1, new CalendarReward(1, granIcon, List.of("5.000 \uE0FE"),
                player -> player.<User>getBungeePlayer().changeCurrency(CurrencyType.GRAN, 5_000)));
        this.rewards.put(2, new CalendarReward(2, granIcon, List.of("15.000 \uE0FE"),
                player -> player.<User>getBungeePlayer().changeCurrency(CurrencyType.GRAN, 15_000)));
        this.rewards.put(3, new CalendarReward(3, granIcon, List.of("25.000 \uE0FE"),
                player -> player.<User>getBungeePlayer().changeCurrency(CurrencyType.GRAN, 25_000)));
        this.rewards.put(4, new CalendarReward(4, granIcon, List.of("50.000 \uE0FE"),
                player -> player.<User>getBungeePlayer().changeCurrency(CurrencyType.GRAN, 50_000)));
        this.rewards.put(5, new CalendarReward(5, granIcon, List.of("100.000 \uE0FE"),
                player -> player.<User>getBungeePlayer().changeCurrency(CurrencyType.GRAN, 100_000)));
        this.rewards.put(6, new CalendarReward(6, granIcon, List.of("150.000 \uE0FE"),
                player -> player.<User>getBungeePlayer().changeCurrency(CurrencyType.GRAN, 150_000)));
        this.rewards.put(7, new CalendarReward(7, firmIcon, List.of("10 \uE104"),
                player -> player.<User>getBungeePlayer().changeCurrency(CurrencyType.FIRM_PRIVATE, 10)));

        Anime.createReader("vd:reward:click", (player, byteBuf) -> {
            var day = byteBuf.readInt();

            var user = player.<User>getBungeePlayer();
            var settings = user.findCalendarSettings();

            if (!user.findDonateSettings().donateData().isSubscribed()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Данное действие доступно", "только с премиум подпиской");
                return;
            }

            if (settings.canTake()) {
                settings.updateState();
                var reward = this.rewards.get(settings.getLastDay());
                if (reward != null) {
                    reward.getHandler().accept(player);
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                    Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Была выдана награда за %d день!".formatted(settings.getLastDay()));
                } else {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Произошла ошибка во время выдачи награды. Попробуйте позже.");
                }
            } else {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Вы еще не можете взять данную награду");
            }
        });
    }

    public void openMenu(@NotNull final Player player) {
        var user = player.<User>getBungeePlayer();
        var settings = user.findCalendarSettings();

        DailyRewardMenu.builder()
                .taken(settings.canTake())
                .rewards(this.rewards.values().stream()
                        .sorted(Comparator.comparingInt(CalendarReward::getIndex))
                        .map(calendarReward -> new ReactiveButton().description(calendarReward.getDescription()).item(calendarReward.getTexture()))
                        .collect(Collectors.toList())
                )
                .currentDay(settings.getNextDay())
                .build()
                .open(player);
    }

}
