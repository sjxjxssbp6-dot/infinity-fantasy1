package cupcake.nikky.imperium.tower.hud.debug;

import cupcake.nikky.imperium.tower.client.protocol.packet.PacketService;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.debug.AttributeClient;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.debug.AttributeDebugPacket;
import dev.xdark.clientapi.event.input.KeyPress;
import dev.xdark.clientapi.gui.Screen;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Color;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AttributeDebugScreen extends GuiScreen {

    private final List<AttributeClient> attributeClients = new ArrayList<>();
    private final Input search = new Input();
    private String searchFilter = "";

    public AttributeDebugScreen(final boolean debug) {
        if (!debug) return;

        this.setColor(new Color(0.0, 0.0, 0.0, 0));
        val rectangle = new CarvedRectangle(4).setColor(Palette.BLACK.alpha(.62)).setSize(new V3(800, 800)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        val scroll = new VerticalScrollView<>(VerticalLayout.class).setSize(new V3(792, 792)).setColor(Palette.BLACK.alpha(.62)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        rectangle.addChild(scroll.setDisableBarOnInactive(true).setPos(new V3(4, 4)));
        scroll.getLayout().setSpacing(8D);

        search.setSize(new V3(256, 38));
        search.setFocused(true);
        search.setFilter(Input.ADVANCED_FILTER);
        search.setPlaceHolder("Поиск");
        search.onTextChange((input, string) -> {
            this.searchFilter = string;
            this.update(scroll);
        });

        this.addChild(rectangle.addChild(search.setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.TOP_RIGHT).setPos(new V3(0, -8))));

        PacketService.getInstance().addListener(AttributeDebugPacket.class, attributeDebugPacket -> {
            if (Enginex.getCurrentScreen() != this) return;
            attributeClients.clear();
            attributeClients.addAll(attributeDebugPacket.getAttributes());

            this.update(scroll);
        });

        Enginex.registerEvent(KeyPress.class, event -> {
            if (event.getKey() == Keyboard.KEY_EQUALS) {
                this.open();
            }
        }, 1);
    }

    private void update(@NotNull final VerticalScrollView<?> scroll) {
        scroll.clearChilds();
        this.attributeClients.stream()
                .filter(attributeClient -> searchFilter.isEmpty() || attributeClient.getName().toLowerCase().contains(searchFilter.toLowerCase()))
                .sorted(Comparator.comparingInt(client -> client.getName().charAt(0)))
                .forEach(attributeClient -> {
                    scroll.addChild(new Text().setValue(attributeClient.getName() +
                            String.format(" (BASE %.1f || ADD %.1f || INCR %.1f || MORE %.1f)",
                                    attributeClient.getBase(), attributeClient.getAdditional(), attributeClient.getIncrease(), attributeClient.getMore()
                            )));
                });
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (search.isFocused()) {
            if (code == Keyboard.KEY_ESCAPE) {
                search.setFocused(false);
            } else {
                search.keyPress(code);
            }
        } else {
            if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode()) {
                this.close();
            }
        }
    }

}
