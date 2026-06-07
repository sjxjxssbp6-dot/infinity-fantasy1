package cupcake.nikky.imperium.tower.hud.prompt.cursor;

import cupcake.nikky.imperium.tower.data.prompt.cursor.CursorPromptStorage;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.VerticalLayout;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class CursorPromptContext extends VerticalLayout {

    private long last = Instant.now().toEpochMilli();

    public CursorPromptContext() {
        this.initialize();
        this.update();
    }

    private void initialize() {
        this.setSpacing(5D).setOrigin(Relative.TOP_LEFT).setAlign(Relative.CENTER).setPos(new V3(14, -8));
    }

    public void update() {
        if (CursorPromptStorage.getInstance().isEmpty()) return;

        val current = last + 200L - Instant.now().toEpochMilli();
        val allPrompts = CursorPromptStorage.getInstance().findAll();

        for (int i = 0; i < allPrompts.size(); i++) {
            val cursorPromptData = allPrompts.get(i);
            Enginex.getClientApi().syncExecutorService().schedule(() ->
                            this.addChild(0, new LinePrompt(cursorPromptData, this::removeChild).initialize()),
                    current > 0 ? current + i * 200L : i * 200L,
                    TimeUnit.MILLISECONDS);
            CursorPromptStorage.getInstance().invalidate(cursorPromptData.getUuid());
        }
    }

}
