package cupcake.nikky.imperium.tower.item;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.entity.monster.statistic.MonsterStatistic;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseRegistry;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;
import cupcake.nikky.imperium.tower.item.modifier.process.*;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionContainer;
import cupcake.nikky.imperium.tower.item.generator.ItemGenerator;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionProvider;
import cupcake.nikky.imperium.tower.item.settings.exact.*;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorSettings;
import cupcake.nikky.imperium.tower.item.settings.exact.loot.LootSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcherRegistry;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

@Getter
public abstract class DefaultImperiumItem implements ImperiumItem {

    @Setter private ItemRare rare;
    private final ItemType imperiumType;
    private final Material material;
    @Setter private short durability;
    @Setter private String name;
    private final List<String> additionalDescription;
    private final ItemWatcherRegistry itemWatcherRegistry = new ItemWatcherRegistry(this);

    private static final Map<Class<? extends ItemSettings>, Function<ImperiumItem, ItemSettings>> settingsCreators = new HashMap<>();
    private final Map<Class<? extends ItemSettings>, ItemSettings> settings = new HashMap<>();

    private final AttributeMap attributes = new AttributeMap(new EnumMap<>(ImperiumAttribute.class));

    private final List<String> description = new ArrayList<>();

    protected ItemGenerator itemGenerator;

    public DefaultImperiumItem(ItemRare rare, ItemType imperiumType, Material material, short durability, String name, List<String> additionalDescription) {
        this.rare = rare;
        this.imperiumType = imperiumType;
        this.material = material;
        this.durability = durability;
        this.name = name;
        this.additionalDescription = additionalDescription;

        if (settingsCreators.isEmpty()) {
            settingsCreators.put(CursorSettings.class, CursorSettings::new);
            settingsCreators.put(AspectSettings.class, AspectSettings::new);
            settingsCreators.put(DamageSettings.class, DamageSettings::new);
            settingsCreators.put(DropSettings.class, DropSettings::new);
            settingsCreators.put(GearSettings.class, GearSettings::new);
            settingsCreators.put(GeneralSettings.class, GeneralSettings::new);
            settingsCreators.put(InteractSettings.class, InteractSettings::new);
            settingsCreators.put(LootSettings.class, LootSettings::new);
            settingsCreators.put(ModifierSettings.class, ModifierSettings::new);
            settingsCreators.put(ToolSettings.class, ToolSettings::new);
            settingsCreators.put(FragmentSettings.class, FragmentSettings::new);
            settingsCreators.put(DescriptionSettings.class, DescriptionSettings::new);
        }

        this.findSettings(AspectSettings.class); // pre init because of async use

        this.preInitialize();
        itemGenerator = new ItemGenerator(this);
    }

    protected void preInitialize() {

    }

