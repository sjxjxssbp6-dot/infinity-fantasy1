package cupcake.nikky.imperium.tower.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChatType {

    NONE(""),
    SYSTEM("&7[&cСистема&7]&r"),
    PARTY("&7[&cГруппа&7]&r"),
    GUILD("&7[&cКлан&7]&r"),
    CURRENT_REALM(""),
    NOTIFICATION("&7[&cОповещения&7]&r");

    @Getter
    private final String prefix;

}
