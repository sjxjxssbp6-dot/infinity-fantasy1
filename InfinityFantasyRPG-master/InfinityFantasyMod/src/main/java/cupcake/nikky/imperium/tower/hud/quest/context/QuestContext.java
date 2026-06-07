package cupcake.nikky.imperium.tower.hud.quest.context;

import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.quest.QuestContextStorage;
import cupcake.nikky.imperium.tower.item.FantasyRenderItem;
import cupcake.nikky.imperium.tower.text.FantasyText;
import lombok.val;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

public class QuestContext extends CarvedRectangle {

    private final FantasyButton button = new FantasyButton();
    private final Text titleText = new Text();
    private final HorizontalLayout rewardLayout = new HorizontalLayout(8D);

    private final VerticalLayout generalLayout = new VerticalLayout(8D);

    public QuestContext() {
        this.initialize();
        this.update();
    }

    private void initialize() {
        titleText.setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_LEFT);
        rewardLayout.setOrigin(Relative.BOTTOM_LEFT).setOrigin(Relative.BOTTOM_LEFT).setSize(new V3(40, 40));

        val questState = QuestContextStorage.getInstance().getQuestState();
        val canReceive = QuestContextStorage.getInstance().getCanReceive() == 1;

        button.setActive(questState != QuestState.RECEIVE);

        if (!canReceive) button.setColor(ButtonColor.RED.getDefaultColor());

        button.onLeftClick(element -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("q-context-click", 0.25)) return;

            new ModTransfer()
                    .writeInt(QuestContextStorage.getInstance().getId())
                    .writeInt(QuestContextStorage.getInstance().getQuestState().getState())
                    .send("imp:quest-cont-click");
        });

        this.setColor(Palette.GREY.alpha(.26));
        this.addChild(generalLayout);
    }

    public void update() {
        generalLayout.clearChilds();

        val canReceive = QuestContextStorage.getInstance().getCanReceive() == 1;
        val questState = QuestContextStorage.getInstance().getQuestState();

        if (canReceive) {
            String text;

            if (questState == QuestState.NOT_RECEIVE) text = "Поручиться";
            else if (questState == QuestState.COMPLETE_WITHOUT_PASS) text = "Сдать";
            else if (questState == QuestState.RECEIVE) text = "В процессе";
            else text = "Выполнено";

            button.setText(text);
        } else button.setText("Недоступно");

        ButtonColor buttonColor;

        if (questState == QuestState.NOT_RECEIVE) {
            buttonColor = ButtonColor.BLUE;
        } else if (questState == QuestState.COMPLETE_WITHOUT_PASS) {
            buttonColor = ButtonColor.GREEN;
        } else if (questState == QuestState.RECEIVE) {
            buttonColor = ButtonColor.YELLOW;
        } else {
            buttonColor = ButtonColor.GREY;
        }

        if (!canReceive) buttonColor = ButtonColor.RED;

        button.setForceCanClick(canReceive && (questState == QuestState.NOT_RECEIVE || questState == QuestState.COMPLETE_WITHOUT_PASS))
                .setSize(new V3(button.getText().getSize().getX() + 16 * 2, 38));

        button.setButtonColor(buttonColor);

        button.setActive(questState != QuestState.RECEIVE && canReceive);

        titleText.setValue(QuestContextStorage.getInstance().getTitle());

        val descriptionText = new FantasyText(22.4, QuestContextStorage.getInstance().getDescription());
        descriptionText.setAutoSkip(560).setShadow(true).setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_LEFT);

        val goalsText = new FantasyText(22.4, QuestContextStorage.getInstance().getGoals());
        goalsText.setShadow(true).setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_LEFT);

        rewardLayout.clearChilds();
        QuestContextStorage.getInstance().getReward().forEach(itemStack -> {
            val item = new FantasyRenderItem(itemStack, true, true).setSize(new V3(32, 32));
            rewardLayout.addChild(item);
        });

        val goalLayout = new VerticalLayout(5D).addChild(
                new Text("Цели").setShadow(true).setColor(ButtonColor.YELLOW.getDefaultColor()).setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM_LEFT),
                goalsText
        );

        if (QuestContextStorage.getInstance().getGoals().isEmpty()) goalLayout.setEnabled(false);

        val scroll = new ScrollView<VerticalLayout>().setDisableBarOnInactive(true);
        scroll.getBar().setPos(new V3(-20, 0));

        generalLayout.addChild(titleText,
                new VerticalLayout(10).addChild(
                        scroll.setColor(Palette.GREY).setSize(new V3(586, Math.min(150, descriptionText.getSize().getY()))).addChild(descriptionText),
                        goalLayout,
                        new VerticalLayout(5D).addChild(
                                new Text("Награды").setShadow(true).setColor(ButtonColor.YELLOW.getDefaultColor()).setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM_LEFT),
                                rewardLayout
                        ),
                        button
                ))
                .setPos(new V3(16, 16));

        this.setSize(generalLayout.getSize().clone().add(32, 32, 0).setX(594));
    }

}
