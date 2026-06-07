package cupcake.nikky.imperium.tower.tooltip;

import com.google.common.collect.Lists;
import cupcake.nikky.imperium.tower.data.UserMetadata;
import cupcake.nikky.imperium.tower.tooltip.position.TooltipPosition;
import cupcake.nikky.imperium.tower.utility.ItemUtility;
import cupcake.nikky.imperium.tower.utility.NumberUtility;
import dev.xdark.clientapi.event.render.ItemTooltipRender;
import dev.xdark.clientapi.event.render.TooltipRender;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.opengl.GlStateManager;
import dev.xdark.clientapi.render.BufferBuilder;
import dev.xdark.clientapi.render.DefaultVertexFormats;
import dev.xdark.clientapi.render.Tessellator;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.renderer.SimpleDrawer;
import ru.cristalix.enginex.renderer.contexts.PostOverlay;
import ru.cristalix.enginex.util.Color;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.Collection;
import java.util.Comparator;

public class FantasyTooltip {

    private final TooltipPosition tooltipPosition;

    @Getter private final Mask panel;
    private final VerticalLayout layout;
    private final Text text;
    private final Text titleText;
    private final AbstractElement line;
    private Color cornersColor;
    private Image[] corners;

    @Getter private boolean isOverflow = false;

    public FantasyTooltip(@NotNull final TooltipPosition tooltipPosition) {
        this.tooltipPosition = tooltipPosition;

        panel = new Mask();
        panel.setOutline(2)
                .setCarveSize(4)
                .setSize(new V3(26, 26, 0))
                .setColor(Palette.GREY_DARK)
                .setEnabled(false);
        PostOverlay.get().addChild(panel);

        initCorners();

        layout = new VerticalLayout(8);
        layout.setPos(new V3(18, 18));
        panel.addChild(layout);

        titleText = new Text()
                .setShadow(true)
                .setLineHeight(24);
        layout.addChild(titleText);

        line = new AbstractElement() {
            @Override
            public void render(double partialTicks, double mouseX, double mouseY) {
                color.apply();
                GlStateManager.disableTexture2D();
                GlStateManager.glLineWidth(3);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);

                Tessellator tessellator = Enginex.getClientApi().tessellator();
                BufferBuilder builder = tessellator.getBufferBuilder();

                builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
                builder.pos(0, 0, 0).endVertex();
                builder.pos(layout.getSize().getX(), 0, 0).endVertex();
                tessellator.draw();

                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GlStateManager.enableTexture2D();
                Palette.WHITE.getColor().apply();
            }
        }.setColor(Palette.GREY_MIDDLE)
                .setSize(new V3(1, 1));
        layout.addChild(line);

        text = new Text().setShadow(true).setLineHeight(24);
        layout.addChild(text);

