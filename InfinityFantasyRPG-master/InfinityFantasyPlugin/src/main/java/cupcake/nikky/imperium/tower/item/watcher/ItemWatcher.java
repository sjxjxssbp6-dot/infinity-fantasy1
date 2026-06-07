package cupcake.nikky.imperium.tower.item.watcher;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectRegistry;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import cupcake.nikky.imperium.tower.item.watcher.serializer.WatcherSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record ItemWatcher<T>(@NotNull String key,
                             @NotNull Function<ImperiumItem, T> dataFetcher,
                             @NotNull BiConsumer<ImperiumItem, T> dataSetter,
                             @NotNull WatcherSerializer<T> watcherSerializer) {

    public void watch(@NotNull final ImperiumItem item, @NotNull final String serialized) {
        dataSetter().accept(item, watcherSerializer().getDeserializer().apply(serialized));
    }

    public String unwatch(@NotNull final ImperiumItem item) {
        return watcherSerializer().getSerializer().apply(dataFetcher().apply(item));
    }

    public static final ItemWatcher<Integer> LEVEL = new ItemWatcher<>("item_level",
            item -> item.getGeneralSettings().itemLevel(),
            (item, itemLevel) -> item.getGeneralSettings().itemLevel(itemLevel),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);
    public static final ItemWatcher<String> DISPLAY_NAME = new ItemWatcher<>("display_name",
            ImperiumItem::getName,
            ImperiumItem::setName,
            WatcherSerializer.STRING_WATCHER_SERIALIZER);
    public static final ItemWatcher<Boolean> COPY_STATEMENT = new ItemWatcher<>("equip_copy",
            item -> item.getGearSettings().copy(),
            (item, copy) -> item.getGearSettings().copy(copy),
            WatcherSerializer.BOOLEAN_WATCHER_SERIALIZER);
    public static final ItemWatcher<ItemRare> RARITY = new ItemWatcher<>("rarity",
            ImperiumItem::getRare,
            ImperiumItem::setRare,
            new WatcherSerializer<>(Enum::name, ItemRare::valueOf));
    public static final ItemWatcher<Integer> REQUIRED_LEVEL = new ItemWatcher<>("required_level",
            item -> item.getGeneralSettings().requiredLevel(),
            (item, requiredLevel) -> item.getGeneralSettings().requiredLevel(requiredLevel),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);
    public static final ItemWatcher<AspectBase> ASPECT = new ItemWatcher<>("aspect",
            item -> item.getAspectSettings().aspect(),
            (item, aspect) -> item.getAspectSettings().aspect(aspect),
            new WatcherSerializer<>(
                    aspect -> Optional.ofNullable(aspect).map(aspect1 -> aspect1.getClass().getName()).orElse(""),
                    aspectClass -> aspectClass.isEmpty() ? null : AspectRegistry.getInstance().findAspect(aspectClass)
            ));
    public static final ItemWatcher<Integer> ASPECT_CORRUPT = new ItemWatcher<>("aspect_corrupt",
            item -> item.getAspectSettings().aspectCorrupt(),
            (item, aspectCorrupt) -> item.getAspectSettings().aspectCorrupt(aspectCorrupt),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);
    public static final ItemWatcher<Integer> ASPECT_EXALTED_VALUE = new ItemWatcher<>("aspect_exalted_value",
            item -> item.getAspectSettings().exaltedValue(),
            (item, aspectCorrupt) -> item.getAspectSettings().exaltedValue(aspectCorrupt),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);
    public static final ItemWatcher<Boolean> ASPECT_COPY_STATEMENT = new ItemWatcher<>("aspect_copy",
            item -> item.getAspectSettings().aspectCopy(),
            (item, aspectCopy) -> item.getAspectSettings().aspectCopy(aspectCopy),
            WatcherSerializer.BOOLEAN_WATCHER_SERIALIZER);
    public static final ItemWatcher<Boolean> DROPPABLE = new ItemWatcher<>("droppable",
            item -> item.getDropSettings().droppable(),
            (item, droppable) -> item.getDropSettings().droppable(droppable),
            WatcherSerializer.BOOLEAN_WATCHER_SERIALIZER);
    public static final ItemWatcher<Integer> CORRUPTED = new ItemWatcher<>("corrupted",
            item -> item.getGearSettings().corrupted(),
            (item, corrupted) -> item.getGearSettings().corrupted(corrupted),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);
    public static final ItemWatcher<Integer> TOOL_EXP = new ItemWatcher<>("tool_exp",
            item -> item.getToolSettings().exp(),
            (item, exp) -> item.getToolSettings().exp(exp),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);
    public static final ItemWatcher<List<SpecificModifier>> MODIFIERS = new ItemWatcher<>("modifiers",
            item -> item.getModifierSettings().findAllSpecificModifiers(),
            (item, specificModifiers) -> item.getModifierSettings().registerSpecificModifier(specificModifiers),
            WatcherSerializer.listSpecificModifierWatcherSerializer()
    );
    public static final ItemWatcher<Integer> GEAR_QUALITY = new ItemWatcher<>("gear_quality",
            item -> item.getGearSettings().quality(),
            (item, quality) -> item.getGearSettings().quality(quality),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);
    public static final ItemWatcher<List<ModifierTag>> GEAR_TAG_QUALITY = new ItemWatcher<>("gear_tag_quality",
            item -> item.getGearSettings().qualityTags(),
            (item, qualityTags) -> item.getGearSettings().addQualityTags(qualityTags),
            WatcherSerializer.listModifierTagWatcherSerializer());
    public static final ItemWatcher<Integer> GEAR_POTENTIAL = new ItemWatcher<>("gear_potential",
            item -> item.getGearSettings().potential(),
            (item, potential) -> item.getGearSettings().potential(potential),
            WatcherSerializer.INTEGER_WATCHER_SERIALIZER);

}