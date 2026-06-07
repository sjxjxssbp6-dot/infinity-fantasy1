package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;

import java.util.concurrent.atomic.AtomicBoolean;

public class NotifySettings extends BaseUserSettings {

    private final AtomicBoolean doNotDisturb = new AtomicBoolean();

    public NotifySettings(User user) {
        super(user);
    }

    public void setDoNotDisturb(final boolean doNotDisturb) {
        for (;;) {
            var old = this.doNotDisturb.get();
            if (this.doNotDisturb.compareAndSet(old, doNotDisturb)) break;
        }
    }

    public boolean isDoNotDisturb() {
        return doNotDisturb.get();
    }

}
