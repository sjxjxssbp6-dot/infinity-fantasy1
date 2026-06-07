package cupcake.nikky.imperium.tower.hud.quest.context;

import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.quest.QuestContextStorage;
import cupcake.nikky.imperium.tower.data.quest.QuestListData;
import cupcake.nikky.imperium.tower.data.quest.QuestListStorage;
import cupcake.nikky.imperium.tower.gui.FantasyGuiScreen;
import dev.xdark.clientapi.gui.Screen;
import dev.xdark.clientapi.item.TooltipFlag;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

public class QuestContextList extends FantasyGuiScreen {

    @Getter
    private final QuestContext questContext = new QuestContext();

    private final GridLayout generalLayout = new GridLayout(LayoutPriority.HORIZONTAL, 0, 10, 6, 6);

    public QuestContextList() {
        val scroll = new ScrollView().setDisableBarOnInactive(true);
        scroll.getBar().setPos(new V3(5, 0));

        scroll.setColor(Palette.GREY.alpha(.62)).setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_LEFT);

        scroll.onPreTransform(element -> {
            val size = generalLayout.getChilds().size();
            val finalSize = Math.min(2, size / 9 + (size % 9 > 0 ? 1 : 0));
            val columns = generalLayout.getColumns();
            scroll.setSize(new V3(columns * 54D + columns * 6D, 54D * finalSize + 6D * finalSize));
        });

        val titleList = new HorizontalLayout().addChild(new Text("Список поручений").setColor(Palette.YELLOW).setAlign(Relative.LEFT).setOrigin(Relative.LEFT));
        titleList.onPreTransform(element -> titleList.setSize(scroll.getSize().clone().setY(16)));

        val centralLayout = new VerticalLayout(10D).setRenderLayer(1);
        centralLayout.onPreTransform(element -> {
            centralLayout.setPos(new V3(0, -250)).setOrigin(Relative.BOTTOM).setAlign(Relative.BOTTOM);
        });

        this.addChild(centralLayout.addChild(questContext.setOrigin(Relative.CENTER).setAlign(Relative.CENTER),
                        new VerticalLayout(5D).addChild(titleList, scroll.addChild(generalLayout.setOrigin(Relative.CENTER).setAlign(Relative.CENTER))))
        );
    }

    public void update() {
        if (QuestListStorage.getInstance().isUpdated()) {
            QuestListStorage.getInstance().setUpdated(false);
            generalLayout.clearChilds();

            val quests = QuestListStorage.getInstance().getQuests();
            quests.keySet().stream().sorted().forEach(id -> {
                val quest = quests.get(id);
                this.generalLayout.addChild(this.createQuestComponent(id, quest));
            });

            for (int i = 0; i < 10 - quests.size() % 10; i++) {
                this.generalLayout.addChild(this.createEmptyComponent());
            }
        }

        questContext.update();
    }

    private AbstractElement createQuestComponent(final int id, @NotNull final QuestListData quest) {
        val questButton = new FantasyButton();
        questButton.getText().setEnabled(false);

        val itemComponent = new ItemRender(quest.getItemStack());

        questButton.setCarveSize(4).setSize(new V3(54, 54));
        questButton.addChild(itemComponent.setSize(new V3(38, 38)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
        questButton.setTooltip(String.join("\n", quest.getItemStack().getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL)));

        val state = quest.getQuestState();
        questButton.setButtonColor(findColorByState(state, quest.isCanReceive()));

        questButton.onLeftClick(element -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("quest-list-click", 0.25)) {
                questButton.setPlaySound(false);
                return;
            } else questButton.setPlaySound(true);

            new ModTransfer().writeInt(id).send("imp:quest-lclick");
        });

        questButton.onPreTick(element -> {
            if (QuestContextStorage.getInstance().getId() == id) {
                if (questButton.isCanClick()) questButton.setForceCanClick(false).setPlaySound(false).setActive(false);
            } else if (!questButton.isCanClick()) {
                questButton.setForceCanClick(true).setPlaySound(true).setActive(true);
            }
        });

        return questButton;
    }

    private AbstractElement createEmptyComponent() {
        val result = new CarvedRectangle(4);
        result.setSize(new V3(54, 54)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setColor(findColorByState(QuestState.PASS, true).getDisableColor());
        return result;
    }

    private ButtonColor findColorByState(@NotNull final QuestState questState, final boolean canReceive) {
        if (questState == QuestState.NOT_RECEIVE && !canReceive) {
            return ButtonColor.RED;
        }

        if (questState == QuestState.NOT_RECEIVE) {
            return ButtonColor.BLUE;
        } else if (questState == QuestState.RECEIVE) {
            return ButtonColor.YELLOW;
        } else if (questState == QuestState.COMPLETE_WITHOUT_PASS) {
            return ButtonColor.GREEN;
        } else {
            return ButtonColor.GREY;
        }
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode()) {
            new ModTransfer().send("imp:quest-close");
            this.close();
        }
    }

}
