package cupcake.nikky.imperium.tower.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class NavigationStorage {

    @Getter
    private static final NavigationStorage instance = new NavigationStorage();

    @Getter
    @Setter
    private boolean treeIndicator;

    public void clear() {
        treeIndicator = false;
    }

}
