package cupcake.nikky.imperium.tower.data.buff;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.buff.BuffCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
@AllArgsConstructor
public class BuffData {

    private final int id;
    private BuffCategory buffCategory;
    private int stack;
    private int duration;
    private final FantasyImage buffImage;
    private final String title;
    private String[] description;

}
