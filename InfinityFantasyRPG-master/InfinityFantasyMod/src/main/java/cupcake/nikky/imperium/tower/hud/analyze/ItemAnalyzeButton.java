package cupcake.nikky.imperium.tower.hud.analyze;

import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeContainer;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.element.VerticalLayout;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class ItemAnalyzeButton extends VerticalLayout {

    private final ItemAnalyzeContainer container;
    private final ItemAnalyzeContext context;

    private boolean closed = true;

    public ItemAnalyzeButton(@NotNull final ItemAnalyzeContainer container, @NotNull final ItemAnalyzeContext context) {
        super(2D);
        this.container = container;
        this.context = context;
        this.addTopButton();
    }

    private void addTopButton() {
        val button = new FantasyButton();
        button.setButtonColor(ButtonColor.GREY);
        button.setCarveSize(2D);

        val titleText = new Text(container.getTitle()).setShadow(true).setInteractive(false).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
        val tiersText = new Text(String.valueOf(container.getTiers())).setShadow(true).setInteractive(false).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
        val baseLevelText = new Text(String.valueOf(container.getMaxBaseLevel())).setShadow(true).setInteractive(false).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
        val weightText = new Text(String.valueOf(container.getSumWeights())).setShadow(true).setInteractive(false).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);

        button.setSize(new V3(1_100, 40)).addChild(
                titleText.setPos(new V3(10, 0)),
                tiersText.setPos(new V3(600, 0)),
                baseLevelText.setPos(new V3(800, 0)),
                weightText.setPos(new V3(1000, 0))
        );

        button.onLeftClick(element -> {
            if (closed) this.open();
            else this.close();

            context.getGeneralLayout().rescale();
        });

        this.addChild(button);
    }

    private void addAnalyzeComponents() {
        container.getAnalyzes().forEach(itemAnalyze -> {
            val component = new CarvedRectangle().setCarveSize(2D);
            component.setColor(Palette.GREY_MIDDLE.alpha(.67));

            val min = itemAnalyze.getMin();
            val max = itemAnalyze.getMax();
            val amplitude = itemAnalyze.getAmplitude();

            val canRound = canRound(itemAnalyze.getMin()) && canRound(itemAnalyze.getMax());
            val hasAmplitude = amplitude != 0D;

            String title = container.getTitle()
                    .replace("#.#", "#")
                    .replace("%", "%%")
                    .replace("#", canRound ? (hasAmplitude ? "(%.0f-%.0f)" : "%.0f-%.0f") : (hasAmplitude ? "(%.1f-%.1f)" : "%.1f-%.1f"));
            if (hasAmplitude) {
                title = String.format(title, min - amplitude, max - amplitude, min + amplitude, max + amplitude);
            } else title = String.format(title, min, max);

            val titleText = new Text(title).setShadow(true).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
            val tiersText = new Text(String.valueOf(itemAnalyze.getTier())).setShadow(true).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
            val baseLevelText = new Text(String.valueOf(itemAnalyze.getBaseLevel())).setShadow(true).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
            val weightText = new Text(String.valueOf(itemAnalyze.getWeight())).setShadow(true).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);

            component.setSize(new V3(1_100, 30)).addChild(
                    titleText.setPos(new V3(10, 0)),
                    tiersText.setPos(new V3(600, 0)),
                    baseLevelText.setPos(new V3(800, 0)),
                    weightText.setPos(new V3(1000, 0))
            );

            this.addChild(component);
        });
    }

    private boolean canRound(final double value) {
        return value % (int) value == 0D;
    }

    private void open() {
        if (!closed) return;

        context.getGeneralLayout().getChilds().stream()
                .filter(ItemAnalyzeButton.class::isInstance)
                .map(ItemAnalyzeButton.class::cast)
                .filter(itemAnalyzeButton -> !itemAnalyzeButton.closed)
                .forEach(ItemAnalyzeButton::close);

        closed = false;
        this.addAnalyzeComponents();
    }

    private void close() {
        if (closed) return;
        closed = true;
        this.clearChilds();
        this.addTopButton();
    }

}
