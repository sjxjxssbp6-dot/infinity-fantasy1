package cupcake.nikky.imperium.tower.particle;

import gg.cristalix.particle.system.ParticleSystem;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.Container;
import ru.cristalix.enginex.renderer.contexts.IContext;

@Getter
@Accessors(fluent = true)
public abstract class BaseParticle {

    private final ParticleSystem particleSystem;

    protected BaseParticle(@NotNull final ParticleSystem particleSystem, @NotNull final Container container) {
        this.particleSystem = particleSystem;
        this.initialize();
        container.addChild(particleSystem());
    }

    protected abstract void initialize();

}
