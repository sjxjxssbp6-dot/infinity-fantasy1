package cupcake.nikky.imperium.tower.hud.guide;

import cupcake.nikky.imperium.tower.client.protocol.GuidePage;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

import java.util.ArrayList;
import java.util.List;

public class GuidePageContext extends CarvedRectangle {

    public GuidePageContext(@NotNull final List<AbstractElement> elements) {
        this.setColor(Palette.GREY_MIDDLE.alpha(.68)).setSize(new V3(705, 800));

        val layout = new VerticalLayout(8D);
        val scroll = new ScrollView<VerticalLayout>().setDisableBarOnInactive(true);
        elements.forEach(layout::addChild);
        this.addChild(scroll.addChild(layout).setColor(Palette.GREY_LIGHT.alpha(.62)).setPos(new V3(15, 15)).setSize(getSize().clone().add(-15D * 2D - 5D, -15D * 2D, 0)));
    }

    public static GuidePageContextBuilder builder(@NotNull final GuidePage guidePage) {
        return new GuidePageContextBuilder(guidePage);
    }

    @RequiredArgsConstructor
    public static class GuidePageContextBuilder {

        private final GuidePage guidePage;
        public final List<AbstractElement> elements = new ArrayList<>();

        public GuidePageContextBuilder appendElement(@NotNull final String... text) {
            return this.appendElement(new Text(text).setShadow(true).setLineHeight(22.4).setAutoSkip(650D).setColor(Palette.WHITE));
        }

        public GuidePageContextBuilder appendElement(@NotNull final Text text) {
            text.setLineHeight(22.4);
            this.elements.add(text);
            return this;
        }

        public GuidePageContextBuilder appendElement(@NotNull final AbstractElement element) {
            this.elements.add(element);
            return this;
        }

        public GuidePageContext build() {
            elements.add(0, new Text(guidePage.getTitle()).setLineHeight(22.4).setColor(Palette.WHITE).setScale(new V3(1.5, 1.5)));
            elements.add(1, new Text(""));
            return new GuidePageContext(elements);
        }

    }

}
