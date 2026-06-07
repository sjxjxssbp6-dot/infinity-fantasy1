package cupcake.nikky.imperium.tower.item.exact.aspect.event;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.user.User;
import org.jetbrains.annotations.NotNull;

public interface AspectOutboundDamageEvent extends AspectEvent {

    Monster getTarget();

    User getUser();

    int getLevel();

    static AspectOutboundDamageEvent create(@NotNull Monster target, @NotNull User user, int level) {
        return new AspectOutboundDamageEvent() {
            @Override
            public Monster getTarget() {
                return target;
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
