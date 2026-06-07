package cupcake.nikky.imperium.tower.entity.monster.modifier;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ModifierProcess {

    public static final ModifierProcess ADD_VALUE_PROCESS = new ModifierProcess() {
        @Override
        public void applyAttribute(@NotNull Monster monster, @NotNull AttributeModifier modifier) {
            monster.getAttributes().find(modifier.getAttribute()).applyBaseValue(modifier.getAttributeValue().apply(monster));
        }
    };

    public static final ModifierProcess ADD_MULTIPLIED_VALUE_PROCESS = new ModifierProcess() {
        @Override
        public void applyAttribute(@NotNull Monster monster, @NotNull AttributeModifier modifier) {
            val attribute = monster.getAttributes().find(modifier.getAttribute());
            attribute.applyBaseValue(attribute.getBaseValue() * modifier.getAttributeValue().apply(monster));
        }
    };

    public static final ModifierProcess ADD_TO_MORE_PROCESS = new ModifierProcess() {
        @Override
        public void applyAttribute(@NotNull Monster monster, @NotNull AttributeModifier modifier) {
            monster.getAttributes().find(modifier.getAttribute()).applyMore(modifier.getAttributeValue().apply(monster));
        }
    };

    public abstract void applyAttribute(@NotNull final Monster monster, @NotNull final AttributeModifier modifier);

}
