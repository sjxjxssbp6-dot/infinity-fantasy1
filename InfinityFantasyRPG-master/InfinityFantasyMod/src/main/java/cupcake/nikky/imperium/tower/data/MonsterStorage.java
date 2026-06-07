package cupcake.nikky.imperium.tower.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MonsterStorage {

    @Getter
    private static final MonsterStorage instance = new MonsterStorage();

    private String name;
    private int level, health, maxHealth;
    private boolean isTarget = false;

    public void clear() {
        this.name = "";
        this.level = 0;
        this.health = 0;
        this.maxHealth = 0;
        this.isTarget = false;
    }

}
