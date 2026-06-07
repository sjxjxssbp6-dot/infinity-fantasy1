package cupcake.nikky.imperium.tower.handler.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;

@Accessors(fluent = true)
@Getter @Setter
@AllArgsConstructor
public class KeyContainer {

    private int defaultKey;
    private String category, description;

    public static KeyContainer of(final int defaultKey, @NotNull final String category, @NotNull final String description) {
        return new KeyContainer(defaultKey, category, description);
    }

    public String keyChar() {
        return Keyboard.getKeyName(this.key());
    }

    public int key() {
        val found = Enginex.getClientApi().settingsManager().getBindingByName(description);
        return found == null ? defaultKey : found.getKeyCode();
    }

    public String niceKeyChar() {
        return String.format("[%s]", this.keyChar());
    }

}
