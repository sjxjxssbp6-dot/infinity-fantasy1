package cupcake.nikky.imperium.tower.hud.box;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.CaseChangeAmountPacket;
import cupcake.nikky.imperium.tower.data.UserMetadata;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.Switcher;

public class CaseAmountContext extends Switcher {

    private final int[] amount = new int[] {1, 2, 5};
    @Setter private int currentIndex;

    public CaseAmountContext(@NotNull final FantasyMod fantasyMod) {
        super(" X1 ", " X2 ", " X5 ");

        this.currentIndex = 0;

        this.onPreTick(switcher -> this.layout.getChilds().stream().filter(abstractElement -> abstractElement instanceof SwitcherElement).forEach(abstractElement -> {
            val switcherElement = (SwitcherElement) abstractElement;
            if (switcherElement.getText().getText().equals(" X1 ")) return;

            switcherElement.setActive(UserMetadata.getInstance().isSubscribed());
        }));

        this.onSelect((element, index) -> {
            if (index == currentIndex) return;

            this.setSelected(currentIndex);
            fantasyMod.sendPacket(new CaseChangeAmountPacket(amount[index]));
        });
    }

    public void setAmount(final int amount) {
        for (int i = 0; i < amount; i++) {
            if (this.amount[i] == amount) {
                this.setCurrentIndex(i);
                this.setSelected(i);
                return;
            }
        }
    }

    public int getCurrentAmount() {
        return amount[currentIndex];
    }

}
