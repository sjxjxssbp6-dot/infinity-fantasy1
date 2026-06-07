package cupcake.nikky.imperium.tower.buff;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.utility.LevelUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntConsumer;

@RequiredArgsConstructor
public class FantasyBuff implements Buff {

    @Getter private final int durationSeconds;
    @Getter private final String title;
    private final AtomicBoolean stackable = new AtomicBoolean(false);
    private final AtomicBoolean durationOnlineOnly = new AtomicBoolean(true);
    private final AtomicInteger currentSeconds = new AtomicInteger(0);
    private final AtomicInteger multiplier = new AtomicInteger(1);
    private final AtomicInteger maxMultiplier = new AtomicInteger(1);
    private final AtomicBoolean shouldInvalidate = new AtomicBoolean(false);
    private final AtomicBoolean shouldInvalidateAfterDeath = new AtomicBoolean(false);
    private final AtomicReference<BuffType> buffType = new AtomicReference<>(null);
    private final AtomicBoolean justUpdatedMultiplier = new AtomicBoolean(false);

    @Getter private final AttributeMap attributes = new AttributeMap();
    private final AtomicInteger level = new AtomicInteger(1);

    private final List<IntConsumer> attributeHandlers = new CopyOnWriteArrayList<>();
    private final Set<ImperiumAttribute> percentAttributes = new CopyOnWriteArraySet<>();

    @Override
    public void onReceive(LivingEntity entity) {

    }

    @Override
    public void onCompletion(LivingEntity entity) {

    }

    @Override
    public void onTick(@NotNull final LivingEntity entity) {
        this.setCurrentSeconds(this.getCurrentSeconds() + 1);

        getAttributes().clear();

        val level = LevelUtility.getLevel(entity);
        this.setLevel(level);
        attributeHandlers.forEach(handler -> handler.accept(level));

        if (getMultiplier() < 2) return;

        getAttributes().forEach(attributeInstance -> attributeInstance.applyIncrease(getMultiplier() - 1D));
    }

    @Override
    public Buff registerPercentAttribute(@NotNull final ImperiumAttribute attribute, final double value) {
        this.setupPercentAttribute(attribute);
        return this.addAttributeHandler(level -> getAttributes().register(attribute, value));
    }

    @Override
    public Buff registerMultiplierAttribute(@NotNull final ImperiumAttribute attribute, final double multiplier) {
        this.setupPercentAttribute(attribute);
        return this.addAttributeHandler(level -> getAttributes().register(attribute, 0).find(attribute).setIncrease(multiplier));
    }

    @Override
    public Buff registerValueAttribute(@NotNull final ImperiumAttribute attribute, final double value) {
        return this.addAttributeHandler(level -> getAttributes().register(attribute, value));
    }

    @Override
    public Buff addAttributeHandler(@NotNull final IntConsumer handler) {
        this.attributeHandlers.add(handler);
        return this;
    }

    @Override
    public List<IntConsumer> getAttributeHandlers() {
        return new ArrayList<>(attributeHandlers);
    }

    public Buff setupPercentAttribute(@NotNull final ImperiumAttribute attribute) {
        this.percentAttributes.add(attribute);
        return this;
    }

    @Override
    public List<String> getDescription() {
        final List<String> result = new ArrayList<>();

        getAttributes().getAll().forEach(pair -> {
            val attribute = pair.getFirst();
            if (attribute == ImperiumAttribute.POWER_CHARGE || attribute == ImperiumAttribute.ENDURANCE_CHARGE || attribute == ImperiumAttribute.FRENZY_CHARGE) return;

            val isPercent = percentAttributes.contains(attribute);
            val value = findAttribute(attribute).findValue();
            final String sym = (value >= 0D) ? "&3+" : "&3-";
            final String postfix = isPercent ? "%" : "";
            result.add(String.format("%s%.1f%s %s", attribute.getTitle().contains("Увеличение") ?
                    sym.replace("+", "").replace("-", "") : sym,
                    Math.abs(value), postfix, "&6" + attribute.getTitle()));
        });

        return TextUtility.colorize(result);
    }

    @Override
    public int getLevel() {
        return level.get();
    }

    @Override
    public Buff setLevel(final int level) {
        for (;;) {
            var old = this.level.get();
            if (this.level.compareAndSet(old, level)) break;
        }
        return this;
    }

