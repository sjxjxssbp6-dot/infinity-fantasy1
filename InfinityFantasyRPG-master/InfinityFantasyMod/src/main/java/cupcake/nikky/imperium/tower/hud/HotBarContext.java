package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.data.SettingsStorage;
import cupcake.nikky.imperium.tower.data.UserMetadata;
import cupcake.nikky.imperium.tower.hud.economy.EconomyContext;
import cupcake.nikky.imperium.tower.hud.economy.EconomyDataType;
import cupcake.nikky.imperium.tower.progress.FantasyProgressBar;
import cupcake.nikky.imperium.tower.utility.NumberUtility;
import dev.xdark.clientapi.gui.ingame.ChatScreen;
import lombok.Getter;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.element.HorizontalLayout;
import ru.cristalix.enginex.element.Image;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.element.VerticalLayout;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.time.Instant;

public class HotBarContext extends VerticalLayout {

    @Getter private final AdvancedHotBarBox advancedHotBarBox = new AdvancedHotBarBox();
    @Getter private final EconomyContext economyContext = new EconomyContext();

    @Getter private final FantasyProgressBar healthBar;
    @Getter private final FantasyProgressBar manaBar;
    @Getter private final FantasyProgressBar levelBar;
    @Getter private final FantasyProgressBar battleDuration;

    @Getter private final Text levelText = new Text();
    @Getter private final Text levelPercentText = new Text();
    @Getter private final Text healthText = new Text();
    @Getter private final Text healthPercentText = new Text();
    @Getter private final Image healthImage = new Image("\uE19A").setSize(new V3(16,16));
    @Getter private final Text manaText = new Text();
    @Getter private final Image manaImage = new Image(FantasyImage.MANA8.toTexture()).setSize(new V3(16,16));
    @Getter private final Text manaPercentText = new Text();

    public HotBarContext() {
        super(2D);

        this.setAlign(Relative.BOTTOM).setOrigin(Relative.BOTTOM).setPos(new V3(0, -44)).setRenderLayer(-1);

        this.healthBar = new FantasyProgressBar(0, 0);
        this.healthBar.getBar().setColor(Palette.RED);
        this.healthBar.setCarveSize(2D).setOrigin(Relative.CENTER).setSize(new V3(364, 24, 0));
        this.healthBar.addChild(new HorizontalLayout(4D).addChild(healthText.setShadow(true), healthImage).setOrigin(Relative.CENTER).setAlign(Relative.CENTER),
                healthPercentText.setShadow(true).setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT).setPos(new V3(-5, 0)));

