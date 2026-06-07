package cupcake.nikky.imperium.tower.board;

import cupcake.nikky.imperium.tower.board.exact.BinaBoard;
import cupcake.nikky.imperium.tower.board.exact.GeneralBoard;
import cupcake.nikky.imperium.tower.board.exact.HochmaBoard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    HOCHMA(new HochmaBoard()),
    BINA(new BinaBoard()),
    GENERAL(new GeneralBoard());

    private final FantasyBoard fantasyBoard;

}
