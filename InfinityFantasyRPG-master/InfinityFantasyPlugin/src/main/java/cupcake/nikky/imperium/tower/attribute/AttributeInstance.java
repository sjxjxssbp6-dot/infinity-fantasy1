package cupcake.nikky.imperium.tower.attribute;

import com.google.common.base.Objects;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class AttributeInstance {

    private final AtomicDouble baseValue = new AtomicDouble();
    private final AtomicDouble increase = new AtomicDouble();
    private final AtomicDouble additional = new AtomicDouble();
    private final AtomicDouble more = new AtomicDouble(1D);

    public AttributeInstance(final double baseValue) {
        this.setBaseValue(baseValue);
    }

    public void clearForeign() {
        this.setAdditional(0);
        this.setIncrease(0);
        this.setMore(1D);
    }

    public double getBaseValue() {
        return baseValue.get();
    }

    public void setBaseValue(final double baseValue) {
        this.baseValue.set(baseValue);
    }

    public void setMore(final double more) {
        this.more.set(more);
    }

    public AttributeInstance applyMore(final double more) {
        for (;;) {
            val oldMultiplier = this.more.get();
            if (this.more.compareAndSet(oldMultiplier, oldMultiplier * more)) break;
        }

        return this;
    }

    public double getMore() {
        return more.get();
    }

    public void setIncrease(final double increase) {
        this.increase.set(increase);
    }

    public AttributeInstance applyIncrease(final double multiplier) {
        for (;;) {
            val oldMultiplier = this.increase.get();
            if (this.increase.compareAndSet(oldMultiplier, oldMultiplier + multiplier)) break;
        }

        return this;
    }

    public double getIncrease() {
        return increase.get();
    }

    public void setAdditional(final double additional) {
        this.additional.set(additional);
    }

    public AttributeInstance applyAdditional(final double additional) {
        for (;;) {
            val oldAdditional = this.getAdditional();
            if (this.additional.compareAndSet(oldAdditional, oldAdditional + additional)) break;
        }

        return this;
    }

    public double getAdditional() {
        return additional.get();
    }

    public double getValue() {
        return (this.getBaseValue() + this.getAdditional()) * (1 + getIncrease()) * getMore();
    }

    public AttributeInstance applyInstance(@NotNull final AttributeInstance attributeInstance) {
        this.applyAdditional(attributeInstance.getBaseValue())
                .applyAdditional(attributeInstance.getAdditional())
                .applyIncrease(attributeInstance.getIncrease())
                .applyMore(attributeInstance.getMore());
        return this;
    }

    public AttributeInstance setInstance(@NotNull final AttributeInstance attributeInstance) {
        this.setBaseValue(attributeInstance.getBaseValue());
        this.setAdditional(attributeInstance.getAdditional());
        this.setIncrease(attributeInstance.getIncrease());
        this.setMore(attributeInstance.getMore());

        return this;
    }

    public AttributeInstance applyBaseValue(final double value) {
        for (;;) {
            val oldBase = this.getBaseValue();
            if (this.baseValue.compareAndSet(oldBase, oldBase + value)) break;
        }

        return this;
    }

    public boolean isEmpty() {
        return getIncrease() == 0D && getBaseValue() == 0D && getAdditional() == 0D && getMore() == 1D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeInstance that = (AttributeInstance) o;
        return getBaseValue() == that.getBaseValue() && getAdditional() == that.getAdditional() && getIncrease() == that.getIncrease() && getMore() == that.getMore();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBaseValue(), getIncrease(), getAdditional(), getMore());
    }

}
