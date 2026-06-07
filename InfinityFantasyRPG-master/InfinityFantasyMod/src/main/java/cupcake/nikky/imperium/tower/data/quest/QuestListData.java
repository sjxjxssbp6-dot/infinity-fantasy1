package cupcake.nikky.imperium.tower.data.quest;

import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import dev.xdark.clientapi.item.ItemStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestListData {

    private final ItemStack itemStack;
    private final QuestState questState;
    private final boolean canReceive;

}
