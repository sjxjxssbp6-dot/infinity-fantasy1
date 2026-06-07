package cupcake.nikky.imperium.tower.particle.exact;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.values.*;
import gg.cristalix.particle.system.zone.PointParticleZone;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class EntityIceExplosionParticle extends BaseParticle {

    public EntityIceExplosionParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {

    }

    public void spawn(final int entityId, final double radius) {
        val entity = Enginex.getPlayer().getEntityId() == entityId ? Enginex.getPlayer() : Enginex.getMinecraft().getWorld().getEntity(entityId);
        if (entity == null) return;

        val particleData = ParticleData.builder()
                .color(new StableColorValue(Palette.BLUE_LIGHT))
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
                .velocity(new RandomDoubleValue(-radius / 20D, radius / 20D))
                .lookAtPlayerYaw(true)
                .lifeTicks(new StableIntValue(20))
                .textureData(ParticleTextures.QUAD)
                .build();

        particleSystem().spawnParticles(particleData, new PointParticleZone(new V3(entity.getX(), entity.getY() + entity.getEyeHeight() / 1.75, entity.getZ())), new V3(), 75);
    }

}
