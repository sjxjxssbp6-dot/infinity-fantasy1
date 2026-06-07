package cupcake.nikky.imperium.tower.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMetadata {

    @Getter
    private static final UserMetadata instance = new UserMetadata();

    private boolean subscribed;
    private double currentExp = 1;
    private double maxExp = 1;
    private double multiplier = 1;
    private int health = 1;
    private int maxHealth = 1;
    private int mana = 0;
    private boolean isManaBlocked;
    private int maxMana = 1;
    private int level = 1;
    private String levelColor = "";

    public void clear() {
        this.subscribed = false;
        this.isManaBlocked = false;
        this.currentExp = 1;
        this.maxExp = 1;
        this.multiplier = 1;
        this.health = 1;
        this.maxHealth = 1;
        this.mana = 0;
        this.maxMana = 1;
        this.level = 1;
        this.levelColor = "";
    }

}
