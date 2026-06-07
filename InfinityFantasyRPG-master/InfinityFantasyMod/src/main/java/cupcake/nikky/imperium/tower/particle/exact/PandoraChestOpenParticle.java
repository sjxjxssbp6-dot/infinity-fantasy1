package cupcake.nikky.imperium.tower.particle.exact;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.values.StableColorValue;
import gg.cristalix.particle.system.values.StableDoubleValue;
import gg.cristalix.particle.system.values.StableIntValue;
import gg.cristalix.particle.system.zone.CubeParticleZone;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class PandoraChestOpenParticle extends BaseParticle {

    private ParticleData particleData;

    public PandoraChestOpenParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        this.particleData = ParticleData.builder()
                .color(new StableColorValue(Palette.RED))
                .size(new StableDoubleValue(.2))
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.5, 1)
                        .addPoint(0.55, .5)
                        .addPoint(1, 0)
                        .build()
                )
                .sizeCurve(Curve.builder()
                        .addPoint(0, 3)
                        .addPoint(.2, 3)
                        .addPoint(.25, .5)
                        .addPoint(1, .5)
                        .build())
                .lookAtPlayerYaw(true)
                .lifeTicks(new StableIntValue(30))
                .textureData(ParticleTextures.QUAD)
                .build();
    }

    public void spawn(final double x, final double y, final double z) {
        particleSystem().spawnParticles(particleData, new CubeParticleZone(new V3(1, 1, 1)), new V3(x, y, z), 50);
    }

}