    @Override
    public Buff setDurationOnlineOnly(final boolean value) {
        for (;;) {
            var old = durationOnlineOnly.get();
            if (durationOnlineOnly.compareAndSet(old, value)) break;
        }
        return this;
    }

    @Override
    public boolean isDurationOnlineOnly() {
        return durationOnlineOnly.get();
    }

    @Override
    public int getCurrentSeconds() {
        return currentSeconds.get();
    }

    @Override
    public Buff setCurrentSeconds(final int ticks) {
        for (;;) {
            var old = currentSeconds.get();
            if (this.currentSeconds.compareAndSet(old, ticks)) break;
        }

        if(ticks >= this.getDurationSeconds()) {
            this.setShouldInvalidate(true);
        }

        return this;
    }

    @Override
    public int getMultiplier() {
        return multiplier.get();
    }

    @Override
    public Buff setMultiplier(final int multiplier) {
        for (;;) {
            var old = this.multiplier.get();
            if (this.multiplier.compareAndSet(old, multiplier)) break;
        }
        return this;
    }

    @Override
    public boolean shouldInvalidate() {
        return shouldInvalidate.get();
    }

    public void setShouldInvalidate(final boolean shouldInvalidate) {
        for (;;) {
            var old = this.shouldInvalidate.get();
            if (this.shouldInvalidate.compareAndSet(old, shouldInvalidate)) break;
        }
    }

    @Override
    public boolean shouldInvalidateAfterDeath() {
        return shouldInvalidateAfterDeath.get();
    }

    @Override
    public Buff setShouldInvalidateAfterDeath(final boolean shouldInvalidate) {
        for (;;) {
            var old = this.shouldInvalidateAfterDeath.get();
            if (this.shouldInvalidateAfterDeath.compareAndSet(old, shouldInvalidate)) break;
        }
        return this;
    }

    @Override
    public Buff setMaxMultiplier(final int value) {
        for (;;) {
            var old = maxMultiplier.get();
            if (maxMultiplier.compareAndSet(old, value)) break;
        }
        return this;
    }

    @Override
    public int getMaxMultiplier() {
        return maxMultiplier.get();
    }

    @Override
    public BuffType getBuffType() {
        return this.buffType.get();
    }

    @Override
    public Buff setType(@NotNull final BuffType buffType) {
        for (;;) {
            var old = this.buffType.get();
            if (this.buffType.compareAndSet(old, buffType)) break;
        }
        return this;
    }

    @Override
    public boolean isStackable() {
        return stackable.get();
    }

    @Override
    public Buff setStackable(boolean value) {
        for (;;) {
            var old = stackable.get();
            if (stackable.compareAndSet(old, value)) break;
        }
        return this;
    }

    @Override
    public boolean isJustUpdatedMultiplier() {
        return justUpdatedMultiplier.get();
    }

    @Override
    public void setJustUpdatedMultiplier(boolean value) {
        for (;;) {
            var old = justUpdatedMultiplier.get();
            if (justUpdatedMultiplier.compareAndSet(old, value)) break;
        }
    }

    @Override
    public String serialize() {
        val type = new TypeToken<Map<String, String>>(){}.getType();

        final Map<String, String> data = new HashMap<>();

        val attributes = this.getAttributes();
        if (!attributes.isEmpty()) {
            attributes.getAll().stream().filter(pair -> pair.getSecond().getValue() != 0D).findAny().ifPresent(pair -> {
                AttributeInstance instance = pair.getSecond();
                double base = instance.getBaseValue();
                double additional = instance.getAdditional();
                double multiplier = instance.getIncrease();
                data.put("value", String.format("%.1f", base == 0D ? additional == 0D ? multiplier == 0D ? 0D : multiplier : additional : base));
            });
        }

        if (!isDurationOnlineOnly()) data.put("savedTime", String.valueOf(Instant.now().toEpochMilli()));

        data.put("buffType", getBuffType().name());
        data.put("duration", String.valueOf(getDurationSeconds()));
        data.put("currentTicks", String.valueOf(getCurrentSeconds()));
        data.put("multiplier", String.valueOf(getMultiplier()));

        return Fantasy.gson().toJson(data, type);
    }

}
