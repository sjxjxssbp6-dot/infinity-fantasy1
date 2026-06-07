package cupcake.nikky.imperium.tower.hud.navigation;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.data.NavigationStorage;
import cupcake.nikky.imperium.tower.data.UserMetadata;
import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class NavigationContext extends CarvedRectangle {

    @Getter
    private final Map<NavigationHandler, Button> data = new EnumMap<>(NavigationHandler.class);
    @Getter
    private final GridLayout gridLayout = new GridLayout(LayoutPriority.HORIZONTAL, 0,3, 7.5D);

    public NavigationContext(@NotNull final FantasyMod mod) {
        this.initialize(mod);
        this.update(mod);

        this.onPreTransform(element -> this.setScale(ScaleUtility.findCurrentScale()));
    }

    private void initialize(@NotNull final FantasyMod mod) {
        this.setColor(Palette.BLACK.alpha(0D)).setRenderLayer(1);
        this.setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_LEFT).setPos(new V3(16, 16));

        onPreTick(element -> {
            for (NavigationHandler navigationHandler : new NavigationHandler[]{NavigationHandler.FIRMOLOG, NavigationHandler.STORAGE}) {
                val button = data.get(navigationHandler);
                if (button != null) button.setPlaySound(UserMetadata.getInstance().isSubscribed());
            }
        });

        for (NavigationHandler value : NavigationHandler.values()) {
            val button = new FantasyButton();
            button.getText().setEnabled(false);

            AbstractElement image;

            if (value.getIcon().length() == 1) {
                image = new Image(value.getIcon()).setSize(new V3(48, 48)).setAlign(Relative.TOP).setOrigin(Relative.TOP).setPos(new V3(0, 5));
            } else {
                val location = value.getIcon().startsWith("neitexx:") ?
                        ResourceLocation.of(value.getIcon()) : ResourceLocation.of("minecraft", "textures/items/" + value.getIcon());
                image = new Image(location).setSize(new V3(48, 48)).setAlign(Relative.TOP).setOrigin(Relative.TOP).setPos(new V3(0, 5));
            }

            FantasyMod.tooltipHandler().applyHoverTooltip(button, element -> Collections.singletonList(value.getHoverHandler().apply(mod)));

            button.addChild(image, new Text(value.getTitle().get()).setAlign(Relative.BOTTOM).setOrigin(Relative.BOTTOM).setPos(new V3(0, -3)));
            button.setCarveSize(2D).setSize(new V3(74, 74));

            button.onLeftClick(element -> {
                button.setOutlineColor((Color) null);
                value.getHandlerClick().accept(mod);
                NavigationStorage.getInstance().setTreeIndicator(false);
            });

            data.put(value, button);
            gridLayout.addChild(button);
        }

        this.addChild(gridLayout);
    }

    public void update(FantasyMod mod) {
        getData().forEach((navigationHandler, element) -> {
            element.setColor(navigationHandler.getButtonColorHandler().apply(mod).alpha(.62));
            element.getChilds().stream().filter(Text.class::isInstance)
                    .map(Text.class::cast)
                    .forEach(text -> text.setValue(navigationHandler.getTitle().get()));
            ((Button) element).setText("");
        });
    }

}
