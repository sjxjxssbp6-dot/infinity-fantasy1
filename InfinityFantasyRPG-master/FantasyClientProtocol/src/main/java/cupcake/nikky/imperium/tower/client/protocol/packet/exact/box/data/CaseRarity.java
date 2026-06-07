package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaseRarity {

    COMMON("&f", -100), RARE("&9", -10), EPIC("&5", -1), UNIQUE("&6", 1), EDGE("&4", 10);

    private final String color;
    private final int order;

}
