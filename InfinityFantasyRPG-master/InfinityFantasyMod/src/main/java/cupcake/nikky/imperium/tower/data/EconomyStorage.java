package cupcake.nikky.imperium.tower.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EconomyStorage {

    @Getter
    private static final EconomyStorage instance = new EconomyStorage();

    @Getter @Setter
    private int gran;
    @Getter @Setter
    private int special;
    @Getter @Setter
    private int privateSpecial;
    @Getter @Setter
    private int mine;
    @Getter @Setter
    private int flower;

    public void clear() {
        this.gran = 0;
        this.special = 0;
        this.privateSpecial = 0;
        this.mine = 0;
        this.flower = 0;
    }

}
