package cupcake.nikky.imperium.tower.data.pandora;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PandoraChest {

    private UUID uuid;
    private double x;
    private double y;
    private double z;

    public boolean equal(final double x, final double y, final double z) {
        return this.x == x && this.y == y && this.z == z;
    }

}
