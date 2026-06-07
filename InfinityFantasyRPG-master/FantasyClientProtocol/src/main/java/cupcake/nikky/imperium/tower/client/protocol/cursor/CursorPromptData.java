package cupcake.nikky.imperium.tower.client.protocol.cursor;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CursorPromptData {

    private String text;
    private String texture;
    private UUID uuid;

    public CursorPromptData(String text, String texture) {
        this.text = text;
        this.texture = texture;
        this.uuid = UUID.randomUUID();
    }

}
