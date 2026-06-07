package cupcake.nikky.imperium.tower.handler;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.data.*;
import dev.xdark.clientapi.ClientApi;
import dev.xdark.clientapi.event.gui.ScreenDisplay;
import dev.xdark.clientapi.event.lifecycle.GameLoop;
import dev.xdark.clientapi.gui.ingame.ChatScreen;
import dev.xdark.clientapi.gui.ingame.InventorySurvivalScreen;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;

public class GameLoopEventHandler {

    public GameLoopEventHandler(@NotNull final ClientApi clientApi, @NotNull final FantasyMod mod) {
        Enginex.registerEvent(GameLoop.class, event -> {
            val buffSelection = mod.getBuffContext();
            val questColumn = mod.getQuestColumn();

            val tabHidden = !Enginex.getClientApi().settingsManager().getBindingByCode(15).isKeyDown();
            val debugInfoHidden = Enginex.getClientApi().settingsManager().getOptionValue("showDebugInfo").equals(Boolean.FALSE);
            val chatScreenHidden = !(Enginex.getScreen() instanceof ChatScreen);

            buffSelection.setEnabled(tabHidden && debugInfoHidden);
            questColumn.setEnabled(tabHidden && debugInfoHidden && chatScreenHidden);

            mod.getTargetBar().setEnabled(tabHidden && MonsterStorage.getInstance().isTarget());

            if (clientApi.minecraft().currentScreen() == null) {
                CraftViewStorage.getInstance().craftStack(null);
            }
        }, 1);

        Enginex.registerEvent(ScreenDisplay.class, event -> {
            if (event.getScreen() instanceof InventorySurvivalScreen) {
                mod.getNavigationContext().update(mod);
                mod.getBindInfoContext().update();
            }
        }, 1);
    }

}
