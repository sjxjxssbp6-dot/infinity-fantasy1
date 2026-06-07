package cupcake.nikky.imperium.tower.client.protocol.analyze;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemAnalyzeContainer {

    private String title;
    private int tiers, maxBaseLevel, sumWeights;
    private ItemAnalyzeType type;

    private List<ItemAnalyze> analyzes;

}
