package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaseCurrency {

    GRAN("\uE0FE"),
    SPECIAL("\uE104"),
    MINES("\uE106");

    private final String icon;

}
