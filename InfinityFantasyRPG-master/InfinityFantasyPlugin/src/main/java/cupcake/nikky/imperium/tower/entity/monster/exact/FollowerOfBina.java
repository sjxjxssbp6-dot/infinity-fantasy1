package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import org.bukkit.Location;
import org.bukkit.entity.PigZombie;
import org.jetbrains.annotations.NotNull;

public class FollowerOfBina extends FantasyMonster {

    private boolean pick = false;

    public FollowerOfBina(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.FOLLOWER_OF_BINA);

        appendCast(new InboundDamageCast(this) {
            @Override
            public boolean canApply() {
                return getCurrentHealthPercent() <= 50D && !pick;
            }

            @Override
            public void apply() {
                pick = true;
                getAttributes().find(ImperiumAttribute.DEFENSE).setAdditional(25);
            }
        });
    }

    @Override
    public void onSpawn() {
        super.onSpawn();

        ((PigZombie) getSpigot()).setBaby(false);
    }
}

