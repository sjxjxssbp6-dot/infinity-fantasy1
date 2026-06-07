package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

import java.util.Collections;
import java.util.List;

public class MonsterImmobility extends FantasyBuff {

    public MonsterImmobility(final double value, final int duration) {
        super(duration, "Неподвижность");
        this.registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, -1000D);
    }

    @Override
    public List<String> getDescription() {
        return Collections.emptyList();
    }
}
