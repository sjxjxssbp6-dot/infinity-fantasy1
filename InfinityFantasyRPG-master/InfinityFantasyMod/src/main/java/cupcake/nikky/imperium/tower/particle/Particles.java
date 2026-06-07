package cupcake.nikky.imperium.tower.particle;

import cupcake.nikky.imperium.tower.particle.exact.*;
import cupcake.nikky.imperium.tower.particle.exact.blood.EntityBaseBloodParticle;
import cupcake.nikky.imperium.tower.particle.exact.blood.EntityHeartBloodParticle;
import cupcake.nikky.imperium.tower.particle.exact.blood.EntityMultiColoredBloodParticle;
import cupcake.nikky.imperium.tower.particle.exact.blood.EntityWhiteBloodParticle;
import cupcake.nikky.imperium.tower.particle.exact.death.ExplosionParticle;
import cupcake.nikky.imperium.tower.particle.exact.death.PurpleExplosionDeathParticle;
import cupcake.nikky.imperium.tower.particle.exact.death.RedLineUpDeathParticle;
import ru.cristalix.enginex.util.Palette;

public class Particles {

    public static EntityBaseBloodParticle BASE_BLOOD;
    public static EntityHeartBloodParticle HEART_BLOOD;
    public static EntityWhiteBloodParticle WHITE_BLOOD;
    public static EntityMultiColoredBloodParticle MULTI_COLORED_BLOOD;

    public static EntityLevelUpParticle ENTITY_LEVEL_UP;
    public static EntityIceExplosionParticle ENTITY_ICE_EXPLOSION;
    public static EdgeFragmentZoneParticle EDGE_FRAGMENT_ZONE;
    public static EntitySparksParticle ENTITY_SPARKS;

    public static EntityAilmentParticle ENTITY_SHOCK;
    public static EntityAilmentParticle ENTITY_IGNITE;
    public static EntityAilmentParticle ENTITY_FREEZE;

    public static ExplosionParticle DARK_EXPLOSION_DEATH;
    public static PurpleExplosionDeathParticle PURPLE_EXPLOSION_DEATH;
    public static RedLineUpDeathParticle RED_LINE_UP_DEATH;

    public static PandoraChestOpenParticle PANDORA_CHEST_OPEN;
    public static PandoraChestSpawnParticle PANDORA_CHEST_SPAWN;

    public static BinaEffect TORNADO;
    public static ExplosionParticle PURPLE_EXPLOSION_EDGE;

    public static EntityHealParticle HEAL;

    public static void loadAll() {
        BASE_BLOOD = new EntityBaseBloodParticle();
        HEART_BLOOD = new EntityHeartBloodParticle();
        WHITE_BLOOD = new EntityWhiteBloodParticle();
        MULTI_COLORED_BLOOD = new EntityMultiColoredBloodParticle();

        ENTITY_LEVEL_UP = new EntityLevelUpParticle();
        ENTITY_ICE_EXPLOSION = new EntityIceExplosionParticle();
        EDGE_FRAGMENT_ZONE = new EdgeFragmentZoneParticle();
        ENTITY_SPARKS = new EntitySparksParticle();

        ENTITY_SHOCK = new EntityAilmentParticle(Palette.YELLOW);
        ENTITY_IGNITE = new EntityAilmentParticle(Palette.RED);
        ENTITY_FREEZE = new EntityAilmentParticle(Palette.BLUE_LIGHT);

        DARK_EXPLOSION_DEATH = new ExplosionParticle(Palette.BLACK);
        PURPLE_EXPLOSION_DEATH = new PurpleExplosionDeathParticle();
        RED_LINE_UP_DEATH = new RedLineUpDeathParticle();

        PANDORA_CHEST_OPEN = new PandoraChestOpenParticle();
        PANDORA_CHEST_SPAWN = new PandoraChestSpawnParticle();

        TORNADO = new BinaEffect();
        PURPLE_EXPLOSION_EDGE = new ExplosionParticle(Palette.PURPLE);

        HEAL = new EntityHealParticle();
    }

    public static void invalidateAll() {
        BASE_BLOOD = null;
        HEART_BLOOD = null;
        WHITE_BLOOD = null;
        MULTI_COLORED_BLOOD = null;

        ENTITY_LEVEL_UP = null;
        ENTITY_ICE_EXPLOSION = null;
        EDGE_FRAGMENT_ZONE = null;
        ENTITY_SPARKS = null;

        ENTITY_SHOCK = null;
        ENTITY_IGNITE = null;
        ENTITY_FREEZE = null;

        DARK_EXPLOSION_DEATH = null;
        PURPLE_EXPLOSION_DEATH = null;
        RED_LINE_UP_DEATH = null;

        PANDORA_CHEST_OPEN = null;
        PANDORA_CHEST_SPAWN = null;

        TORNADO = null;
        PURPLE_EXPLOSION_EDGE = null;

        HEAL = null;
    }

}
