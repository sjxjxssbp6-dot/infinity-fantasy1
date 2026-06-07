package cupcake.nikky.imperium.tower.particle.exact;

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
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.Container;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class PandoraChestSpawnParticle extends BaseParticle {

    private ParticleData particleData;

    public PandoraChestSpawnParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        this.particleData = ParticleData.builder()
                .color(new StableColorValue(Palette.YELLOW))
                .size(new StableDoubleValue(.2))
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.5, 1)
                        .addPoint(0.55, .5)
                        .addPoint(1, 0)
                        .build()
                )
                .velocityCurve(Curve.builder()
                        .addPoint(0, 5)
                        .addPoint(.2, 5)
                        .addPoint(.25, .1)
                        .addPoint(1, .1)
                        .build())
                .velocity(new RandomDoubleValue(-.2, .2))
                .lookAtPlayerYaw(true)
                .lifeTicks(new StableIntValue(30))
                .textureData(ParticleTextures.QUAD)
                .build();
    }

    public void spawn(final double x, final double y, final double z) {
        particleSystem().spawnParticles(particleData, new PointParticleZone(new V3(x, y, z)), new V3(), 50);
    }

}
