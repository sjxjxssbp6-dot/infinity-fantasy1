package cupcake.nikky.imperium.tower.hud.quest.column;

import cupcake.nikky.imperium.tower.data.quest.QuestData;
import cupcake.nikky.imperium.tower.text.FantasyText;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.stream.Stream;

@Getter
public class QuestBox extends VerticalLayout {

    private final QuestData questData;

    private FantasyText titleText;
    private final VerticalLayout descriptionBox = new VerticalLayout();

    private QuestBox(@NotNull final QuestData questData) {
        super(2D);

        this.questData = questData;

        this.initialize();
    }

    public static QuestBox of(@NotNull final QuestData questData) {
        return new QuestBox(questData);
    }

    private void initialize() {
        titleText = new FantasyText(questData.getTitle());
        titleText.setShadow(true).setInteractive(true)/*.setScale(new V3(1.1, 1.1))*/.setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT);
        descriptionBox.setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT);

        this.setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT);

        Stream.of(questData.getDescription()).forEach(description -> {
            val descriptionText = new Text(description);
            descriptionText.setShadow(true)
                    .setOrigin(Relative.RIGHT)
                    .setAlign(Relative.RIGHT)
                    /*.setScale(new V3(.9, .9))*/;

            descriptionBox.addChild(descriptionText);
        });

        this.addChild(titleText, descriptionBox).setInteractive(true);
    }

}
