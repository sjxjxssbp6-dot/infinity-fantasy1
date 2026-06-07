package cupcake.nikky.imperium.tower.sound;

import lombok.Getter;
import org.bukkit.SoundCategory;
import org.jetbrains.annotations.NotNull;

@Getter
public class AdvancedSounds {

    public static final AdvancedSounds CLICK_1 = new AdvancedSounds("click_1");
    public static final AdvancedSounds CLICK_2 = new AdvancedSounds("click_2");
    public static final AdvancedSounds COINS_GIVE = new AdvancedSounds("coins_give");
    public static final AdvancedSounds COINS_RECEIVE = new AdvancedSounds("coins_receive");
    public static final AdvancedSounds CONFUSION = new AdvancedSounds("confusion");
    public static final AdvancedSounds ENCHANT = new AdvancedSounds("enchant");
    public static final AdvancedSounds EVASION = new AdvancedSounds("evasion");
    public static final AdvancedSounds FAIL = new AdvancedSounds("fail");
    public static final AdvancedSounds HEARTBEAT = new AdvancedSounds("heartbeat");
    public static final AdvancedSounds LEVEL_UP = new AdvancedSounds("level_up");
    public static final AdvancedSounds MESSAGE = new AdvancedSounds("message");
    public static final AdvancedSounds TAP = new AdvancedSounds("TAP");
    public static final AdvancedSounds RING = new AdvancedSounds("ring");
    public static final AdvancedSounds WEAPON = new AdvancedSounds("weapon");
    public static final AdvancedSounds CLOTH = new AdvancedSounds("cloth");
    public static final AdvancedSounds ROLL = new AdvancedSounds("roll");
    public static final AdvancedSounds POTION_USAGE_1 = new AdvancedSounds("potion_usage");
    public static final AdvancedSounds POTION_USAGE_2 = new AdvancedSounds("potion_usage_2");

    private final String soundName;
    private SoundCategory category = SoundCategory.MASTER;

    public AdvancedSounds(@NotNull final String soundName) {
        this.soundName = "custom." + soundName;
    }

    public AdvancedSounds(@NotNull final String soundName, @NotNull final SoundCategory soundCategory) {
        this.soundName = "custom." + soundName;
        this.category = soundCategory;
    }

}
