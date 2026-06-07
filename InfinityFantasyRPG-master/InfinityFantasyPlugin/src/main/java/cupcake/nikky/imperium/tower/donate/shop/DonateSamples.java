package cupcake.nikky.imperium.tower.donate.shop;

import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.blood.BloodObject;
import cupcake.nikky.imperium.tower.buff.Buff;
import cupcake.nikky.imperium.tower.death.DeathObject;
import cupcake.nikky.imperium.tower.donate.SubscriptionDurationType;
import cupcake.nikky.imperium.tower.donate.shop.sample.DonateSample;
import cupcake.nikky.imperium.tower.donate.shop.sample.exact.*;
import cupcake.nikky.imperium.tower.mount.MountType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DonateSamples {

    @Getter private static final DonateSample<Buff> buffSample = new DonateBuffSample();
    @Getter private static final DonateSample<Integer> currencySample = new DonateCurrencySample();
    @Getter private static final DonateSample<SubscriptionDurationType> premiumSample = new DonatePremiumSample();
    @Getter private static final DonateSample<SubscriptionDurationType> lootFilterSample = new DonateLootFilterSample();
    @Getter private static final DonateSample<AuraObject> auraSample = new DonateAuraSample();
    @Getter private static final DonateSample<MountType> mountSample = new DonateMountSample();
    @Getter private static final DonateSample<BloodObject> bloodSample = new DonateBloodSample();
    @Getter private static final DonateSample<DeathObject> deathSample = new DonateDeathSample();

}
