package cupcake.nikky.imperium.tower.beacon;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.teleport.TeleportType;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public enum BeaconObjects {

    START(1, BeaconSettings.create().title("Осколок Беглецов")
            .canTeleportQuestedPlayer(questedPlayer -> true),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 4.5, 99, 9.5, 0F, 0F))),
    DEAD_FRAGMENT(2, BeaconSettings.create().requirement("Выполнить задание #4").title("Мертвый осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(4)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, -5.3, 100, 211, -35, 23.5F))),
    FRAGMENT_OF_ARACHNOPHOBIA(3, BeaconSettings.create().requirement("Выполнить задание #6").title("Осколок Арахнофоба")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(6)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, -10, 92, 378, -60, 6))),
    ADJACENT_FRAGMENT(4, BeaconSettings.create().requirement("Выполнить задание #8").title("Смежный осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(8)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 4, 92, 518, 39, 12.5F))),
    ENDERMIT_FRAGMENT(5, BeaconSettings.create().requirement("Выполнить задание #9").title("Осколок эндермитов")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(9)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 10, 92, 661, 82, 10))),
    DARK_FRAGMENT(6, BeaconSettings.create().requirement("Выполнить задание #10").title("Темный осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(10)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 10.3, 98, 800.5, 67, 18))),
    DRAGON_FRAGMENT(7, BeaconSettings.create().requirement("Выполнить задание #11").title("Осколок дракона")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(11)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 4.7, 93, 940.2, 70, 0))),
    HERMIT_FRAGMENT(8, BeaconSettings.create().requirement("Выполнить задание #13").title("Обитель отшельника")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(13)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 5.5, 94, 1073, 53.6F, 7))),
    ENSLAVED_CASTLE_FRAGMENT(9, BeaconSettings.create().requirement("Выполнить задание #15").title("Порабощенный замок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(15)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 1.4, 94, 1204, 54.5F, 8))),
    MOUNTAIN_FRAGMENT(10, BeaconSettings.create().requirement("Выполнить задание #16").title("Горный осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(16)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, -1.7, 92, 1344.5, -32.5F, -2.5F))),
    BINA_FRAGMENT(11, BeaconSettings.create().requirement("Выполнить задание #17").title("Осколок Бина")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(17)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 7, 92, 1473, 92.5F, 5))),
    GOLEM_FRAGMENT(12, BeaconSettings.create().requirement("Выполнить задание #18").title("Осколок големов")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(18)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 6, 93, 1602, 97.5F, 0.5F))),
    CORRUPTED_FRAGMENT(13, BeaconSettings.create().requirement("Выполнить задание #18").title("Оскверненный осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(18)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 3.6, 93, 1734, 64, 1.4F))),
    DECAYED_FRAGMENT(14, BeaconSettings.create().requirement("Выполнить задание #19").title("Сгнивший осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(19)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 3.7, 93, 1858, 64, 5))),
    DISTORTED_FRAGMENT(15, BeaconSettings.create().requirement("Выполнить задание #19").title("Искаженный осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(19)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 3.5F, 91.5, 1986.4F, 73, -1.4F))),
    ETERNAL_FRAGMENT(16, BeaconSettings.create().requirement("Выполнить задание #20").title("Вечный осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(20)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 1.8F, 91.5, 2104.1F, 73.4F, -2.7F))),
    WOLF_FRAGMENT(17, BeaconSettings.create().requirement("Выполнить задание #20").title("Волчий осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(20)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2, 91.5, 2224.1F, 73.4F, -2.7F))),
    FREE_FRAGMENT(18, BeaconSettings.create().requirement("Выполнить задание #21").title("Вольный осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(21)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2, 91.5, 2340.1F, 73.4F, -2.7F))),
    SOUL_FRAGMENT(19, BeaconSettings.create().requirement("Выполнить задание #21").title("Осколок духов")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(21)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 6.7F, 91.5, 2463.7F, 73.4F, -2.7F))),
    WATCHER_FRAGMENT(20, BeaconSettings.create().requirement("Выполнить задание #22").title("Осколок созерцателей")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(22)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.1F, 91.5, 2569.1F, 73.4F, -2.7F))),
    KETER_AND_CHESED_FRAGMENT(21, BeaconSettings.create().requirement("Выполнить задание #22").title("Осколок милости")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(22)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.4F, 91.5, 2677.1F, 73.4F, -2.7F))),
    CENTURION_FRAGMENT(22, BeaconSettings.create().requirement("Выполнить задание #23").title("Осколок сотника")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(23)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.4F, 91.5, 2787.5F, 73.4F, -2.7F))),
    FOREST_FRAGMENT(23, BeaconSettings.create().requirement("Выполнить задание #23").title("Лесной осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(23)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.4F, 91.5, 2894.1F, 73.4F, -2.7F))),
    FIELDS_FRAGMENT(24, BeaconSettings.create().requirement("Выполнить задание #24").title("Осколок полей")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(24)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.5F, 91.5, 2999.1F, 73.4F, -2.7F))),
    MERNIGA_FRAGMENT(25, BeaconSettings.create().requirement("Выполнить задание #24").title("Осколок Мерниги")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(24)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.5F, 91.5, 3097.1F, 73.4F, -2.7F))),
    SHADOW_FRAGMENT(26, BeaconSettings.create().requirement("Выполнить задание #25").title("Осколок теней")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(25)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.5F, 91.5, 3201.1F, 73.4F, -2.7F))),
    WAR_FRAGMENT(27, BeaconSettings.create().requirement("Выполнить задание #25").title("Осколок войны")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(25)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.5F, 91.5, 3300.1F, 73.4F, -2.7F))),
    CHAOS_FRAGMENT(28, BeaconSettings.create().requirement("Выполнить задание #26").title("Осколок хаоса")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(26)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.5F, 91.5, 3395.1F, 73.4F, -2.7F))),
    CHESED_FRAGMENT(29, BeaconSettings.create().requirement("Выполнить задание #26").title("Осколок Хеседа")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(26)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 2.5F, 91.5, 3491, 73.4F, -2.7F))),
    COLD_FRAGMENT(30, BeaconSettings.create().requirement("Выполнить задание #27").title("Медвежий осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(27)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 7, 93, 3572.4F, 150F, -1.7F))),
    MINE(31, BeaconSettings.create().requirement("Выполнить задание #28").title("Шахтерский осколок")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(28)),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, -0.7, 91, 3585.3F, -8.5F, -19.4F))),
    POLYGON_BINA(32, BeaconSettings.create().requirement("Выполнение задания #28").title("Полигон Бина")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(28))
            .priceFunction(beaconObjects -> 1_000),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, 1469, 105, 1915, -25F, 11.6F))),
    POLYGON_HOCHMA(33, BeaconSettings.create().requirement("Выполнение задания #28").title("Полигон Хохма")
            .canTeleportQuestedPlayer(questedPlayer -> questedPlayer.isPassed(28))
            .priceFunction(beaconObjects -> 1_000),
            FantasyBeacon.simple(new Location(MapServiceProcess.GAME_WORLD, -1478.5, 104, 1893.7, 41.4F, 15.4F)));

    private final int id;
    private final BeaconSettings beaconSettings;
    private final Beacon beacon;

    public void teleportToRespawn(@NotNull final Player player, final int cost) {
        val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
        val isPremium = donateData != null && donateData.isSubscribed();

        val playerMetadata = player.getMetadata("teleport_process");

        if (!playerMetadata.isEmpty()) {
            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            return;
        }

        if (isPremium) {
            if (Fantasy.userService().findById(player.getUniqueId()).map(user -> user.getCurrencyHandler().withdrawCurrency(CurrencyType.GRAN, cost)).orElse(false)) {
                this.forceTeleportToRespawn(player);
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_GIVE);
            } else {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно средств", "для перемещения");
            }
            return;
        }

        TeleportType.BEACON.teleportGenerator().apply(player, this).process(4, cost);
        player.setMetadata("teleport_process", new FixedMetadataValue(Fantasy.getJavaPlugin(), 1));
    }

    public void forceTeleportToRespawn(@NotNull final Player player) {
        this.forceTeleportToRespawn(player, ignore -> {});
    }

    public void forceTeleportToRespawn(@NotNull final Player player, @NotNull final Consumer<Player> onComplete) {
        val user = player.<User>getBungeePlayer();
        player.leaveVehicle();

        user.findFragmentSettings().setLastLocation(null);

        Fantasy.runLater(() -> {
            user.findFragmentSettings().setExitTime(0L);

            player.setFallDistance(0F);
            player.teleport(getBeacon().getRespawnLocation());
            onComplete.accept(player);
        }, 2);
    }

    public static BeaconObjects findById(final int id) {
        for (BeaconObjects value : values()) {
            if (value.getId() == id) return value;
        }

        return null;
    }

}
