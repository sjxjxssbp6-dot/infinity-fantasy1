package cupcake.nikky.imperium.tower.protocol.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeType {

    BINA("boss_1"), HOCHMA("boss_2");

    private final String fieldName;

}
