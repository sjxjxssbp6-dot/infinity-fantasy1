package cupcake.nikky.imperium.tower.item.exact.aspect.event;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.user.User;
import org.jetbrains.annotations.NotNull;

public interface AspectHandEvent extends AspectEvent {

    ImperiumItem getItem();

    User getUser();

    AttributeMap getAttributes();

    int getLevel();

    static AspectHandEvent create(@NotNull ImperiumItem item, @NotNull User user, @NotNull AttributeMap attributes, int level) {
        return new AspectHandEvent() {
            @Override
            public ImperiumItem getItem() {
                return item;
            }

            @Override
            public User getUser() {
                return user;
            }

            @Override
            public AttributeMap getAttributes() {
                return attributes;
            }

            @Override
            public int getLevel() {
                return level;
            }
        };
    }

}
