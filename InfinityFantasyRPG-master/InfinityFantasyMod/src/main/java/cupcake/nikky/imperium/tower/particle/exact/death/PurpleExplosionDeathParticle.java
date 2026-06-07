package cupcake.nikky.imperium.tower.particle.exact.death;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.values.RandomDoubleValue;
import gg.cristalix.particle.system.values.StableColorValue;
import gg.cristalix.particle.system.values.StableDoubleValue;
import gg.cristalix.particle.system.values.StableIntValue;
import gg.cristalix.particle.system.zone.PointParticleZone;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class PurpleExplosionDeathParticle extends BaseParticle {

    private ParticleData deathData;

    public PurpleExplosionDeathParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        deathData = ParticleData.builder()
                .color(new StableColorValue(Palette.PURPLE_DARK))
                .size(new StableDoubleValue(.2))
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.5, 1)
                        .addPoint(0.55, .5)
                        .addPoint(1, 0)
                        .build()
                )
                .sizeCurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.5, .75)
                        .addPoint(.75, .50)
                        .addPoint(1, .25)
                        .build())
                .velocityCurve(Curve.builder()
                        .addPoint(0, 5)
                        .addPoint(.1, 5)
                        .addPoint(.15, .1)
                        .addPoint(1, .1)
                        .build())
                .velocity(new RandomDoubleValue(-.2, .2))
                .lookAtPlayer(true)
                .lifeTicks(new StableIntValue(30))
                .textureData(ParticleTextures.QUAD)
                .build();
    }

    public void spawn(final double x, final double y, final double z) {
        particleSystem().spawnParticles(deathData,
                new PointParticleZone(new V3(x, y, z)),
                new V3(0, .5, 0),
                80);
    }

}
