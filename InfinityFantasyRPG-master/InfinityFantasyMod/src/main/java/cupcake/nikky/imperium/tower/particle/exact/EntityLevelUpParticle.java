package cupcake.nikky.imperium.tower.particle.exact;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.values.RandomDoubleValue;
import gg.cristalix.particle.system.values.RandomIntValue;
import gg.cristalix.particle.system.values.StableColorValue;
import gg.cristalix.particle.system.values.StableDoubleValue;
import gg.cristalix.particle.system.zone.CubeParticleZone;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

public class EntityLevelUpParticle extends BaseParticle {

    private ParticleData data;

    public EntityLevelUpParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        this.data = ParticleData.builder()
                .color(new StableColorValue(Palette.YELLOW_LIGHT))
                .colorACurve(
                        Curve.builder()
                                .addPoint(0, 1)
                                .addPoint(0.75, 0.5)
                                .addPoint(1, 0)
                                .build()
                )
                .sizeX(new StableDoubleValue(0.2))
                .sizeZ(new StableDoubleValue(0.2))
                .sizeY(new RandomDoubleValue(3D, 4D))
                .sizeYCurve(
                        Curve.builder()
                                .addPoint(0, 0)
                                .addPoint(0.3, 1)
                                .addPoint(1, 1)
                                .build()
                )
                .lookAtPlayerYaw(true)
                .lifeTicks(new RandomIntValue(20, 40))
                .textureData(ParticleTextures.FADE_DOWN_UP)
                .build();
    }

    public void spawn(final int entityId) {
        val entity = Enginex.getPlayer().getEntityId() == entityId ? Enginex.getPlayer() : Enginex.getMinecraft().getWorld().getEntity(entityId);
        if (entity == null) return;

        particleSystem().spawnParticles(data, new CubeParticleZone(new V3(1, 0, 1)), new V3(entity.getX(), entity.getY(), entity.getZ()), 40);
    }

}
