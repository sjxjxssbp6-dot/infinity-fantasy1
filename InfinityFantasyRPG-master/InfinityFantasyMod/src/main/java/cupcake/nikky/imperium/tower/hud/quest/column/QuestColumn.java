package cupcake.nikky.imperium.tower.hud.quest.column;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.data.SettingsStorage;
import cupcake.nikky.imperium.tower.data.quest.QuestStorage;
import cupcake.nikky.imperium.tower.hud.SettingsContext;
import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import dev.xdark.clientapi.gui.ingame.ChatScreen;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class QuestColumn extends VerticalLayout {

    private final VerticalScrollView<VerticalLayout> questColumnScroll = new VerticalScrollView<>();

    private final FantasyMod mod;

    public QuestColumn(@NotNull final FantasyMod fantasyMod) {
        super(20D);

        this.mod = fantasyMod;

        this.initialize();
        this.update();

        this.onPreTransform(element -> this.setScale(ScaleUtility.findCurrentScale()));
    }

    private void initialize() {
        this.setPos(new V3(-16, 16));
        questColumnScroll.getLayout().setSpacing(10D).setOrigin(Relative.TOP_RIGHT).setAlign(Relative.TOP_RIGHT).setInteractive(true);
        questColumnScroll.getLayout().setInset(new V3(5, 5));

        questColumnScroll.setDisableBarOnInactive(true)
                .setColor(Palette.BLUE.alpha(.7))
                .setOrigin(Relative.TOP_RIGHT)
                .setAlign(Relative.TOP_RIGHT);

        questColumnScroll.onPreTransform(abstractElement ->
                questColumnScroll.setSize(new V3(questColumnScroll.getChilds().stream()
                        .mapToDouble(element -> element.getSize().getX() * 1.1)
                        .max().orElse(400D), 600))
        );

        this.setOrigin(Relative.TOP_RIGHT).setAlign(Relative.TOP_RIGHT);
        this.onPreTick(element -> {
            val isDataEmpty = QuestStorage.getInstance().getQuests().isEmpty();
            val isEmpty = !isDataEmpty && questColumnScroll.getChilds().isEmpty();
            val visibility = SettingsStorage.getInstance().getSettings().getBoolean(SettingsType.QUEST_COLUMN_VISIBILITY);

            if ((visibility && isEmpty) || (!visibility && !isEmpty)) {
                this.update();
            }
        });

        this.addChild(questColumnScroll).setRenderLayer(-1);
    }

    public void update() {
        questColumnScroll.clearChilds();

        if (!SettingsStorage.getInstance().getSettings().getBoolean(SettingsType.QUEST_COLUMN_VISIBILITY)) return;

        QuestStorage.getInstance().getQuests().forEach(questData -> {
            if (!questData.isHide()) {
                val box = QuestBox.of(questData);
                questColumnScroll.addChild(box.setPos(new V3(-4, 0)));

                if (questData.isDaily()) FantasyMod.tooltipHandler().applyHoverTooltip(box.getTitleText(), "Ежедневное задание");
            }
        });
    }

}
