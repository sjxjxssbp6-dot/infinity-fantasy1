package cupcake.nikky.imperium.tower.client.protocol.analyze;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemAnalyze {

    private double min, max;
    private double amplitude;
    private int tier, baseLevel, weight;

}
