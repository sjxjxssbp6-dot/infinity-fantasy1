package cupcake.nikky.imperium.tower.item.exact.shard;

import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.zone.Zone;

public class ProjectionYIHochmaShard extends ShardItem {

    public ProjectionYIHochmaShard() {
        super((short) 1031, "Проекция Хохма", BeaconObjects.POLYGON_HOCHMA.getBeaconSettings().title());
        this.setMonsterType(MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_HOCHMA);
        this.setSpawnZone(new Zone(-1534, -1472, 97, 112, 1904, 1952, false, 0, true));
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.PROJECTION_YI_HOCHMA_SHARD;
    }

    @Override
    public ImperiumItem create() {
        return new ProjectionYIHochmaShard();
    }

}
