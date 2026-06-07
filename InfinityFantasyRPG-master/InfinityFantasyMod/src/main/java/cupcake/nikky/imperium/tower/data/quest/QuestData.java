package cupcake.nikky.imperium.tower.data.quest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public final class QuestData {

    private final String title;
    private final int id;
    @Setter private String[] description;
    private final boolean isDaily;
    private boolean hide;

    public static QuestData of(@NotNull final String title, final int id, @NotNull final String... description) {
        return new QuestData(title, id, Stream.of(description).flatMap(s -> Stream.of(s.split("\n"))).toArray(String[]::new), title.contains("*"), false);
    }

    public static QuestData of(@NotNull final String title, final int id, @NotNull final String description) {
        return new QuestData(title, id, description.split("\n"), title.contains("*"), false);
    }

    public QuestData setHide(boolean hide) {
        this.hide = hide;
        return this;
    }

}
