package cupcake.nikky.imperium.tower.box;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class CaseSessionData {

    private final CaseType caseType;
    @Setter private int currentAmount = 1;

}
