package cupcake.nikky.imperium.tower.protocol.board;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTimeData {

    private UUID uuid;
    private TimeType timeType;
    private int time;
    private int level;
    private int corrupt;
    private double score;

}
