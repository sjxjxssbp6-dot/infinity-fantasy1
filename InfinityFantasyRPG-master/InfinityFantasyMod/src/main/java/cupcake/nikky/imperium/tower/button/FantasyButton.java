package cupcake.nikky.imperium.tower.button;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.event.FantasyItemTooltipRender;
import cupcake.nikky.imperium.tower.event.FantasyTooltipRender;
import dev.xdark.clientapi.event.render.ItemTooltipRender;
import dev.xdark.clientapi.event.render.TooltipRender;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.item.TooltipFlag;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.element.AbstractElement;
import ru.cristalix.enginex.element.Button;
import ru.cristalix.enginex.util.SoundUtil;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FantasyButton extends Button {

    private int button = 1;
    @Getter @Accessors(chain = true)
    private boolean canClick = true, lazyCanClick = true;
    @Getter @Setter @Accessors(chain = true)
    private boolean confusionSound;
    @Getter @Setter
    private ItemStack itemTooltip;

    public FantasyButton() {
        this("");
    }

    public FantasyButton(@NotNull final ItemStack itemTooltip) {
        this("");
        this.itemTooltip = itemTooltip;
    }

    public FantasyButton(String content) {
        super(content);

        if (onLeftClickListeners != null) onLeftClickListeners.clear();

        this.onLeftClick(element -> {
            if (!isCanClick()) return;

            String path = button == 1 ? "sounds/custom/click_1.ogg" : "sounds/custom/click_2.ogg";
            button = -button;

            if (confusionSound) path = "sounds/custom/confusion.ogg";

            if (getPlaySound()) SoundUtil.play(path, 1.0F, 1.0F);
            this.smoothChangeColor(this.activeColor, 0.07);
            Animation.play(element, "interactive", 0.07, (e, progress) -> {}).onComplete(element1 -> this.updateColor());

            this.canClick = lazyCanClick;
        });
    }

    public void processLeftClick() {
        if (onLeftClickListeners != null) onLeftClickListeners.forEach(handler -> handler.accept(this));
    }

    public FantasyButton setCanClick(boolean canClick) {
        this.lazyCanClick = canClick;
        return this;
    }

    public FantasyButton setForceCanClick(boolean canClick) {
        this.canClick = canClick;
        return this;
    }

    @Override
    public void onLeftClick(Consumer<Button> consumer) {
        if (onLeftClickListeners == null) onLeftClickListeners = new ArrayList<>();

        onLeftClickListeners.add(0, consumer);
        setInteractive(true);
    }

    @Override
    public boolean mouseClick(int mouse) {
        if (!isCanClick()) return false;
        return super.mouseClick(mouse);
    }

    @Override
    protected void preRender(double partialTicks, double mouseX, double mouseY) {
        if (this.preRenderListeners != null) {
            this.preRenderListeners.forEach(imageConsumer -> imageConsumer.accept(this.genericThis));
        }

        if (itemTooltip != null && this.isHover()) {
            val event = ItemTooltipRender.BUS.fire(new FantasyItemTooltipRender(itemTooltip, itemTooltip.getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL)));
            if (!event.isCancelled()) {
                FantasyMod.customTooltip().render(event.getContent());
            }
        } else if (this.tooltipData != null && this.isHover()) {
            val event = TooltipRender.BUS.fire(new FantasyTooltipRender(new ArrayList<>(this.tooltipData.getLines())));
            if (!event.isCancelled()) {
                FantasyMod.customTooltip().render(event.getContent());
            }
        }
    }

}
