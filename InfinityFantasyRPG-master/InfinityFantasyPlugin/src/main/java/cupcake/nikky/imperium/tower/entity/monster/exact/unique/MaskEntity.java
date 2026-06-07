package cupcake.nikky.imperium.tower.entity.monster.exact.unique;

import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.Sessional;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.session.Session;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.border.WorldBorder;

public class MaskEntity extends FantasyMonster implements Sessional {

    @Getter @Setter private Session session;
    @Getter @Setter private WorldBorder worldBorder;

    public MaskEntity(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.MASK_ENTITY);

        this.setAuraRadius(12D);
    }

}
