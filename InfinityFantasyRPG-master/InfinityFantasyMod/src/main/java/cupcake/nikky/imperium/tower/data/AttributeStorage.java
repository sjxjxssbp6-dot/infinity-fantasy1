package cupcake.nikky.imperium.tower.data;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import dev.xdark.feder.MathUtil;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttributeStorage {

    @Getter
    private static final AttributeStorage instance = new AttributeStorage();

    private final Map<ImperiumAttribute, Double> data = new EnumMap<>(ImperiumAttribute.class);

    public void clear() {
        this.data.clear();
    }

    public void updateValue(@NotNull final ImperiumAttribute attribute, final double value) {
        this.data.put(attribute, value);
    }

    public int maxAmount() {
        return Math.max(this.findAttackAttributes().size(), this.findDefenseAttributes().size());
    }

    public Map<ImperiumAttribute, Double> findAll() {
        return new EnumMap<>(data);
    }

    public Map<ImperiumAttribute, Double> findAttackAttributes() {
        val result = this.findAll();
        for (ImperiumAttribute attribute : ImperiumAttribute.findDefenseAttributes()) {
            result.remove(attribute);
        }
        return result;
    }

    public Map<ImperiumAttribute, Double> findDefenseAttributes() {
        val result = this.findAll();
        for (ImperiumAttribute attribute : ImperiumAttribute.findAttackAttributes()) {
            result.remove(attribute);
        }
        return result;
    }

}
