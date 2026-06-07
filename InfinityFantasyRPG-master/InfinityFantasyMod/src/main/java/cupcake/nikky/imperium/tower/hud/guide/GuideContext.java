package cupcake.nikky.imperium.tower.hud.guide;

import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.GuidePage;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.gui.FantasyGuiScreen;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import dev.xdark.clientapi.gui.Screen;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class GuideContext extends FantasyGuiScreen {

    private final VerticalLayout buttonsLayout = new VerticalLayout(5D);
    private final HorizontalLayout generalLayout = new HorizontalLayout(5D);

    @Getter private GuidePageList currentPage;
    private Button lastActive;

    public GuideContext() {
        this.initialize();
        this.initializePage();
    }

    private void initialize() {
        this.addChild(generalLayout.setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
    }

    public void fillButtons() {
        buttonsLayout.clearChilds();
        Arrays.stream(GuidePage.values()).sorted(Comparator.comparingInt(GuidePage::ordinal))
                .map(GuidePageList::findByPage).filter(Objects::nonNull)
                .forEachOrdered(guidePageList -> buttonsLayout.addChild(this.createButton(guidePageList)));
    }

    public void initializePage() {
        this.currentPage = GuidePageList.LEVEL_AND_CORPSE_CONTEXT;

        val buttonScroll = new ScrollView<VerticalLayout>().setDisableBarOnInactive(true);
        buttonScroll.getBar().setPos(new V3(-5, 0));
        buttonScroll.setColor(Palette.GREY_LIGHT.alpha(.67));

        this.generalLayout.addChild(buttonScroll.setSize(new V3(232, 800)).addChild(buttonsLayout));

        this.generalLayout.addChild(this.currentPage.getPageContext());
        this.fillButtons();
    }

    public void updatePage(@Nullable final GuidePageList pageList) {
        val children = this.generalLayout.getChilds();
        this.generalLayout.removeChild(children.get(children.size() - 1));

        if (pageList != null) this.generalLayout.addChild(pageList.getPageContext());
        currentPage = pageList;
    }

    private FantasyButton createButton(@NotNull final GuidePageList pageList) {
        val button = new FantasyButton();
        button.setCarveSize(3D);
        val title = pageList.getPage().getMiniTitle();
        button.setText(title);
        button.getText().setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(16, 0));
        button.setSize(new V3(222, 54));
        button.setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        button.setButtonColor(ButtonColor.GREY);

        if (currentPage == pageList) {
            lastActive = button.setActive(false);
            button.setForceCanClick(false);
        }

        button.onLeftClick(element -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("guide-context-click", 0.1)) return;
            if (lastActive != null) {
                lastActive.setActive(true);
                ((FantasyButton) lastActive).setForceCanClick(true);
            }
            lastActive = button;
            button.setActive(false);
            button.setCanClick(false);
            this.updatePage(pageList);
        });

        return button;
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode() || code == KeyHandler.HELP.container().key()) {
            this.close();
        }
    }

}
