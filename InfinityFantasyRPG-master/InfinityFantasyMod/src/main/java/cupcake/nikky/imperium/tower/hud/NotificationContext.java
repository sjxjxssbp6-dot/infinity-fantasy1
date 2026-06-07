package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.hud.notification.NotificationData;
import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.animation.EasingFunc;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.Rectangle;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.util.*;

import java.util.Deque;
import java.util.LinkedList;

public class NotificationContext extends CarvedRectangle {

    private NotificationData currentNotification;

    private final Deque<NotificationData> nextNotifications = new LinkedList<>();
    private final Text title = new Text();

    private final Rectangle innerCooldown = new Rectangle();
    private final Rectangle outerCooldown = new Rectangle();

    private final FantasyMod fantasyMod;

    public NotificationContext(@NotNull final FantasyMod fantasyMod) {
        this.fantasyMod = fantasyMod;

        this.setEnabled(false);

        this.setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.BOTTOM_RIGHT);
        this.onLeftClick(element -> this.hide(true));
        this.setColor(Palette.BLACK.alpha(.62));

        this.addChild(
                title.setShadow(true),
                outerCooldown.setOrigin(Relative.BOTTOM).setAlign(Relative.BOTTOM).setColor(Palette.BLUE_DARK.alpha(.67))
                        .addChild(innerCooldown.setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setColor(Palette.BLUE_LIGHT.alpha(.67)))
        );
    }

    public void applyCurrentData() {
        title.setLineHeight(24).setValue(currentNotification.getLines()).setPos(new V3(8, 8, 0));

        this.setSize(title.getSize().clone().add(16, 16, 0));
        this.setPos(new V3(getSize().getX() + 16, -44D, 0));

        this.setCarveSize(2D);
        this.setAlpha(1D);

        val cooldownSize = getSize().clone().add(-3D * 2D, 0, 0).setY(3);
        innerCooldown.setSize(cooldownSize.clone());
        outerCooldown.setSize(cooldownSize.clone());
    }

    public void addNext(@NotNull final NotificationData data) {
        if (currentNotification != null) {
            if (data.getType() == SystemNotificationType.NONE) {
                if (currentNotification.getType() != SystemNotificationType.NONE) {
                    this.hide(true);
                }
            } else {
                this.hide(true);
            }
        }

        if (currentNotification == null) {
            currentNotification = data;
            this.applyCurrentData();
            this.show();
        } else {
            this.nextNotifications.addLast(data);
        }
    }

    public void show() {
        if (currentNotification == null) return;
        SoundUtil.play("sounds/custom/" + (currentNotification.isWithConfusion() ? "confusion.ogg" : "tap.ogg"), 1F, 1F);

        this.setEnabled(true);
        fantasyMod.getBindInfoContext().setEnabled(false);

        val defaultOffsetX = getPos().getX();
        val offsetX = getPos().getX() + 16D;

        Animation.play(this, "play_1", 0.3, EasingFunc.EASE_OUT_BACK, (element, progress) -> this.getPos().setX(defaultOffsetX - offsetX * progress));

        val duration = Math.max(Math.floor(title.getText().replace("\n", " ").split("\\s+").length / 2D), 3);

        val cooldownSize = innerCooldown.getSize().getX();
        Animation.play(this, "waiting", 0.3 + duration * 2D, EasingFunc.LINEAR, (element, progress) -> innerCooldown.setSize(new V3(cooldownSize - cooldownSize * progress, 3)))
                .onComplete(element -> this.hide(false));
    }

    public void hide(final boolean force) {
        if (currentNotification == null) return;

        if (force) {
            Animation.stop(this);
            this.setEnabled(false);

            currentNotification = null;
            fantasyMod.getBindInfoContext().setEnabled(true);
            return;
        }

        currentNotification = null;

        Animation.stop(this);
        Animation.play(this, "play_2", 0.35, EasingFunc.EASE_IN_BACK, (element2, progress2) -> this.setAlpha(1 - progress2))
                .onComplete(element -> {
                    this.setEnabled(false);

                    this.currentNotification = nextNotifications.pollFirst();
                    if (currentNotification != null) {
                        this.applyCurrentData();
                        this.show();
                    } else {
                        fantasyMod.getBindInfoContext().setEnabled(true);
                    }
                });
    }

}
