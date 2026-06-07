package cupcake.nikky.imperium.tower.attack.damage;

import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DamageContainer {

    @Getter private final DamageParticipant attacker;
    @Getter private final DamageParticipant defender;
    @Setter private LivingEntity attackInfluence;
    @Getter @Setter private DamageType damageType = DamageType.HIT;
    @Getter private final Map<ImperiumAttribute, AttributeInstance> additionalAttributes = new HashMap<>();

    private final Map<InfluenceType, Double> data = new EnumMap<>(InfluenceType.class);
    private int damageFlag = 0;

    public DamageContainer(@NotNull final Monster defender) {
        this.attacker = new DamageParticipant();
        this.defender = new DamageParticipant(defender);
    }

    public DamageContainer(@NotNull final User defender) {
        this.attacker = new DamageParticipant();
        this.defender = new DamageParticipant(defender);
    }

    public DamageContainer(@NotNull final User attacker, @NotNull final Monster defender) {
        this.attacker = new DamageParticipant(attacker);
        this.defender = new DamageParticipant(defender);
    }

    public DamageContainer(@NotNull final User attacker, @NotNull final User defender) {
        this.attacker = new DamageParticipant(attacker);
        this.defender = new DamageParticipant(defender);
    }

    public DamageContainer(@NotNull final Monster attacker, @NotNull final Monster defender) {
        this.attacker = new DamageParticipant(attacker);
        this.defender = new DamageParticipant(defender);
    }

    public DamageContainer(@NotNull final Monster attacker, @NotNull final User defender) {
        this.attacker = new DamageParticipant(attacker);
        this.defender = new DamageParticipant(defender);
    }

    public double findAttributeValue(@NotNull final ImperiumAttribute attribute) {
        return this.getAdditionalAttributes().getOrDefault(attribute, new AttributeInstance(0)).getValue();
    }

    public LivingEntity getAttackInfluence() {
        return attacker == null || attacker.getLivingEntity() == null ? attackInfluence : attacker.getLivingEntity();
    }

    public ParticipantCallback getAttackInfluenceCallback() {
        return new ParticipantCallback(getAttackInfluence());
    }

    public double findTotalDamage() {
        double result = 0;

        for (InfluenceType value : InfluenceType.values()) {
            result += this.findDamageByInfluence(value);
        }

        return result;
    }

    public double findDamageByInfluence(@NotNull final InfluenceType type) {
        return this.data.getOrDefault(type, 0D);
    }

    public void setDamage(@NotNull final InfluenceType type, final double value) {
        this.data.put(type, value);
    }

    public void resetDamage() {
        for (InfluenceType value : InfluenceType.values()) {
            this.setDamage(value, 0D);
        }

        this.setDamage(InfluenceType.PHYSICAL, 1);
    }

    public void applyDamageFlag(@NotNull final DamageFlag flag) {
        damageFlag |= flag.getFlag();
    }

    public void removeDamageFlag(@NotNull final DamageFlag flag) {
        if (!containsDamageFlag(flag)) return;
        damageFlag -= flag.getFlag();
    }

    public boolean containsDamageFlag(@NotNull final DamageFlag flag) {
        return (damageFlag & flag.getFlag()) == flag.getFlag();
    }

    public double changeDamage(@NotNull final InfluenceType influenceType, final double delta) {
        val current = this.findDamageByInfluence(influenceType);
        this.setDamage(influenceType, current + delta);
        return current + delta;
    }

}