    public ImperiumItem originalStack(@NotNull final ItemStack itemStack) {
        this.getItemWatcherRegistry().registerOriginalStack(itemStack);
        this.updateAttributes();

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ItemSettings> T findSettings(Class<? extends T> clazz) {
        return (T) this.settings.computeIfAbsent(clazz, clazz2 -> settingsCreators.get(clazz2).apply(this));
    }

    public ItemRare getRare() {
        this.getItemWatcherRegistry().watch(ItemWatcher.RARITY);
        return rare;
    }

    public String getName() {
        this.getItemWatcherRegistry().watch(ItemWatcher.DISPLAY_NAME);
        return name;
    }

    @Override
    public List<String> generateDescription() {
        val container = new DescriptionContainer(this);
        DescriptionProvider.getInstance().process(container);
        return container.getLore();
    }

    @Override
    public DescriptionContainer processDescriptionContainer() {
        val container = new DescriptionContainer(this);
        DescriptionProvider.getInstance().process(container);
        return container;
    }

    @Override
    public void updateAttributes() {
        getAttributes().clear();

        if (this.getImperiumType() == ItemType.EDGE_FRAGMENT) {
            findSettings(FragmentSettings.class).setFragmentItemQuantity(getGearSettings().corrupted());
            findSettings(FragmentSettings.class).setFragmentExperienceIncrease(getGearSettings().corrupted());
        }

        if (this.getImperiumType() == ItemType.SHARD_BREAKTHROUGH) {
            var levelDifference = (MonsterStatistic.getInstance().findHigherMonsterLevel() - LevelStorage.getInstance().getBreakThroughLevel()) * 2D;
            var multiplier = (getGeneralSettings().itemLevel() - LevelStorage.getInstance().getBreakThroughLevel() + 1) / levelDifference;
            findSettings(FragmentSettings.class).setFragmentItemQuantity((int) Math.round(getGearSettings().corrupted() * multiplier));
            findSettings(FragmentSettings.class).setFragmentExperienceIncrease((int) Math.round(getGearSettings().corrupted() * multiplier));
        }

        var maximumRequiredLevelFromModifier = 0;

        for (SpecificModifier specificModifier : getModifierSettings().findAllSpecificModifiers()) {
            if (specificModifier.getModifier() == null) {
                var possible = getModifierSettings().findPossibleById(specificModifier.getModifierId());
                if (possible == null)
                    possible = ModifierBaseRegistry.findById(specificModifier.getModifierId());

                specificModifier.setModifier(possible);

                if (possible == null) {
                    getModifierSettings().removeSpecificModifier(specificModifier.getModifierId());
                    continue;
                }
            }

            specificModifier.applyProcess(this);

            var tier = specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier());
            if (tier != null)
                maximumRequiredLevelFromModifier = Math.max(maximumRequiredLevelFromModifier, tier.requireBaseLevel());

            if (this.getImperiumType() == ItemType.EDGE_FRAGMENT) {
                if (tier == null) continue;

                val fragmentTier = tier.getFragmentTierIfPresent();
                if (fragmentTier == null) continue;

                val fragmentSettings = this.findSettings(FragmentSettings.class);
                fragmentSettings.setFragmentItemQuantity(fragmentSettings.getFragmentItemQuantity() + fragmentTier.dropQuantity());
                if (fragmentTier.min() != fragmentTier.max())
                    fragmentSettings.setFragmentItemQuantity(
                            fragmentSettings.getFragmentItemQuantity() + (int) Math.round(specificModifier.getKey() / fragmentTier.max() * 5)
                    );

                fragmentSettings.setFragmentExperienceIncrease(
                        fragmentSettings.getFragmentExperienceIncrease() + fragmentSettings.getFragmentItemQuantity() / 5
                );
            }

            if (getGearSettings().corrupted() != 0 && (specificModifier.getModifier().getProcess() instanceof AttributeBaseModifierProcess ||
                    specificModifier.getModifier() == ModifierItemListExtended.ADD_FIRE_DAMAGE || specificModifier.getModifier() == ModifierItemListExtended.ADD_COLD_DAMAGE ||
                    specificModifier.getModifier() == ModifierItemListExtended.ADD_LIGHTNING_DAMAGE || specificModifier.getModifier() == ModifierItemListExtended.ADD_CHAOS_DAMAGE)) {

                specificModifier.getModifier().getProcess().getAttributes().forEach(attribute -> {
                    var corrupted = getGearSettings().corrupted() * 0.040;
                    val baseValue = Math.abs(this.getAttributes().find(attribute).getBaseValue());

                    if (attribute == ImperiumAttribute.ATTACK_SPEED) {
                        return;
                    }

                    this.getAttributes().find(attribute).applyAdditional(baseValue * corrupted);
                });
            }
        }

        if (getRare() != ItemRare.UNIQUE && maximumRequiredLevelFromModifier > getGeneralSettings().requiredLevel())
            getGeneralSettings().requiredLevel(maximumRequiredLevelFromModifier);
    }

    @Override
    public ItemStack update(@NotNull final Player player) {
        this.updateAttributes();
        return this.createNew();
    }

    public boolean isUnique() {
        return itemGenerator.isUnique();
    }

    @Override
    public ItemStack createNew() {
        return this.createNew(1);
    }

    @Override
    public ItemStack createNew(final int amount) {
        return this.createNew(amount, null);
    }

    @Override
    public ItemStack createNew(Player player) {
        return this.createNew(1, player);
    }

    @Override
    public ItemStack createNew(final int amount, Player player) {
        return this.getItemGenerator().createItemStack(amount, player);
    }

    @Override
    public String getUniqueId() {
        return getObject().getUniqueId();
    }
}
