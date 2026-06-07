package cupcake.nikky.imperium.tower.item.exact.shard;

import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.zone.Zone;

public class ProjectionYIBinaShard extends ShardItem {

    public ProjectionYIBinaShard() {
        super((short) 1040, "Проекция Бина", BeaconObjects.POLYGON_BINA.getBeaconSettings().title());
        this.setMonsterType(MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_BINA);
        this.setSpawnZone(new Zone(1478, 1541, 99, 118, 1894, 1952, false, 0, true));
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.PROJECTION_YI_BINA_SHARD;
    }

    @Override
    public ImperiumItem create() {
        return new ProjectionYIBinaShard();
    }

}
