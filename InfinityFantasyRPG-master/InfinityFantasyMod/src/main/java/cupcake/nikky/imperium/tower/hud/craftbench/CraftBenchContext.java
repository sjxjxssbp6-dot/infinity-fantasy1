package cupcake.nikky.imperium.tower.hud.craftbench;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchSlotClick;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchTryCraft;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.ModifierList;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.CraftBenchStorage;
import cupcake.nikky.imperium.tower.data.EconomyStorage;
import cupcake.nikky.imperium.tower.hud.economy.EconomyContext;
import cupcake.nikky.imperium.tower.hud.economy.EconomyDataType;
import cupcake.nikky.imperium.tower.inventory.InventoryContext;
import cupcake.nikky.imperium.tower.utility.NumberUtility;
import dev.xdark.clientapi.gui.Screen;
import dev.xdark.clientapi.item.Items;
import lombok.Getter;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.renderer.TooltipData;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.HashMap;
import java.util.Map;

public class CraftBenchContext extends GuiScreen {

    private int lastSlot = -1;
    private String selectedType;
    private String selectedSubtype;
    private CraftModifierType selectedModifierType = CraftModifierType.ALL;
    private AbstractElement currentSubList;
    private FantasyButton lastButton;
    private FantasyButton lastSubButton;

    @Getter
    private final InventoryContext inventoryContext;
    private final VerticalLayout modifiersLayout = new VerticalLayout(2D);
    private final EconomyContext economyContext = new EconomyContext();
    private final Switcher switcher = new Switcher("Префиксы", "Всё", "Суффиксы");

    private final FantasyMod mod;
    private final Text emptyModifiersText = new Text("Пусто").setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setEnabled(false);

    public CraftBenchContext(@NotNull final FantasyMod mod) {
        this.mod = mod;
        inventoryContext = new InventoryContext((slot, itemStack) -> {
            val item = itemStack.getItem();
            if (item == null || item == Items.AIR) return;

            mod.sendPacket(new CraftBenchSlotClick(slot));
        });

        this.initialize();
        this.updateList();
    }

