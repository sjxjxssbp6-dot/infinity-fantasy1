package cupcake.nikky.imperium.tower.particle.exact.blood;

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

public class EntityHeartBloodParticle extends BaseParticle {

    private ParticleData bloodData;

    public EntityHeartBloodParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        this.bloodData = ParticleData.builder()
                .size(new RandomDoubleValue(.1, .15))
                .color(new StableColorValue(Palette.RED))
                .lifeTicks(new RandomIntValue(8, 16))
                .velocityX(new RandomDoubleValue(-0.05, 0.05))
                .velocityY(new RandomDoubleValue(0.05, 0.1))
                .rotationVelocity(new RandomDoubleValue(-10, 10))
                .velocityYCurve(Curve.builder()
                        .addPoint(0, 2)
                        .addPoint(.25, 1)
                        .addPoint(.75, -1.5)
                        .addPoint(1, -3)
                        .build())
                .velocityZ(new RandomDoubleValue(-0.05, 0.05))
                .sizeCurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.5, .75)
                        .addPoint(.75, .50)
                        .addPoint(1, .25)
                        .build())
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.8, 1)
                        .addPoint(1, .25)
                        .build())
                .textureData(ParticleTextures.HEART)
                .lookAtPlayer(true)
                .build();
    }

    public void spawn(final int entityId) {
        val entity = Enginex.getPlayer().getEntityId() == entityId ? Enginex.getPlayer() : Enginex.getMinecraft().getWorld().getEntity(entityId);
        if (entity == null) return;

        particleSystem().spawnParticles(bloodData, new PointParticleZone(new V3(entity.getX(), entity.getY() + entity.getEyeHeight() / 1.75, entity.getZ())), new V3(), 40);
    }

}
