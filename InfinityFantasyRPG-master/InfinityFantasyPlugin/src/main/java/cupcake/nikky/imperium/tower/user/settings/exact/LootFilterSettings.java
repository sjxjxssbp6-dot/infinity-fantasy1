package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.filter.ItemFilterHandler;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
@Setter
public class LootFilterSettings extends SavableUserSettings {

    private ItemRare minimumRare = ItemRare.COMMON;
    private int minimumItemLevel = 1;
    private final Map<ItemObject, Integer> gearToIgnore = new HashMap<>();
    private final List<ItemObject> aspectsToIgnore = new ArrayList<>();
    private final List<ItemObject> itemsToIgnore = new ArrayList<>();
    private final List<ItemObject> divinationCardsToIgnore = new ArrayList<>();
    private final List<ItemObject> incubators = new ArrayList<>();
    private final List<ItemObject> masks = new ArrayList<>();

    private ItemFilterHandler itemFilterHandler;

    public LootFilterSettings(User user) {
        super(user);
    }

    public ItemFilterHandler getItemFilterHandler() {
        if (itemFilterHandler == null)
            this.itemFilterHandler = new ItemFilterHandler(this.getPlayer(), getUser());
        return itemFilterHandler;
    }

    public String serialize() {
        final Map<String, String> data = new HashMap<>();

        data.put("rare", minimumRare.name());
        data.put("itemLevel", String.valueOf(minimumItemLevel));
        data.put("gear", Fantasy.gson().toJson(gearToIgnore));
        data.put("aspectsToIgnore", Fantasy.gson().toJson(aspectsToIgnore));
        data.put("itemsToIgnore", Fantasy.gson().toJson(itemsToIgnore));
        data.put("divinationCardsToIgnore", Fantasy.gson().toJson(divinationCardsToIgnore));
        data.put("incubators", Fantasy.gson().toJson(incubators));
        data.put("masks", Fantasy.gson().toJson(masks));

        return Fantasy.gson().toJson(data, new TypeToken<Map<String, String>>(){}.getType());
    }

    public void deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.isEmpty()) return;

        final Map<String, String> data = Fantasy.gson().fromJson(serialized, new TypeToken<Map<String, String>>(){}.getType());

        final List<ItemObject> itemsToIgnore = Fantasy.gson().fromJson(data.get("itemsToIgnore"), new TypeToken<List<ItemObject>>(){}.getType());
        final List<ItemObject> aspectsToIgnore = Fantasy.gson().fromJson(data.get("aspectsToIgnore"), new TypeToken<List<ItemObject>>(){}.getType());
        final List<ItemObject> divinationCardsToIgnore = Fantasy.gson().fromJson(data.get("divinationCardsToIgnore"), new TypeToken<List<ItemObject>>(){}.getType());
        final List<ItemObject> incubators = Fantasy.gson().fromJson(data.get("incubators"), new TypeToken<List<ItemObject>>(){}.getType());
        final List<ItemObject> masks = Fantasy.gson().fromJson(data.get("masks"), new TypeToken<List<ItemObject>>(){}.getType());
        final Map<ItemObject, Integer> gear = Fantasy.gson().fromJson(data.get("gear"), new TypeToken<Map<ItemObject, Integer>>(){}.getType());

        this.setMinimumRare(ItemRare.valueOf(data.getOrDefault("rare", "COMMON")));
        this.setMinimumItemLevel(NumberUtility.parseInteger(data.getOrDefault("itemLevel", "1")));

        if (gear != null) this.getGearToIgnore().putAll(gear);
        if (itemsToIgnore != null) this.getItemsToIgnore().addAll(itemsToIgnore.stream().filter(Objects::nonNull).toList());
        if (aspectsToIgnore != null) this.getAspectsToIgnore().addAll(aspectsToIgnore.stream().filter(Objects::nonNull).toList());
        if (divinationCardsToIgnore != null) this.getDivinationCardsToIgnore().addAll(divinationCardsToIgnore.stream().filter(Objects::nonNull).toList());
        if (incubators != null) this.getIncubators().addAll(incubators.stream().filter(Objects::nonNull).toList());
        if (masks != null) this.getMasks().addAll(masks.stream().filter(Objects::nonNull).toList());
    }

    @Override
    public List<Bson> createUpdates() {
        return Collections.singletonList(Updates.set("filterData", this.serialize()));
    }

    @Override
    public void readDocument(Document document) {
        this.deserialize(document.get("filterData", ""));
    }

}
