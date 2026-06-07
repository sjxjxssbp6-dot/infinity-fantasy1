package cupcake.nikky.imperium.tower.aura.data;

import cupcake.nikky.imperium.tower.aura.FantasyAuraManager;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.aura.Aura;
import ru.cristalix.wada.module.aura.AuraAnimation;
import ru.cristalix.wada.module.aura.AuraTexture;

@Getter
@RequiredArgsConstructor
public enum AuraObject {

    NONE(new FantasyAura("Без ауры")),
    NONE_ALL(new FantasyAura("Выключить все ауры")),
    PREMIUM_AURA(new FantasyAura("Исключительная аура", AuraTexture.SPACE, AuraAnimation.PULSE_ROUND,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed(), "&6Премиум подписка"))),
    AWARENESS_AURA(new FantasyAura("Осознанная аура", AuraTexture.AWARENESS, AuraAnimation.ARROW,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("AWARENESS_AURA")))),
    CONFRONTATION_AURA(new FantasyAura("Аура противостояния", AuraTexture.CONFRONTATION, AuraAnimation.PULSE_ROUND_REVERSE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("CONFRONTATION_AURA")))),
    BONE_AURA(new FantasyAura("Костяная аура", AuraTexture.BONE, AuraAnimation.PULSE_HORIZONTAL,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("BONE_AURA")))),
    HUMILITY_AURA(new FantasyAura("Аура смирения", AuraTexture.HUMILITY, AuraAnimation.PULSE_HORIZONTAL_REVERSE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("HUMILITY_AURA")))),
    SNOWFLAKE_AURA(new FantasyAura("Аура снежинки", AuraTexture.SNOWFLAKE, AuraAnimation.WAVE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("SNOWFLAKE_AURA")))),
    PUMPKIN_AURA(new FantasyAura("Аура тыквы", AuraTexture.PUMPKIN, AuraAnimation.PULSE_ROUND,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("PUMPKIN_AURA")))),
    CHRISTMAS_AURA(new FantasyAura("Рождественская аура", AuraTexture.CHRISTMAS, AuraAnimation.DOUBLE_CIRCLE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("CHRISTMAS_AURA")))),
    WATER_AURA(new FantasyAura("Водяная аура", AuraTexture.WATER, AuraAnimation.ARROW,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("WATER_AURA")))),
    BLACK_AURA(new FantasyAura("Темная аура", AuraTexture.BLACK, AuraAnimation.PULSE_ROUND_REVERSE,
            AuraRequirement.of(player ->player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("BLACK_AURA")))),
    DEEP_AURA(new FantasyAura("Глубокая аура", AuraTexture.DEEP, AuraAnimation.PULSE_HORIZONTAL,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("DEEP_AURA")))),
    DEFENCE_AURA(new FantasyAura("Защитная аура", AuraTexture.DEFENCE, AuraAnimation.PULSE_HORIZONTAL_REVERSE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("DEFENCE_AURA")))),
    CURSE_AURA(new FantasyAura("Проклятая аура", AuraTexture.CURSE, AuraAnimation.WAVE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("CURSE_AURA")))),
    DRAGON_AURA(new FantasyAura("Драконья аура", AuraTexture.DRAGON, AuraAnimation.PULSE_ROUND,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("DRAGON_AURA")))),
    EARTH_AURA(new FantasyAura("Аура земли", AuraTexture.EARTH, AuraAnimation.DOUBLE_CIRCLE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("EARTH_AURA")))),
    EXHAUSTION_AURA(new FantasyAura("Аура истощения", AuraTexture.EXHAUSTION, AuraAnimation.ARROW,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("EXHAUSTION_AURA")))),
    FIRE_AURA(new FantasyAura("Огненная аура", AuraTexture.FIRE, AuraAnimation.PULSE_ROUND_REVERSE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("FIRE_AURA")))),
    FLAME_AURA(new FantasyAura("Пламенная аура", AuraTexture.FLAME, AuraAnimation.PULSE_HORIZONTAL,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("FLAME_AURA")))),
    FOREST_AURA(new FantasyAura("Лесная аура", AuraTexture.FOREST, AuraAnimation.PULSE_HORIZONTAL_REVERSE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("FOREST_AURA")))),
    GOODNESS_AURA(new FantasyAura("Добрая аура", AuraTexture.GOODNESS, AuraAnimation.WAVE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("GOODNESS_AURA")))),
    ICE_AURA(new FantasyAura("Ледяная аура", AuraTexture.ICE, AuraAnimation.PULSE_ROUND,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("ICE_AURA")))),
    ICE_BREATHE_AURA(new FantasyAura("Аура ледяного дыхания", AuraTexture.ICE_BREATHE, AuraAnimation.DOUBLE_CIRCLE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("ICE_BREATHE_AURA")))),
    LIGHT_AURA(new FantasyAura("Светлая аура", AuraTexture.LIGHT, AuraAnimation.ARROW,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("LIGHT_AURA")))),
    LIGHTING_AURA(new FantasyAura("Световая аура", AuraTexture.LIGHTNING, AuraAnimation.PULSE_ROUND_REVERSE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("LIGHTING_AURA")))),
    REBIRTH_AURA(new FantasyAura("Аура перерождения", AuraTexture.REBIRTH, AuraAnimation.PULSE_HORIZONTAL,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("REBIRTH_AURA")))),
    SCIENCE_AURA(new FantasyAura("Научная аура", AuraTexture.SCIENCE, AuraAnimation.PULSE_HORIZONTAL_REVERSE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("SCIENCE_AURA")))),
    LAVA_AURA(new FantasyAura("Лавовая аура", AuraTexture.LAVA, AuraAnimation.WAVE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("LAVA_AURA")))),
    LEAF_AURA(new FantasyAura("Аура листа", AuraTexture.LEAF, AuraAnimation.PULSE_ROUND,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("LEAF_AURA")))),
    MAGIC_AURA(new FantasyAura("Магическая аура", AuraTexture.MAGIC, AuraAnimation.DOUBLE_CIRCLE,
            AuraRequirement.of(player -> player.<User>getBungeePlayer().findDonateSettings().donateData().containsMetadata("MAGIC_AURA"))));

    private final FantasyAura fantasyAura;

    public void apply(@NotNull final Player player) {
        val user = player.<User>getBungeePlayer();
        if (user.getCurrentAura() != null) {
            FantasyAuraManager.get().removeEntityAura(player.getEntityId());

            if (user.getCurrentAura() == NONE_ALL) {
                FantasyAuraManager.get().invalidateIgnore(player.getUniqueId());
            }
        }

        user.setCurrentAura(this);

        if (this == NONE) {
            return;
        } else if (this == NONE_ALL) {
            FantasyAuraManager.get().addToIgnoreList(player.getUniqueId());
            return;
        }

        val aura = Aura.builder()
                .texture(fantasyAura.texture())
                .entity(player)
                .animation(fantasyAura.animation())
                //.bindOnlyToPlayer(true)
                .build();
        FantasyAuraManager.get().add(aura);
    }

}
