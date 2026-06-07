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
import gg.cristalix.particle.system.zone.PointParticleZone;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Color;
import ru.cristalix.enginex.util.V3;

import java.util.concurrent.TimeUnit;

public class BinaEffect extends BaseParticle {

    public BinaEffect() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {

    }

    public void spawn(@NotNull final Color color, final int durationTicks, final double x, final double y, final double z) {
        val quantum = ParticleData.builder()
                .size(new StableDoubleValue(.15))
                .color(new StableColorValue(color))
                .velocityCurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(1, 5)
                        .build())
                .velocityY(new StableDoubleValue(.15))
                .sizeCurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.8, 1)
                        .addPoint(1, 0)
                        .build())
                .lifeTicks(new StableIntValue(10))
                .lookAtPlayer(true)
                .textureData(ParticleTextures.QUAD)
                .build();
        val lineUp = ParticleData.builder()
                .sizeX(new StableDoubleValue(.75))
                .sizeZ(new StableDoubleValue(.75))
                .sizeY(new StableDoubleValue(8))
                .color(new StableColorValue(color.alpha(.75)))
                .sizeCurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.95, 1)
                        .addPoint(1, 0)
                        .build())
                .lifeTicks(new StableIntValue(durationTicks))
                .lookAtPlayerYaw(true)
                .textureData(ParticleTextures.QUAD)
                .build();

        particleSystem().spawnParticles(lineUp, new PointParticleZone(new V3(x, y, z)), new V3(), 1);

        val task = Enginex.getClientApi().syncExecutorService().scheduleAtFixedRate(() -> {
            particleSystem().spawnParticles(quantum, new CubeParticleZone(new V3(1, 1, 1)), new V3(x, y, z), 5);
        }, 0, 50, TimeUnit.MILLISECONDS);

        Enginex.getClientApi().syncExecutorService().schedule(() -> {
            task.cancel(true);
        }, durationTicks * 50L, TimeUnit.MILLISECONDS);
    }

}
