package cupcake.nikky.imperium.tower.particle.exact.blood;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.values.RandomDoubleValue;
import gg.cristalix.particle.system.values.RandomIntValue;
import gg.cristalix.particle.system.values.StableColorValue;
import gg.cristalix.particle.system.zone.PointParticleZone;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class EntityBaseBloodParticle extends BaseParticle {

    private ParticleData bloodData;

    public EntityBaseBloodParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        this.bloodData = ParticleData.builder()
                .size(new RandomDoubleValue(.08, .1))
                .color(new StableColorValue(Palette.RED_DARK))
                .lifeTicks(new RandomIntValue(8, 16))
                .velocityX(new RandomDoubleValue(-0.05, 0.05))
                .velocityY(new RandomDoubleValue(0.05, 0.1))
                .velocityYCurve(Curve.builder()
                        .addPoint(0, 2)
                        .addPoint(.25, 1)
                        .addPoint(.75, -1.5)
                        .addPoint(1, -3)
                        .build())
                .velocityZ(new RandomDoubleValue(-0.05, 0.05))
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.8, 1)
                        .addPoint(1, .25)
                        .build())
                .textureData(ParticleTextures.QUAD)
                .lookAtPlayer(true)
                .build();
    }

    public void spawn(final int entityId) {
        val entity = Enginex.getPlayer().getEntityId() == entityId ? Enginex.getPlayer() : Enginex.getMinecraft().getWorld().getEntity(entityId);
        if (entity == null) return;

        particleSystem().spawnParticles(bloodData, new PointParticleZone(new V3(entity.getX(), entity.getY() + entity.getEyeHeight() / 1.75, entity.getZ())), new V3(), 40);
    }

}
