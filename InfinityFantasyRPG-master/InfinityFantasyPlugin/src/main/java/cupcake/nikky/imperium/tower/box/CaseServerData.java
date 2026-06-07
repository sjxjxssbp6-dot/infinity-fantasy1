package cupcake.nikky.imperium.tower.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import cupcake.nikky.imperium.tower.rng.exact.box.CaseWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CaseServerData {

    private final CaseData caseData;
    private final WeightPool<CaseWrapper<?>> weightPool;

}