        this.manaBar = new FantasyProgressBar(0, 0);
        this.manaBar.getBar().setColor(Palette.BLUE);
        this.manaBar.setCarveSize(2D).setOrigin(Relative.CENTER).setSize(new V3(364, 24, 0));
        this.manaBar.addChild(new HorizontalLayout(4D).addChild(manaText.setShadow(true), manaImage).setOrigin(Relative.CENTER).setAlign(Relative.CENTER),
                manaPercentText.setShadow(true).setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT).setPos(new V3(-5, 0)));

        this.levelBar = new FantasyProgressBar(0, 0);
        this.levelBar.getBar().setColor(Palette.ORANGE);
        this.levelBar.setCarveSize(2D).setOrigin(Relative.CENTER).setSize(new V3(364, 24, 0));
        this.levelBar.addChild(levelText.setShadow(true).setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(5, 0)),
                levelPercentText.setShadow(true).setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT).setPos(new V3(-5, 0)));

        economyContext.apply(EconomyDataType.GRAN, EconomyDataType.MINING, EconomyDataType.SPECIAL);
        economyContext.onPreTransform(element -> {
            economyContext.update();
            economyContext.setPos(new V3(364D / 2D - economyContext.getSize().getX() / 2D, 0));
        });

        advancedHotBarBox.onPreTransform(element -> {
            advancedHotBarBox.update();
            advancedHotBarBox.setPos(new V3(364D / 2D - advancedHotBarBox.getSize().getX() / 2D, 0));
        });

        this.addChild(advancedHotBarBox.setOrigin(Relative.BOTTOM_LEFT), economyContext.setOrigin(Relative.BOTTOM_LEFT), healthBar, manaBar, levelBar);

        this.battleDuration = new FantasyProgressBar(0, 0, Palette.PURPLE_MIDDLE.alpha(1));
        this.battleDuration.setCarveSize(0D).setSize(new V3(360, 3)).setOrigin(Relative.BOTTOM).setAlign(Relative.BOTTOM);
        this.battleDuration.setProgressInstant(1D);
        this.battleDuration.setEnabled(false);
        this.battleDuration.onPreTransform(element -> {
            if (battleDuration.getRenderProgress() == 0D) {
                battleDuration.setEnabled(false);
            }
        });

        this.onPreTick(element -> {
            val player = Enginex.getPlayer();

            if (Enginex.getScreen() instanceof ChatScreen) {
                if (!SettingsStorage.getInstance().getSettings().getBoolean(SettingsType.HOT_BAR_WITH_CHAT_VISIBILITY)) {
                    healthBar.setEnabled(false);
                    manaBar.setEnabled(false);
                    levelBar.setEnabled(false);
                    advancedHotBarBox.setEnabled(false);
                }
            } else {
                val value = !player.isSpectator() && !player.isCreative();
                healthBar.setEnabled(value);
                manaBar.setEnabled(value && !UserMetadata.getInstance().isManaBlocked());
                levelBar.setEnabled(value);
                advancedHotBarBox.setEnabled(value);
            }
        });

        this.healthBar.addChild(battleDuration);

        this.manaBar.setEnabled(false);

        this.updateHealth();
        this.updateMana();
        this.updateLevel();
    }

    public void updateBattle(final int duration, final boolean enabled) {
        if (!enabled) {
            this.battleDuration.setEnabled(false);
            this.battleDuration.setProgressInstant(1D);
            this.battleDuration.setProgress(1D);
            return;
        }

        battleDuration.setEnabled(true);
        battleDuration.setProgressInstant(1D);

        val currentMillis = Instant.now().toEpochMilli();
        battleDuration.setTimer(currentMillis, currentMillis + duration);
    }

    public void updateMana() {
        val progress = UserMetadata.getInstance().getMana() / Math.max(UserMetadata.getInstance().getMaxMana(), 1D);
        val currentProgress = this.manaBar.getRenderProgress();

        if (Animation.hasAnimation(this.manaBar, "update_mana")) {
            Animation.stop(this.manaBar);
        }

        if (this.manaBar.getRenderProgress() <= 0) {
            this.manaBar.setProgressInstant(progress);
            this.manaBar.setProgress(progress);
        } else {
            Animation.play(this.manaBar, "update_mana", .3, (element, aDouble) -> {
                val delta = currentProgress - progress;
                val newProgress = currentProgress - delta * aDouble;

                this.manaBar.setProgressInstant(newProgress);
                this.manaBar.setProgress(newProgress);
            });
        }

        manaText.setValue(NumberUtility.classicalFormat(Math.max(UserMetadata.getInstance().getMana(), 0)) + "/" +
                NumberUtility.classicalFormat(Math.max(UserMetadata.getInstance().getMaxMana(), 1)));
        manaPercentText.setValue(String.format("%.0f%%", Math.max(progress * 100, 0)));
    }

    public void updateHealth() {
        val progress = (double) UserMetadata.getInstance().getHealth() / (double) UserMetadata.getInstance().getMaxHealth();
        val currentProgress = this.healthBar.getRenderProgress();

        if (Animation.hasAnimation(this.healthBar, "update_health")) {
            Animation.stop(this.healthBar);
        }

        if (this.healthBar.getRenderProgress() <= 0) {
            this.healthBar.setProgressInstant(progress);
            this.healthBar.setProgress(progress);
        } else {
            Animation.play(this.healthBar, "update_health", .3, (element, aDouble) -> {
                val delta = currentProgress - progress;
                val newProgress = currentProgress - delta * aDouble;

                this.healthBar.setProgressInstant(newProgress);
                this.healthBar.setProgress(newProgress);
            });
        }

        healthText.setValue(NumberUtility.classicalFormat(Math.max(UserMetadata.getInstance().getHealth(), 1)) + "/" +
                NumberUtility.classicalFormat(Math.max(UserMetadata.getInstance().getMaxHealth(), 1)));
        healthPercentText.setValue(String.format("%.0f%%", Math.max(progress * 100, 1)));
    }

    public void updateLevel() {
        if (UserMetadata.getInstance().isSubscribed()) this.levelBar.getBar().setColor(Palette.GREEN);
        else this.levelBar.getBar().setColor(Palette.ORANGE);

        val progress = UserMetadata.getInstance().getCurrentExp() / UserMetadata.getInstance().getMaxExp();
        val currentProgress = this.levelBar.getRenderProgress();

        if (Animation.hasAnimation(this.levelBar, "update_exp")) {
            Animation.stop(this.levelBar);
        }

        if (this.levelBar.getRenderProgress() <= 0) {
            this.levelBar.setProgressInstant(progress);
            this.levelBar.setProgress(progress);
        } else {
            Animation.play(this.levelBar, "update_exp", .3, (element, aDouble) -> {
                val delta = currentProgress - progress;
                val newProgress = currentProgress - delta * aDouble;

                this.levelBar.setProgressInstant(newProgress);
                this.levelBar.setProgress(newProgress);
            });
        }

        levelText.setValue("Уровень " + UserMetadata.getInstance().getLevelColor() + UserMetadata.getInstance().getLevel());
        if (UserMetadata.getInstance().getMaxExp() != -1D) {
            levelPercentText.setEnabled(true);
            levelPercentText.setValue(String.format("%.0f%%", Math.max(Math.floor(progress * 100D), UserMetadata.getInstance().getCurrentExp() > 0 ? 1 : 0)));
        } else {
            levelPercentText.setEnabled(false);
        }
    }

}
