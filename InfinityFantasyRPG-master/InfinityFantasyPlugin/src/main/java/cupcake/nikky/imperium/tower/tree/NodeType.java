package cupcake.nikky.imperium.tower.tree;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cristalix.wada.util.Color;

@Getter
@RequiredArgsConstructor
public enum NodeType {

    SMALL_CHARACTER(Color.RED, 50),
    BIG_CHARACTER(Color.DARK_PINK, 65),
    KEY_CHARACTER(Color.YELLOW, 80),

    SMALL_EDGE(Color.BLUE, 50),
    BIG_EDGE(Color.DARK_CIAN, 65),
    KEY_EDGE(Color.YELLOW, 80);

    private final Color color;
    private final double size;

}
