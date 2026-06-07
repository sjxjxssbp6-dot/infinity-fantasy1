package cupcake.nikky.imperium.tower.attribute;

import com.google.common.base.Objects;
import cupcake.nikky.imperium.tower.attribute.process.AttributeProcess;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class AttributeMap {

    private final Map<ImperiumAttribute, AttributeInstance> data;

    public AttributeMap(@NotNull final Map<ImperiumAttribute, AttributeInstance> data) {
        this.data = data;
    }

    public AttributeMap() {
        this(new ConcurrentHashMap<>(ImperiumAttribute.values().length, 1F, 2));
    }

    public AttributeMap register(@NotNull final ImperiumAttribute object, @NotNull final AttributeInstance instance) {
        this.data.put(object, instance);
        return this;
    }

    public AttributeMap register(@NotNull final ImperiumAttribute object, final double value) {
        return this.register(object, new AttributeInstance(value));
    }

    public AttributeMap register(@NotNull final AttributeProcess attributeProcess) {
        return this.register(attributeProcess.findSourceAttribute(), new AttributeInstance(attributeProcess.findValue()));
    }

    public AttributeMap invalidate(@NotNull final ImperiumAttribute object) {
        this.data.remove(object);
        return this;
    }

    public AttributeInstance find(@NotNull final ImperiumAttribute object) {
        AttributeInstance instance = this.data.get(object);

        if (instance == null) {
            instance = new AttributeInstance(0D);
            this.register(object, instance);
        }

        return instance;
    }

    public void replace(@NotNull final AttributeMap attributeMap) {
        this.data.clear();

        attributeMap.data.forEach(this::register);
    }

    public void join(@NotNull final AttributeMap attributeMap) {
        attributeMap.data.forEach((imperiumAttribute, instance) -> this.find(imperiumAttribute).applyInstance(instance));
    }

    public void forEach(@NotNull final Consumer<AttributeInstance> handler) {
        this.data.values().forEach(handler);
    }

    public List<Pair<ImperiumAttribute, AttributeInstance>> getAll() {
        return data.keySet().stream()
                .map(object -> new Pair<>(object, this.find(object)))
                .filter(object -> !object.getSecond().isEmpty())
                .toList();
    }

    public AttributeMap clear() {
        this.data.clear();
        return this;
    }

    public boolean isEmpty() {
        return this.data.isEmpty() || data.values().stream().noneMatch(attributeInstance -> attributeInstance.getValue() != 0D || attributeInstance.getIncrease() != 1D);
    }

    public boolean equalsPacketAttributes(@NotNull final AttributeMap attributeMap) {
        for (ImperiumAttribute attribute : ImperiumAttribute.findAttackAndDefenseAttributes()) {
            var currentValue = this.data.get(attribute);
            var nextValue = attributeMap.data.get(attribute);

            if (currentValue != null && !currentValue.equals(nextValue)) return false;
            if (nextValue != null && !nextValue.equals(currentValue)) return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeMap that = (AttributeMap) o;
        if (data.size() != that.data.size()) return false;

        for (Map.Entry<ImperiumAttribute, AttributeInstance> attribute : data.entrySet()) {
            var value = that.data.get(attribute.getKey());
            //if (value != null && value.isEmpty()) continue;

            if (!attribute.getValue().equals(value)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }

}