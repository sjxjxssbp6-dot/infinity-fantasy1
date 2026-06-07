package cupcake.nikky.imperium.tower.particle.exact.death;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.texture.RandomTexture;
import gg.cristalix.particle.system.values.RandomDoubleValue;
import gg.cristalix.particle.system.values.RandomIntValue;
import gg.cristalix.particle.system.values.StableColorValue;
import gg.cristalix.particle.system.zone.CubeParticleZone;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class ExplosionParticle extends BaseParticle {

    private ParticleData deathData;

    public ExplosionParticle(@NotNull final Palette palette) {
        super(new ParticleSystem(), WorldContext.get());

        this.deathData = ParticleData.builder()
                .color(new StableColorValue(palette))
                .colorACurve(
                        Curve.builder()
                                .addPoint(0, 1)
                                .addPoint(0.75, 0.5)
                                .addPoint(1, 0)
                                .build()
                )
                .size(new RandomDoubleValue(.8, 1))
                .sizeCurve(
                        Curve.builder()
                                .addPoint(0, 1)
                                .addPoint(0.2, 1)
                                .addPoint(0.6, .75)
                                .addPoint(0.7, .5)
                                .addPoint(0.8, .4)
                                .addPoint(0.9, .3)
                                .addPoint(1, 0)
                                .build()
                )
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.6, .5)
                        .addPoint(.8, .3)
                        .addPoint(1, 0)
                        .build())
                .lookAtPlayer(true)
                .lifeTicks(new RandomIntValue(20, 30))
                .textureStorage(new RandomTexture(
                        ParticleTextures.EXPLOSION_0,
                        ParticleTextures.EXPLOSION_1,
                        ParticleTextures.EXPLOSION_2,
                        ParticleTextures.EXPLOSION_3,
                        ParticleTextures.EXPLOSION_4,
                        ParticleTextures.EXPLOSION_5,
                        ParticleTextures.EXPLOSION_6,
                        ParticleTextures.EXPLOSION_7,
                        ParticleTextures.EXPLOSION_8,
                        ParticleTextures.EXPLOSION_9,
                        ParticleTextures.EXPLOSION_10,
                        ParticleTextures.EXPLOSION_11,
                        ParticleTextures.EXPLOSION_12,
                        ParticleTextures.EXPLOSION_13,
                        ParticleTextures.EXPLOSION_14,
                        ParticleTextures.EXPLOSION_15
                ))
                .build();
    }

    @Override
    protected void initialize() {

    }

    public void spawn(final double x, final double y, final double z) {
        particleSystem().spawnParticles(deathData,
                new CubeParticleZone(new V3(3, 3, 3)),
                new V3(x, y + .5, z),
                30);
    }

}
