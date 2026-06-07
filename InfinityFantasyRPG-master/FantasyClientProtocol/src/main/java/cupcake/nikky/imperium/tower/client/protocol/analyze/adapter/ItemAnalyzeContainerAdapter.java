package cupcake.nikky.imperium.tower.client.protocol.analyze.adapter;

import com.google.gson.*;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyze;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeContainer;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeType;
import lombok.val;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ItemAnalyzeContainerAdapter implements JsonSerializer<ItemAnalyzeContainer>, JsonDeserializer<ItemAnalyzeContainer> {

    @Override
    public ItemAnalyzeContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        val container = new ItemAnalyzeContainer();
        val array = json.getAsJsonArray();

        container.setTitle(array.get(0).getAsString());
        container.setTiers(array.get(1).getAsInt());
        container.setMaxBaseLevel(array.get(2).getAsInt());
        container.setSumWeights(array.get(3).getAsInt());
        container.setType(ItemAnalyzeType.valueOf(array.get(4).getAsString().toUpperCase()));
        container.setAnalyzes(new ArrayList<>());

        array.get(5).getAsJsonArray().iterator().forEachRemaining(jsonElement -> container.getAnalyzes().add(context.deserialize(jsonElement, ItemAnalyze.class)));

        return container;
    }

    @Override
    public JsonElement serialize(ItemAnalyzeContainer src, Type typeOfSrc, JsonSerializationContext context) {
        val jsonArray = new JsonArray();

        jsonArray.add(new JsonPrimitive(src.getTitle()));
        jsonArray.add(new JsonPrimitive(src.getTiers()));
        jsonArray.add(new JsonPrimitive(src.getMaxBaseLevel()));
        jsonArray.add(new JsonPrimitive(src.getSumWeights()));
        jsonArray.add(new JsonPrimitive(src.getType().name()));

        val analyzeArray = new JsonArray();

        src.getAnalyzes().forEach(itemAnalyze -> analyzeArray.add(context.serialize(itemAnalyze)));
        jsonArray.add(analyzeArray);

        return jsonArray;
    }

}
