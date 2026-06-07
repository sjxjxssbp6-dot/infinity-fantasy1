package cupcake.nikky.imperium.tower.entity.monster.exact.unique;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.exact.SwapSlotInboundCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.fraction.FractionProvider;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.zone.IQuadZone;
import ru.cristalix.wada.util.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class WarriorSynergy extends FantasyMonster {

    private IQuadZone mainZone;
    private BukkitTask mainZoneTask;

    private Map<Location, IQuadZone> smallZones = new HashMap<>();
    private Location[] smallZoneLocations = new Location[] {
            new Location(MapServiceProcess.GAME_WORLD, -992.5, 169, -10.5),
            new Location(MapServiceProcess.GAME_WORLD, -992.5, 169, -1.5),
            new Location(MapServiceProcess.GAME_WORLD, -1001.5, 169, -1.5),
            new Location(MapServiceProcess.GAME_WORLD, -1001.5, 169, -10.5)
    };
    private int currentIndexSmallZone = 0;

    private final AtomicBoolean start = new AtomicBoolean(false);

    public WarriorSynergy(@NotNull Location spawnLocation, @NotNull MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.WARRIOR_SYNERGY);
        this.setAuraRadius(10D);
        this.appendCast(new SwapSlotInboundCast(this, 10));
    }

    @Override
    public void onSpawn() {
        super.onSpawn();

        this.mainZoneTask = Fantasy.runTimer(() -> {
            if (!this.isExist() || !start.get()) return;

            if (mainZone == null) this.generateMainZone();
            if (smallZones.isEmpty()) {
                currentIndexSmallZone = 0;
                for (int i = 0; i < smallZoneLocations.length; i++) {
                    this.generateNextSmallZone();
                }
            }

            var nextSize = Math.max(mainZone.getSize() - .1, 1.5);
            if (mainZone.getSize() != nextSize)
                mainZone.setSize(nextSize);
        }, 10);

        FractionProvider.getInstance().setBuffFraction(FractionType.NONE);
    }

    @Override
    public void onAsyncTick() {
        super.onAsyncTick();

        var attackers = this.getAttackers();
        if (attackers.isEmpty()) return;

        if (!start.get()) start.set(true);
        if (mainZone == null) return;

        var spigot = getSpigot();
        if (spigot == null) return;

        var spigotLocation = spigot.getLocation();

        attackers.forEach(player -> {
            var distance = player.getLocation().distanceSquared(spigotLocation);
            if (distance > Math.pow(mainZone.getSize() / 2D, 2) && distance < getAuraRadius() * getAuraRadius()) {
                Fantasy.getAttackExecutor().execute(() -> Fantasy.damageProvider().processDamage(spigot, player).apply());
            }
        });
    }

    @Override
    public void onForgotTarget() {
        super.onForgotTarget();
        this.resetMechanic();
    }

    private void generateMainZone() {
        mainZone = Wada.get().getZoneManager().zoneBuilder()
                .color(Color.RED)
                .location(getSpawnLocation().clone().add(0, -.5, 0))
                .size(10)
                .build();
        Wada.get().getZoneManager().addZone(mainZone);
    }

    private void generateNextSmallZone() {
        if (currentIndexSmallZone == 3) return;
        var location = smallZoneLocations[currentIndexSmallZone++];
        var zone = Wada.get().getZoneManager().zoneBuilder()
                .color(Color.GREEN)
                .location(location.clone())
                .size(1)
                .onCollisionStateChange((player, state) -> {
                    if (state) {
                        var smallZone = smallZones.get(location);
                        if (smallZone == null) return;

                        mainZone.setSize(mainZone.getSize() + 3);
                        Wada.get().getZoneManager().removeZone(smallZone);
                        smallZones.remove(location);
                    }
                })
                .build();
        smallZones.put(location, zone);
        Wada.get().getZoneManager().addZone(zone);
    }

    private void resetMechanic() {
        if (mainZone != null) {
            Wada.get().getZoneManager().removeZone(mainZone);
            mainZone = null;
        }

        currentIndexSmallZone = 0;
        smallZones.forEach((location, zone) -> Wada.get().getZoneManager().removeZone(zone));
        smallZones.clear();
        start.set(false);
    }

    private void cancelMechanic() {
        this.resetMechanic();

        if (mainZoneTask == null || mainZoneTask.isCancelled()) return;
        mainZoneTask.cancel();
        mainZoneTask = null;
    }

    @Override
    public void invalidateEntity() {
        super.invalidateEntity();
        this.cancelMechanic();
    }
}
