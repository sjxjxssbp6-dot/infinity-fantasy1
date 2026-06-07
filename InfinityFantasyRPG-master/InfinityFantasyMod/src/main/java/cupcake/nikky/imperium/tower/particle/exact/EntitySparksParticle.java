package cupcake.nikky.imperium.tower.particle.exact;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.values.*;
import gg.cristalix.particle.system.zone.CubeParticleZone;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class EntitySparksParticle extends BaseParticle {

    public EntitySparksParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {

    }

    public void spawn(final int entityId, final double radius) {
        val entity = Enginex.getPlayer().getEntityId() == entityId ? Enginex.getPlayer() : Enginex.getMinecraft().getWorld().getEntity(entityId);
        if (entity == null) return;

        val particleData = ParticleData.builder()
                .color(new RandomPatternColorValue(Palette.RED_LIGHT, Palette.RED, Palette.RED_MIDDLE, Palette.RED_DARK))
                .size(new StableDoubleValue(.1))
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.5, 1)
                        .addPoint(0.55, .5)
                        .addPoint(1, 0)
                        .build()
                )
                .velocityYCurve(Curve.builder()
                        .addPoint(0, 5)
                        .addPoint(.2, 5)
                        .addPoint(.25, .1)
                        .addPoint(1, .1)
                        .build())
                .velocityY(new RandomDoubleValue(radius / 40D, radius / 20D))
                .lookAtPlayerYaw(true)
                .lifeTicks(new StableIntValue(20))
                .textureData(ParticleTextures.QUAD)
                .build();

        particleSystem().spawnParticles(particleData, new CubeParticleZone(new V3(radius, .1, radius)), new V3(entity.getX(), entity.getY(), entity.getZ()), 75);
    }

}
