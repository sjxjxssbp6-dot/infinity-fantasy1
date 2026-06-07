package cupcake.nikky.imperium.tower.client.protocol.analyze.adapter;

import com.google.gson.*;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyze;
import lombok.val;

import java.lang.reflect.Type;

public class ItemAnalyzeAdapter implements JsonSerializer<ItemAnalyze>, JsonDeserializer<ItemAnalyze> {

    @Override
    public ItemAnalyze deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        val array = json.getAsJsonArray();
        return new ItemAnalyze(
                array.get(0).getAsDouble(),
                array.get(1).getAsDouble(),
                array.get(2).getAsDouble(),
                array.get(3).getAsInt(),
                array.get(4).getAsInt(),
                array.get(5).getAsInt());
    }

    @Override
    public JsonElement serialize(ItemAnalyze src, Type typeOfSrc, JsonSerializationContext context) {
        val array = new JsonArray();
        array.add(new JsonPrimitive(src.getMin()));
        array.add(new JsonPrimitive(src.getMax()));
        array.add(new JsonPrimitive(src.getAmplitude()));
        array.add(new JsonPrimitive(src.getTier()));
        array.add(new JsonPrimitive(src.getBaseLevel()));
        array.add(new JsonPrimitive(src.getWeight()));
        return array;
    }

}
