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

public class EntityHealParticle extends BaseParticle {

    private ParticleData data;

    public EntityHealParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        this.data = ParticleData.builder()
                .color(new RandomPatternColorValue(Palette.RED, Palette.RED_LIGHT, Palette.RED_MIDDLE, Palette.RED_DARK))
                .size(new RandomDoubleValue(.2, .4))
                .colorACurve(
                        Curve.builder()
                                .addPoint(0, 1)
                                .addPoint(0.75, 0.5)
                                .addPoint(1, 0)
                                .build()
                )
                .velocityY(new RandomDoubleValue(.02, .04))
                .sizeYCurve(
                        Curve.builder()
                                .addPoint(0, 1)
                                .addPoint(0.3, 1)
                                .addPoint(0.75, .6)
                                .addPoint(1, .2)
                                .build()
                )
                .lookAtPlayerYaw(true)
                .lifeTicks(new RandomIntValue(30, 40))
                .textureData(ParticleTextures.BUBBLE)
                .build();
    }

    public void spawn(final int entityId) {
        val entity = Enginex.getPlayer().getEntityId() == entityId ? Enginex.getPlayer() : Enginex.getMinecraft().getWorld().getEntity(entityId);
        if (entity == null) return;

        particleSystem().spawnParticles(data, new CubeParticleZone(new V3(1.25, 0, 1.25)), new V3(entity.getX(), entity.getY() + entity.getEyeHeight() / 2.5D, entity.getZ()), 20);
    }

}
