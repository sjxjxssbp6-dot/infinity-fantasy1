package cupcake.nikky.imperium.tower.tree.builder.position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.cristalix.wada.util.V3;

@Getter
@AllArgsConstructor
public class Segment {

    private final int id;
    private int[] parent;
    private int[] wadaParent;
    private final V3 position;

    public void addParent(final int id) {
        var copy = new int[parent.length + 1];
        System.arraycopy(parent, 0, copy, 0, parent.length);
        copy[copy.length - 1] = id;
        this.parent = copy;
    }

    public void addWadaParent(final int id) {
        var copy = new int[wadaParent.length + 1];
        System.arraycopy(wadaParent, 0, copy, 0, wadaParent.length);
        copy[copy.length - 1] = id;
        this.wadaParent = copy;
    }

}