    private void initialize() {
        economyContext.apply(EconomyDataType.GRAN);
        economyContext.onPreTick(element -> economyContext.update());

        var button = new FantasyButton("Модифицировать");

        button.setButtonColor(ButtonColor.BLUE).setActive(false).setSize(new V3(256, 32));
        switcher.setSelected(1).setColor(Palette.BLUE_MIDDLE);

        switcher.onSelect((element, integer) -> {
            if (this.selectedModifierType == CraftModifierType.PREFIX && integer == 0 || this.selectedModifierType == CraftModifierType.SUFFIX && integer == 2 ||
                    this.selectedModifierType == CraftModifierType.ALL && integer == 1) return;
            this.selectedModifierType = integer == 2 ? CraftModifierType.SUFFIX : integer == 0 ? CraftModifierType.PREFIX : CraftModifierType.ALL;
            this.updateList();
        });

        button.onPreTick(element -> {
            if (selectedType != null && selectedSubtype != null && lastSlot != -1) {
                button.setActive(true);
                val stack = Enginex.getMinecraft().getPlayer().getInventory().getStackInSlot(lastSlot);
                if (stack != null && stack.getItem() != Items.AIR) {
                    button.setItemTooltip(stack);
//                    var lore = ItemUtility.findAdvancedLore(stack);
//                    if (lore.isEmpty()) {
//                        button.setTooltip(String.join("\n", stack.getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL)));
//                    } else {
//                        lore.add(0, stack.getDisplayName());
//                        button.setTooltip(String.join("\n", lore));
//                    }
                } else button.setItemTooltip(null);
            } else {
                button.setActive(false);
                button.setTooltip((TooltipData) null);
            }
        });

        button.onLeftClick(element -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("craft-bench-click", 0.3)) return;
            if (selectedType == null || selectedSubtype == null) return;

            mod.sendPacket(new CraftBenchTryCraft(selectedType, selectedSubtype, false));
        });

        final VerticalScrollView<?> scroll = new VerticalScrollView<>().setDisableBarOnInactive(true);
        scroll.setColor(Palette.GREY);
        scroll.getBar().setPos(new V3(5, 0));
        this.addChild(new VerticalLayout(8D)
                .addChild(new CarvedRectangle(6D).setColor(Palette.GREY_DARK.alpha(.62)).setSize(new V3(492D + 32D, 500))
                                .addChild(emptyModifiersText, switcher.setOrigin(Relative.CENTER).setAlign(Relative.TOP), new VerticalLayout(16D)
                                        .addChild(scroll.setSize(new V3(480D + 16D, 402D))
                                                        .addChild(modifiersLayout.setOrigin(Relative.CENTER).setAlign(Relative.CENTER))
                                                        .setOrigin(Relative.CENTER).setAlign(Relative.CENTER),
                                                button.setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setPos(new V3(0, -8)))
                                                .setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setPos(new V3(0, 24))
                                        ).setOrigin(Relative.TOP).setAlign(Relative.TOP),
                        new VerticalLayout(2D).addChild(
                                new Rectangle().setSize(new V3(492D + 32D, 32D)).setColor(Palette.BLACK.alpha(0D))
                                        .addChild(economyContext.setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM_LEFT)),
                                inventoryContext).setOrigin(Relative.CENTER).setAlign(Relative.CENTER),
                        new Text("\uE3FE Выбрать предмет для модификации").setOrigin(Relative.CENTER).setAlign(Relative.CENTER))
                .setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setRenderLayer(1)
        );
    }

    public void updateList() {
        modifiersLayout.clearChilds();
        if (selectedModifierType == CraftModifierType.PREFIX) {
            this.fillPrefixModifiers();
        } else if (selectedModifierType == CraftModifierType.SUFFIX) {
            this.fillSuffixModifiers();
        } else if (selectedModifierType == CraftModifierType.ALL) {
            this.fillAllModifiers();
        }

        emptyModifiersText.setEnabled(modifiersLayout.getChilds().isEmpty());
    }

    public void click(final int slot) {
        inventoryContext.updateItemColor(slot, lastSlot == slot ? 0 : 1);
        if (lastSlot != -1) {
            inventoryContext.updateItemColor(lastSlot, 0);
        }
        if (lastSlot != -1 && lastSlot == slot)
            lastSlot = -1;
        else lastSlot = slot;
    }

    public void fillPrefixModifiers() {
        this.fillModifiers(CraftBenchStorage.getInstance().getPrefixes());
    }

    public void fillSuffixModifiers() {
        this.fillModifiers(CraftBenchStorage.getInstance().getSuffixes());
    }

    public void fillAllModifiers() {
        val data = new HashMap<>(CraftBenchStorage.getInstance().getPrefixes());
        data.putAll(CraftBenchStorage.getInstance().getSuffixes());
        this.fillModifiers(data);
    }

    private void fillModifiers(@NotNull final Map<String, ModifierList> modifiers) {
        modifiers.forEach((title, modifierList) -> {
            val layout = new VerticalLayout(0D);
            val craftTitle = this.cutTitle(title);
            val button = new FantasyButton(modifierList.getData().stream().anyMatch(modifierData -> modifierData.getAccessCode() == 0) ?
                    craftTitle : craftTitle.replace("f", "c"));

            button.setCarveSize(0D);
            button.getText().setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(4, 0));

            if (title.length() > 39)
                button.setTooltip(title);

            this.modifiersLayout.addChild(new VerticalLayout(1D).addChild(button.setButtonColor(ButtonColor.GREY).setSize(new V3(480 + 16D, 32)), layout));

            button.onLeftClick(element -> {
                if (!CooldownService.getInstance().getAndExtendCooldown("craft-bench-type-click", 0.3)) return;
                if (title.equals(selectedType)) {
                    if (lastButton != null)
                        lastButton.setButtonColor(ButtonColor.GREY);
                    if (lastSubButton != null)
                        lastSubButton.setButtonColor(ButtonColor.GREY);
                    selectedType = null;
                    currentSubList = null;
                    lastButton = null;
                    lastSubButton = null;
                    selectedSubtype = null;
                    layout.setEnabled(false);
                    button.setButtonColor(ButtonColor.GREY);
                } else {
                    if (currentSubList != null)
                        currentSubList.setEnabled(false);
                    if (lastButton != null)
                        lastButton.setButtonColor(ButtonColor.GREY);
                    if (lastSubButton != null)
                        lastSubButton.setButtonColor(ButtonColor.GREY);
                    selectedType = title;
                    selectedSubtype = null;
                    lastButton = button;
                    lastSubButton = null;
                    currentSubList = layout.setEnabled(true);
                    button.setColor(ButtonColor.GREY.getActiveColor());
                }
            });

            modifierList.getData().forEach(modifierData -> {
                val buttonTitle = modifierData.getAccessCode() == 0 ? modifierData.getCraftTitle() : modifierData.getCraftTitle().replace("f", "c");
                val subButton = new FantasyButton(this.cutTitle(buttonTitle));

                subButton.setCarveSize(0D);
                subButton.getText().setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(8, 0));

                if (modifierData.getAccessCode() != 0) {
                    subButton.setTooltip(modifierData.getCraftTitle() + "\n" + modifierData.getAccessWarnText());
                } else {
                    if (modifierData.getCraftTitle().length() > 39)
                        subButton.setTooltip(modifierData.getCraftTitle());
                    else subButton.setTooltip((TooltipData) null);

                    subButton.addChild(
                            new HorizontalLayout(2D)
                                    .addChild(new Text(String.valueOf(modifierData.getPrice())), new Image(FantasyImage.GRAN.toTexture()).setSize(new V3(16, 16)))
                                    .setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT).setPos(new V3(-8, 0)));
                    subButton.onLeftClick(element -> {
                        if (!CooldownService.getInstance().getAndExtendCooldown("craft-bench-type-click", 0.3)) return;
                        if (modifierData.getCraftTitle().equals(selectedSubtype)) {
                            if (lastSubButton != null)
                                lastSubButton.setButtonColor(ButtonColor.GREY);
                            this.selectedSubtype = null;
                            lastSubButton = null;
                            subButton.setButtonColor(ButtonColor.GREY);
                        } else {
                            if (lastSubButton != null)
                                lastSubButton.setButtonColor(ButtonColor.GREY);
                            this.lastSubButton = subButton;
                            this.selectedSubtype = modifierData.getCraftTitle();
                            subButton.setColor(ButtonColor.GREY.getActiveColor());
                        }
                    });
                }

                layout.addChild(subButton.setButtonColor(ButtonColor.GREY).setSize(new V3(480 + 16D, 28)));
            });

            layout.setEnabled(false);
        });
    }

    private String cutTitle(@NotNull final String title) {
        val length = title.length();
        if (length <= 39) return title;
        return title.substring(0, 39) + "...";
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode()) {
            this.close();
        }
    }

    @Override
    public void open() {
        super.open();
        inventoryContext.fillItems();
    }

    @Override
    public void close() {
        super.close();
        CraftBenchStorage.getInstance().reset();
        this.updateList();
        selectedModifierType = CraftModifierType.ALL;
        switcher.setSelected(1);
        inventoryContext.clearItems();
        inventoryContext.resetColors();
        lastSlot = -1;
        selectedType = null;
        selectedSubtype = null;
        lastButton = null;
        lastSubButton = null;
    }
}
