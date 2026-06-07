package cupcake.nikky.imperium.tower.user.controller;

import com.google.common.util.concurrent.AtomicDouble;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ManaController {

    private final User user;
    private final AtomicDouble mana = new AtomicDouble(0);

    public double getMana() {
        return mana.get();
    }

    public void setMana(double mana) {
        val nextMana = Math.max(mana, 0);
        if (getMana() == nextMana) return;

        this.mana.set(nextMana);
        Fantasy.clientProvider().hotbarMana(user);
    }

    public void setManaForce(double mana) {
        this.mana.set(mana);
    }

    public boolean changeMana(double delta) {
        double newMana = this.getMana() + delta;
        if (newMana < 0) return false;

        val maxMana = user.findAttribute(ImperiumAttribute.MANA).findValue();
        val reserved = user.findAttribute(ImperiumAttribute.MANA_RESERVED).findValue();
        val reservedValue = user.findAttribute(ImperiumAttribute.MANA_RESERVED_VALUE).findValue();

        this.setMana(Math.min(newMana, maxMana - (maxMana * reserved) - reservedValue));
        return true;
    }

    public double transformReservePercent(final double percent) {
        val efficiency = user.findAttribute(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY).findValue();
        return (percent - percent * (efficiency - 1)) / 100D;
    }

    public double transformReserveValue(final double value) {
        val efficiency = user.findAttribute(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY).findValue();
        return value - value * (efficiency - 1);
    }

    public boolean canReservePercent(final double percent, @NotNull final AttributeMap attributes) {
        val maxMana = attributes.find(ImperiumAttribute.MANA).getValue();
        val reserved = attributes.find(ImperiumAttribute.MANA_RESERVED).getValue();
        val reservedValue = attributes.find(ImperiumAttribute.MANA_RESERVED_VALUE).getValue();
        val efficiency = attributes.find(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY).getValue();

        val remain = maxMana - (maxMana * reserved) - reservedValue;
        val needReserve = maxMana * (percent - percent * (efficiency - 1)) / 100D;

        return remain - needReserve >= 0;
    }

    public boolean canReserveValue(final double value, @NotNull final AttributeMap attributes) {
        val maxMana = attributes.find(ImperiumAttribute.MANA).getValue();
        val reserved = attributes.find(ImperiumAttribute.MANA_RESERVED).getValue();
        val reservedValue = attributes.find(ImperiumAttribute.MANA_RESERVED_VALUE).getValue();
        val efficiency = attributes.find(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY).getValue();

        val remain = maxMana - (maxMana * reserved) - reservedValue;
        val needReserve = value - value * (efficiency - 1);

        return remain - needReserve >= 0;
    }

}
