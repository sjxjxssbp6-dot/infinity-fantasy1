package cupcake.nikky.imperium.tower.item.exact.aspect.event;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.user.User;
import org.jetbrains.annotations.NotNull;

public interface AspectUseEvent extends AspectEvent {

    ImperiumItem getItem();

    User getUser();

    int getLevel();

    int getCooldown();

    void setCooldown(int cooldown);

    static AspectUseEvent create(@NotNull final ImperiumItem item, @NotNull final User user, final int level) {
        return new AspectUseEvent() {

            private int cooldown;

            @Override
            public int getCooldown() {
                return cooldown;
            }

            @Override
            public void setCooldown(int cooldown) {
                this.cooldown = cooldown;
            }

            @Override
            public ImperiumItem getItem() {
                return item;
            }

            @Override
            public User getUser() {
                return user;
            }

            @Override
            public int getLevel() {
                return level;
            }
        };
    }

}
