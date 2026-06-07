package cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettingsType {

    CRAFT_VIEW_VISIBILITY("craft_view_visibility"),
    HOT_BAR_WITH_CHAT_VISIBILITY("hb_wchat_visibility"),
    CURSOR_INFORMATION("cursor_info"),
    GLOW_SCREEN_EFFECT("glow_screen_effect"),
    DAMAGE_INDICATOR_OWN_VISIBILITY("damage_indicator_own"),
    DAMAGE_INDICATOR_OTHER_VISIBILITY("damage_indicator_other"),
    PLAYER_VISIBILITY("player_visibility"),
    QUEST_COLUMN_VISIBILITY("quest_column_visibility"),
    BLOOD_EFFECT_VISIBILITY("blood_effect_visibility");

    private final String configKey;

}
