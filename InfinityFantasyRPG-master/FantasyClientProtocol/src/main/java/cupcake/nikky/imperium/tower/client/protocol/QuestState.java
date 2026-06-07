package cupcake.nikky.imperium.tower.client.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestState {

    NOT_RECEIVE(0),
    RECEIVE(1),
    COMPLETE_WITHOUT_PASS(2),
    PASS(3);

    private final int state;

    public static QuestState of(final int state) {
        for (QuestState value : values()) {
            if (value.getState() == state) return value;
        }

        return null;
    }

}
