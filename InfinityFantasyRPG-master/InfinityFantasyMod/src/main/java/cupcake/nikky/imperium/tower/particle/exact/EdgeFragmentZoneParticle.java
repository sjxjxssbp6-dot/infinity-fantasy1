package cupcake.nikky.imperium.tower.particle.exact;

import cupcake.nikky.imperium.tower.client.protocol.particle.ParticleZoneData;
import cupcake.nikky.imperium.tower.particle.BaseParticle;
import dev.xdark.clientapi.event.lifecycle.GameTickPre;
import gg.cristalix.particle.system.Curve;
import gg.cristalix.particle.system.ParticleData;
import gg.cristalix.particle.system.ParticleSystem;
import gg.cristalix.particle.system.texture.ParticleTextures;
import gg.cristalix.particle.system.texture.RandomTexture;
import gg.cristalix.particle.system.values.RandomDoubleValue;
import gg.cristalix.particle.system.values.RandomIntValue;
import gg.cristalix.particle.system.values.StableColorValue;
import gg.cristalix.particle.system.values.StableDoubleValue;
import gg.cristalix.particle.system.zone.CubeParticleZone;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.renderer.contexts.WorldContext;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.V3;

import java.util.ArrayList;
import java.util.List;

public class EdgeFragmentZoneParticle extends BaseParticle {

    private final List<ParticleZoneData> zones = new ArrayList<>();
    private long cooldown;

    public EdgeFragmentZoneParticle() {
        super(new ParticleSystem(), WorldContext.get());
    }

    @Override
    protected void initialize() {
        val particleData = ParticleData.builder()
                .size(new StableDoubleValue(0.1))
                .velocityY(new RandomDoubleValue(-0.03, -0.02))
                .colorACurve(
                        Curve.builder()
                                .addPoint(0, 0)
                                .addPoint(0.2, 1)
                                .addPoint(0.8, 1)
                                .addPoint(1, 0)
                                .build()
                )
                .rotationZ(new RandomDoubleValue(0, 360))
                .rotationZVelocity(new RandomDoubleValue(-2, 2))
                .rotationZVelocityCurve(
                        Curve.builder()
                                .addPoint(0, 1)
                                .addPoint(0.5, -1)
                                .addPoint(1, 1)
                                .build()
                )
                .lookAtPlayer(true)
                .lifeTicks(new RandomIntValue(100, 160))
                .color(new StableColorValue(Palette.BLACK))
                .textureStorage(new RandomTexture(
                        ParticleTextures.SNOW_0,
                        ParticleTextures.SNOW_1,
                        ParticleTextures.SNOW_2
                ))
                .build();

        Enginex.registerEvent(GameTickPre.class, event -> {
            if (System.currentTimeMillis() < cooldown) return;

            val playerPos = V3.playerPos;
            for (ParticleZoneData zone : zones) {
                if (new V3(zone.getXCenter(), zone.getYCenter(), zone.getZCenter()).squaredDistance(new V3(playerPos.getX(), playerPos.getY(), playerPos.getZ())) <= 128 * 128) {
                    particleSystem().spawnParticles(particleData, new CubeParticleZone(new V3(24, 16, 24)), playerPos.clone().add(0, 6, 0), 6);
                    return;
                }
            }

            cooldown = System.currentTimeMillis() + 3_000;
        });
    }

    public void addZone(@NotNull final ParticleZoneData zone) {
        this.zones.add(zone);
    }

}
