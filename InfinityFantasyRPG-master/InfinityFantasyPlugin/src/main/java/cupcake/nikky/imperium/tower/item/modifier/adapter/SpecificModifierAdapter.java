package cupcake.nikky.imperium.tower.item.modifier.adapter;

import com.google.gson.*;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseRegistry;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.val;

import java.lang.reflect.Type;

public class SpecificModifierAdapter implements JsonSerializer<SpecificModifier>, JsonDeserializer<SpecificModifier> {

    @Override
    public SpecificModifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        val array = json.getAsJsonArray();
        val id = array.get(0).getAsString();
        return new SpecificModifier(
                ModifierType.valueOf(array.get(2).getAsString()),
                null, id,
                Double.parseDouble(array.get(1).getAsString())
        );
    }

    @Override
    public JsonElement serialize(SpecificModifier src, Type typeOfSrc, JsonSerializationContext context) {
        val array = new JsonArray();
        array.add(new JsonPrimitive(src.getModifierId()));
        array.add(new JsonPrimitive(src.getKey()));
        array.add(new JsonPrimitive(src.getModifierType().name()));
        return array;
    }

}
