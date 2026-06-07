package cupcake.nikky.imperium.tower.incubator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor(staticName = "create")
public class IncubatorRequirement {

    private int deathRequirement;
    private int levelRequirement;

}
