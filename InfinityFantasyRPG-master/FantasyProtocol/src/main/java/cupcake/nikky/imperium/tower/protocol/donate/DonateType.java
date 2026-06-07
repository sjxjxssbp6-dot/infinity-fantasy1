package cupcake.nikky.imperium.tower.protocol.donate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum DonateType {

    SPECIAL_CURRENCY(object -> String.format("SPECIAL_CURRENCY:%s", object.toString())),
    LOOT_FILTER_SUBSCRIPTION(object -> String.format("LOOT_FILTER_SUBSCRIPTION:%s", object.toString())),
    PREMIUM_SUBSCRIPTION(object -> String.format("PREMIUM_SUBSCRIPTION:%s", object.toString())),
    AURA(object -> String.format("AURA:%s", object.toString())),
    MOUNT(object -> String.format("MOUNT:%s", object.toString())),
    BLOOD(object -> String.format("BLOOD:%s", object.toString())),
    DEATH_EFFECT(object -> String.format("DEATH_EFFECT:%s", object.toString())),
    BUFF(object -> String.format("BUFF:%s", object.toString())),
    STORAGE(object -> String.format("STORAGE_SLOT:%s", object.toString())),
    CASE(object -> String.format("CASE:%s", object.toString())),
    PANDORA_CHEST(object -> String.format("PANDORA_CHEST:%s", object.toString())),
    BATTLE_PASS_ADVANCED(object -> String.format("BATTLE_PASS_ADVANCED:%s", object.toString())),
    BATTLE_PASS_LEVEL(object -> String.format("BATTLE_PASS_LEVEL:%s", object.toString()));

    private final Function<Object, String> transform;

}
