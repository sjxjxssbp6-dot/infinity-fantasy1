package cupcake.nikky.imperium.tower.aura.data;

import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.aura.AuraAnimation;
import ru.cristalix.wada.module.aura.AuraTexture;

public record FantasyAura(String title, AuraTexture texture, AuraAnimation animation, AuraRequirement auraRequirement) {

    public FantasyAura(@NotNull final String title) {
        this(title, null, null, AuraRequirement.of(player -> true));
    }

    public String resourceLocation() {
        return "wada:aura/" + texture.getHash();
    }

}
