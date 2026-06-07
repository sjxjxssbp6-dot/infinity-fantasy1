package cupcake.nikky.imperium.tower.handler;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.data.UserMetadata;
import cupcake.nikky.imperium.tower.utility.NumberUtility;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.AbstractElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TooltipHandler {

    public void initialize(@NotNull final FantasyMod mod) {
        this.applyHoverTooltip(mod.getHotBarContext().getAdvancedHotBarBox().getFragmentTimeBox(), Collections.singletonList("Оставшееся время пребывания на Осколке"));
        this.applyHoverTooltip(mod.getHotBarContext().getAdvancedHotBarBox().getFragmentRespawnBox(), Collections.singletonList("Оставшееся количество возрождений на Осколке"));

        this.applyHoverTooltip(mod.getHotBarContext().getHealthBar(), "Ваше здоровье");
        this.applyHoverTooltip(mod.getHotBarContext().getManaBar(), "Ваша мана");

        this.applyHoverTooltip(mod.getHotBarContext().getLevelBar(), levelBar -> {
            val currentExp = UserMetadata.getInstance().getCurrentExp();
            val maxExp = UserMetadata.getInstance().getMaxExp();
            val multiplier = UserMetadata.getInstance().getMultiplier();

            if (maxExp != -1D) {
                return Arrays.asList(UserMetadata.getInstance().getLevelColor() + UserMetadata.getInstance().getLevel() + " уровень",
                        String.format("%s/%s опыта (%d%%)",
                                NumberUtility.classicalFormat(currentExp),
                                NumberUtility.classicalFormat(maxExp),
                                Math.max((int) Math.floor(currentExp / maxExp * 100D), currentExp > 0 ? 1 : 0)),
                        String.format("Вы получаете %d%% боевого опыта", (int) (100 * multiplier)));
            }

            return Arrays.asList(UserMetadata.getInstance().getLevelColor() + UserMetadata.getInstance().getLevel() + " уровень",
                    String.format("Вы получаете %d%% боевого опыта", (int) (100 * multiplier)));
        });
    }

    public void applyHoverTooltip(@NotNull final AbstractElement<?> element, @NotNull final Function<AbstractElement<?>, List<String>> tooltip) {
        element.onPreTick(element1 -> element1.setTooltip(String.join("\n", tooltip.apply(element1))));
    }

    public void applyHoverTooltip(@NotNull final AbstractElement element, @NotNull final List<String> tooltip) {
        element.setTooltip(String.join("\n", tooltip));
    }

    public void applyHoverTooltip(@NotNull final AbstractElement element, @NotNull final String... tooltip) {
        element.setTooltip(String.join("\n", tooltip));
    }

}