package cupcake.nikky.imperium.tower.particle.exact;

import cupcake.nikky.imperium.tower.particle.BaseParticle;
import dev.xdark.clientapi.entity.Entity;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.values.*;
import gg.cristalix.particle.system.zone.CubeParticleZone;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class EntityAilmentParticle extends BaseParticle {

    private final ParticleData particleData;
    private final Int2LongOpenHashMap entities;
    private final Set<Integer> entitiesToRemove;

    public EntityAilmentParticle(@NotNull final Palette particlePalette) {
        super(new ParticleSystem(), WorldContext.get());
        this.entities = new Int2LongOpenHashMap();
        this.entitiesToRemove = new HashSet<>();

        this.particleData = ParticleData.builder()
                .sizeX(new StableDoubleValue(.1))
                .sizeY(new StableDoubleValue(.1))
                .sizeZ(new StableDoubleValue(.1))
                .velocityY(new RandomDoubleValue(.02, .06))
                .color(new StableColorValue(particlePalette))
                .textureData(ParticleTextures.QUAD)
                .lifeTicks(new RandomIntValue(10, 20))
                .lookAtPlayer(true)
                .colorACurve(Curve.builder()
                        .addPoint(0, 1)
                        .addPoint(.5, .75)
                        .addPoint(.75, .5)
                        .addPoint(1, 0)
                        .build())
                .build();
    }

    @Override
    protected void initialize() {
        particleSystem().onPreTick(element -> {
            if (entities.isEmpty()) return;

            entities.forEach((entityId, whenReset) -> {
                final Entity entity = Enginex.getPlayer().getEntityId() == entityId ? Enginex.getPlayer() : Enginex.getMinecraft().getWorld().getEntity(entityId);
                if (entity == null) {
                    entitiesToRemove.add(entityId);
                    return;
                }

                particleSystem().spawnParticles(
                        particleData,
                        new CubeParticleZone(new V3(1, 1, 1)),
                        new V3(entity.getX(), entity.getY(), entity.getZ()), 2
                );

                if (whenReset <= Instant.now().toEpochMilli())
                    entitiesToRemove.add(entity.getEntityId());
            });

            entitiesToRemove.removeIf(entityId -> {
                entities.remove(entityId);
                return true;
            });
        });
    }

    public void spawn(final int entityId, final long whenReset) {
        entities.put(entityId, whenReset);
    }

}
