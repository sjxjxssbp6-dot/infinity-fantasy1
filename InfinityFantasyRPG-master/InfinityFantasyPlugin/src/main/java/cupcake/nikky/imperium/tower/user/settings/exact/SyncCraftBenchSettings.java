package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.*;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.modifier.*;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import me.anfanik.steda.api.utility.ItemUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SyncCraftBenchSettings extends BaseUserSettings {

    private int currentSlotOpened = -1;
    private ImperiumItem currentItem;

    private Map<String, ModifierList> currentModifiers;
    private Map<String, Modifier> currentServerModifiers;
    private Map<String, Integer> currentServerModifierTiers;

    public SyncCraftBenchSettings(User user) {
        super(user);
    }

    private boolean click(final int slot) {
        var isClickSelected = slot == currentSlotOpened;
        this.reset();

        var player = getPlayer();
        var item = player.getInventory().getItem(slot);

        if (ItemUtility.isNull(item)) return false;

        currentSlotOpened = isClickSelected ? -1 : slot;
        currentItem = Fantasy.itemProvider().find(item);
        return this.canModify(currentItem);
    }

    private boolean canModify(@Nullable final ImperiumItem item) {
        if (item == null) return false;
        if (item.getImperiumType() != ItemType.ARMOR && item.getImperiumType() != ItemType.WEAPON) return false;
        if (item.getRare() == ItemRare.UNIQUE || item.getRare() == ItemRare.COMMON) return false;

        return true;
    }

    public void reset() {
        this.currentSlotOpened = -1;
        this.currentItem = null;
        currentServerModifiers = null;
        currentModifiers = null;
        currentServerModifierTiers = null;
    }

    public void openScreen() {
        this.reset();
        getUser().sendPacket(new CraftBenchOpen());
    }

    public void clickScreen(final int slot) {
        var lastSlot = this.currentSlotOpened;
        var result = this.click(slot);
        if (!result) {
            Fantasy.clientProvider().notification(getPlayer(), SystemNotificationType.ERROR, "Данный предмет нельзя модифицировать");
            return;
        }

        if (lastSlot == slot) {
            getUser().sendPacket(new CraftBenchSlotClick(slot));
            getUser().sendPacket(new CraftBenchModifierList());
            return;
        }

        var modifierSettings = currentItem.getModifierSettings();
        var craftedModifiers = modifierSettings.findSpecificByType(ModifierType.CRAFTED);
        var specificModifiersPrefix = modifierSettings.findSpecificByPlace(ModifierPlace.PREFIX).size() - craftedModifiers.stream().filter(specificModifier -> specificModifier.getModifier().getPlace() == ModifierPlace.PREFIX).count();
        var specificModifiersSuffix = modifierSettings.findSpecificByPlace(ModifierPlace.SUFFIX).size() - craftedModifiers.stream().filter(specificModifier -> specificModifier.getModifier().getPlace() == ModifierPlace.SUFFIX).count();

        currentModifiers = new HashMap<>();
        final Map<String, ModifierList> currentPrefixModifiers = new HashMap<>();
        final Map<String, ModifierList> currentSuffixModifiers = new HashMap<>();
        currentServerModifiers = new HashMap<>();
        currentServerModifierTiers = new HashMap<>();

        modifierSettings.findPossibleByPlace(ModifierPlace.PREFIX, ModifierPlace.SUFFIX).forEach(modifier -> {
            final List<ModifierData> modifierData = new ArrayList<>();

            var rangeSize = modifier.getRange().sizeWithoutExalted();
            var range = rangeSize > 3 ? modifier.getRange().copyWithTiers(rangeSize - 2) : modifier.getRange();
            var duplicatedModifier = modifierSettings.findSpecificById(modifier.getId());
            var isDuplicate = duplicatedModifier != null && duplicatedModifier.getModifierType() != ModifierType.CRAFTED;

            range.getTiers().stream().filter(ModifierTier::canGetViaCraft).filter(modifierTier -> !modifierTier.exalted()).forEach(modifierTier -> {
                var subTitle = modifier.getModifierDescription().generateSubCraftDescription("&6", "&f", modifierTier);
                var currentModifierData = new ModifierData();
                currentModifierData.setPrice(modifierTier.tier() * 750);
                currentModifierData.setCraftTitle(subTitle);

                var isAccessedByLevel = currentItem.getGeneralSettings().itemLevel() >= modifierTier.requireBaseLevel();

                var size = currentItem.getRare().getModifierSize();
                var isAccessedBySlot = modifier.getPlace() == ModifierPlace.PREFIX ? specificModifiersPrefix < size : specificModifiersSuffix < size;

                if (!isAccessedByLevel) {
                    currentModifierData.setAccessCode(2);
                } else if (!isAccessedBySlot) {
                    currentModifierData.setAccessCode(1);
                } else if (isDuplicate) {
                    currentModifierData.setAccessCode(3);
                }

                currentServerModifierTiers.put(subTitle, modifierTier.tier());
                modifierData.add(currentModifierData);
            });

            if (modifierData.isEmpty()) return;

            var title = modifier.getModifierDescription().generateCraftDescription("&6", "&f");
            var list = new ModifierList(modifierData);

            currentServerModifiers.put(title, modifier);
            currentModifiers.put(title, list);
            if (modifier.getPlace() == ModifierPlace.PREFIX) currentPrefixModifiers.put(title, list);
            else currentSuffixModifiers.put(title, list);
        });

        getUser().sendPacket(new CraftBenchSlotClick(slot));
        getUser().sendPacket(new CraftBenchModifierList(currentPrefixModifiers, currentSuffixModifiers));
    }

    public void tryCraft(@NotNull final String title, @NotNull final String subTitle) {
        if (currentSlotOpened == -1 || currentItem == null || currentModifiers == null || currentModifiers.isEmpty() || currentServerModifiers == null ||
                currentServerModifierTiers == null || currentServerModifiers.isEmpty() || currentServerModifierTiers.isEmpty()) {
            Fantasy.clientProvider().notification(getPlayer(), SystemNotificationType.ERROR, "Операция невозможна", "Попробуйте позже");
            return;
        }

        var modifierList = currentModifiers.get(title);
        if (modifierList == null) {
            Fantasy.clientProvider().notification(getPlayer(), SystemNotificationType.ERROR, "Модификатора не сущесвует", "Попробуйте позже");
            return;
        }

        var modifierData = modifierList.getData().stream().filter(modifierData1 -> modifierData1.getCraftTitle().equals(subTitle)).findAny().orElse(null);
        if (modifierData == null) {
            Fantasy.clientProvider().notification(getPlayer(), SystemNotificationType.ERROR, "Модификатора не сущесвует", "Попробуйте позже");
            return;
        }

        var access = modifierData.getAccessCode();
        if (access != 0) {
            Fantasy.clientProvider().notification(getPlayer(), SystemNotificationType.ERROR, modifierData.getAccessWarnText());
            return;
        }

        var price = modifierData.getPrice();
        if (!getUser().getCurrencyHandler().withdrawCurrency(CurrencyType.GRAN, price)) {
            Fantasy.clientProvider().notification(getPlayer(), SystemNotificationType.ERROR, "Недостаточно средств");
            return;
        }

        currentItem.getModifierSettings().removeSpecificModifierByType(ModifierType.CRAFTED);
        ModifierProvider.getInstance().applyCraftModifier(currentItem, currentServerModifiers.get(title), currentServerModifierTiers.get(subTitle));
        getPlayer().getInventory().setItem(currentSlotOpened, NbtUtility.clearSessionTags(currentItem.createNew()));
        Fantasy.bukkitProvider().sound(getPlayer(), currentItem.getImperiumType() == ItemType.ARMOR ? AdvancedSounds.CLOTH : AdvancedSounds.WEAPON);

        getUser().sendPacket(new CraftBenchTryCraft(title, subTitle, true));
    }

}
