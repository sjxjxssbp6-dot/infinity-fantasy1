package cupcake.nikky.imperium.tower.character;

import cupcake.nikky.imperium.tower.character.ineract.InteractType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class SimpleCharacter {

    private int id;
    private InteractType interactType;
    @Builder.Default
    private String name = "Unknown";
    @Builder.Default
    private String texture = "";
    @Builder.Default
    private String signature = "";
    @Builder.Default
    private String url = "";
    @Builder.Default
    private Set<UUID> personalization = new HashSet<>();

    public ImperiumCharacter createCharacterInstance() {
        val result = new ImperiumCharacter(id, personalization){};
        if (interactType != null)
            result.setInteracting(interactType.getInteract().apply(result));

        result.getData().setName(TextUtility.colorize(name));
        result.getData().setSkinValue(texture);
        result.getData().setSkinSignature(signature);
        result.getData().setSkinUrl(url);
        return result;
    }

}