        panel.onPreRender(element -> {
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
        });
        panel.onPostRender(element -> {
            GlStateManager.enableDepth();
            element.setEnabled(false);
        });
    }

    public FantasyTooltip enableEvents() {
        Enginex.registerEvent(ItemTooltipRender.class, event -> {
            val contentCount = ItemUtility.handleItemTooltipEvent(event);
            event.setCancelled(true);

            val content = event.getContent();
            String title = "";
            if (!content.isEmpty()) title = content.remove(0);

            val requiredLevel = getRequiredLevel(event.getStack());
            if (requiredLevel > 1) {
                val color = requiredLevel > UserMetadata.getInstance().getLevel() ? "§c" : "§a";
                for (int i = 0; i < content.size(); i++) {
                    val contentString = content.get(i);
                    if (contentString.contains("Необходимый уровень")) {
                        content.set(i, contentString.replace("§3", color));
                        break;
                    }
                }
            }

            val price = getPrice(event.getStack());
            if (price != null) content.add(content.size() - contentCount, price);

            render(ItemUtility.findColorByItemRarity(event.getStack()), title, content);
        }, 1);
        Enginex.registerEvent(TooltipRender.class, event -> {
            event.setCancelled(true);
            String title = "";
            if (!event.getContent().isEmpty()) title = event.getContent().remove(0);
            render(ItemUtility.findColorByItemRarity(title), title, event.getContent());
        });

        return this;
    }

    private int getRequiredLevel(ItemStack item) {
        val tags = item.getTagCompound();

        if (tags != null && !tags.isEmpty()) {
            val requiredLevelString = tags.getString("required_level");
            val value = requiredLevelString.isEmpty() ? 0 : NumberUtility.parseInteger(requiredLevelString);

            if (value > 1) return value;
        }

        return 1;
    }

    private String getPrice(ItemStack item) {
        val isAlt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        val tags = item.getTagCompound();

        if (isAlt && tags != null && !tags.isEmpty()) {
            val price = tags.getInteger("price");

            if (price > 0) return (price * item.getCount()) + "\uE0FE";
        }

        return null;
    }

    private String repeat(String with, int amount) {
        if (amount == 0) return "";
        return new String(new char[amount]).replace("\0", with);
    }

    private void initCorners() {
        Image topLeftCorner = new Image("enginex:textures/corner");
        topLeftCorner.setAlign(Relative.TOP_LEFT)
                .setPos(new V3(0, 64))
                .setSize(new V3(64, 64))
                .setRotation(new V3(0, 0, 270))
                .setColor(cornersColor)
                .setEnabled(false);
        panel.addChild(topLeftCorner);

        Image topRightCorner = new Image("enginex:textures/corner");
        topRightCorner.setAlign(Relative.TOP_RIGHT)
                .setPos(new V3(-64, 0))
                .setSize(new V3(64, 64))
                .setColor(cornersColor)
                .setEnabled(false);
        panel.addChild(topRightCorner);

        Image bottomLeftCorner = new Image("enginex:textures/corner");
        bottomLeftCorner.setAlign(Relative.BOTTOM_LEFT)
                .setPos(new V3(64, 0))
                .setSize(new V3(64, 64))
                .setRotation(new V3(0, 0, 180))
                .setColor(cornersColor)
                .setEnabled(false);
        panel.addChild(bottomLeftCorner);

        Image bottomRightCorner = new Image("enginex:textures/corner");
        bottomRightCorner.setAlign(Relative.BOTTOM_RIGHT)
                .setPos(new V3(0, -64))
                .setSize(new V3(64, 64))
                .setRotation(new V3(0, 0, 90))
                .setColor(cornersColor)
                .setEnabled(false);
        panel.addChild(bottomRightCorner);

        corners = new Image[]{topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner};
    }

    public void render(String line) {
        render(null, "", line);
    }

    public void render(Color cornersColor, String title, String line) {
        render(cornersColor, title, line.split("\n"));
    }

    public void render(String... lines) {
        render(null, "", lines);
    }

    public void render(Color cornersColor, String title, String... lines) {
        if (lines.length == 0 && title.isEmpty()) return;
        render(cornersColor, title, Lists.newArrayList(lines));
    }

    public void render(Collection<String> lines) {
        render(null, "", lines);
    }

    public void render(Color cornersColor, String title, Collection<String> lines) {
        if (lines.isEmpty() && title.isEmpty()) return;
        doRender(cornersColor, title, lines.toArray(new String[0]));
    }

    private void doRender(Color cornersColor, String title, String... lines) {
        if (!title.isEmpty())
            titleText.setValue(title).setEnabled(true);
        else titleText.setEnabled(false);

        if(lines.length > 0)
            text.setValue(lines).setEnabled(true);
        else text.setEnabled(false);

        if(lines.length > 0 && !title.isEmpty() && cornersColor != null)
            line.setColor(cornersColor).setEnabled(true);
        else line.setEnabled(false);

        this.cornersColor = cornersColor;
        boolean hasCorners = cornersColor != null;

        double inset = hasCorners ? 18 : 10;

        layout.setPos(new V3(inset, inset));
        panel.setSize(layout.getSize().clone().add(inset * 2, inset * 2, 0));
        panel.setOutline(hasCorners ? 0 : 2);

        for (Image corner : corners)
            corner.setColor(cornersColor).setEnabled(hasCorners);

        V3 mouse = SimpleDrawer.getMousePos();

        AbstractElement overlay = PostOverlay.get();

        GuiScreen guiScreen = Enginex.getCurrentScreen();
        if (guiScreen != null)
            panel.setScale(guiScreen.getScale().clone().multiply(2));
        else
            panel.setScale(new V3(1, 1, 1));

        panel.setPos(tooltipPosition.getPosition(mouse, panel));

        V3 pos = panel.getPos();
        V3 size = panel.getSize().clone().multiply(panel.getScale());

        if (!tooltipPosition.canTouchPosition(mouse, panel)) {
            panel.setEnabled(false);
            return;
        }

        if (tooltipPosition.canOverflow()) {
            if (pos.getX() + size.getX() + 8 > overlay.getSize().getX()) {
                pos.subtract(size.getX() + 48 * panel.getScale().getX(), 0, 0);
                isOverflow = true;
            } else isOverflow = false;

            V3 maxPos = overlay.getSize().clone().subtract(size).subtract(8, 8, 0);
            pos.setX(Math.max(Math.min(pos.getX(), maxPos.getX()), 8)).setY(Math.max(Math.min(pos.getY(), maxPos.getY()), 8));
        }

        panel.setEnabled(true);
    }

}
