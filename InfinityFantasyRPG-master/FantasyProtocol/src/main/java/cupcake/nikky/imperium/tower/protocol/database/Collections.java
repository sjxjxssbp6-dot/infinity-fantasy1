package cupcake.nikky.imperium.tower.protocol.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Collections {

    USERS("users"),
    BANNED_USERS("bans"),
    DONATE_LOGS("donate_logs"),
    MONSTER_SPAWN_LOCATION("monster_spawn");

    private final String collectionName;

}
