package cupcake.nikky.imperium.tower.hud;

import com.google.common.collect.ImmutableList;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.data.AttributeStorage;
import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.Comparator;
import java.util.Map;

public class AttributeContext extends CarvedRectangle {

    private final VerticalLayout attributeLayout = new VerticalLayout(8D);
    private FantasyButton attackButton;
    private FantasyButton defenseButton;

    private int type = 0;

    public AttributeContext() {
        this.initialize();
        this.update();
        this.onPreTransform(element -> this.setScale(ScaleUtility.findCurrentScale()));
    }

    private void initialize() {
        attributeLayout.setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_LEFT).setPos(new V3(10, 10));
        this.setColor(Palette.GREY.alpha(.62)).setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(16, 0));
        this.setCarveSize(2D).setCarveCorners(false, false, true, true);

        this.attackButton = new FantasyButton();
        attackButton.setCarveSize(2D).setCarveCorners(true, true, false, false);

        attackButton.addChild(new Image("\uE145").setSize(new V3(20, 20)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
        attackButton.setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.TOP_LEFT);
        attackButton.setButtonColor(ButtonColor.GREY);
        attackButton.setForceCanClick(false);
        attackButton.setActive(false);
        attackButton.onLeftClick(element -> {
            this.type = 0;
            attackButton.setActive(false);
            defenseButton.setActive(true);

            attackButton.setCanClick(false);
            defenseButton.setForceCanClick(true);
            this.update();
        });

        this.defenseButton = new FantasyButton();
        defenseButton.setCarveSize(2D).setCarveCorners(true, true, false, false);

        defenseButton.addChild(new Image("\uE1A9").setSize(new V3(20, 20)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
        defenseButton.setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.TOP_RIGHT);
        defenseButton.setButtonColor(ButtonColor.GREY);
        defenseButton.onLeftClick(element -> {
            this.type = 1;
            attackButton.setActive(true);
            defenseButton.setActive(false);

            defenseButton.setCanClick(false);
            attackButton.setForceCanClick(true);
            this.update();
        });

        this.addChild(attackButton, defenseButton, attributeLayout);
    }

    public void update() {
        this.attributeLayout.clearChilds();

        Map<ImperiumAttribute, Double> attributes;

        if (type == 0) {
            attributes = AttributeStorage.getInstance().findAttackAttributes();
        } else {
            attributes = AttributeStorage.getInstance().findDefenseAttributes();
        }

        val keySet = attributes.keySet();
        keySet.stream().sorted(Comparator.comparingInt(ImperiumAttribute::getPosition)).forEach(attribute -> attributeLayout.addChild(this.createTextObject(attribute, attributes.get(attribute))));

        this.setSize(new V3(325D, AttributeStorage.getInstance().maxAmount() * 24D + 20D));
        attackButton.setSize(new V3(163, 35));
        defenseButton.setSize(new V3(163, 35));
    }

    private AbstractElement createTextObject(@NotNull final ImperiumAttribute attribute, final double value) {
        final ImmutableList<ImperiumAttribute> resistances = ImmutableList.of(ImperiumAttribute.FIRE_RESISTANCE, ImperiumAttribute.COLD_RESISTANCE,
                ImperiumAttribute.LIGHTNING_RESISTANCE, ImperiumAttribute.CHAOS_RESISTANCE);
        val valueColor = value < 0 ? "&c" : "&3";
        val line = resistances.contains(attribute) ? attribute.getTitle() + ": " + valueColor + (int) value + attribute.getSpecialSym() : attribute.generateWithValue(value).substring(2);

        return new Text(("&r" + line).replace("&", "§")).setShadow(true);
    }

}
