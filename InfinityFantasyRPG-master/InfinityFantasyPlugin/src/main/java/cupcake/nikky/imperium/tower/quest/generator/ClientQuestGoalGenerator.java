package cupcake.nikky.imperium.tower.quest.generator;

import cupcake.nikky.imperium.tower.quest.Quest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ClientQuestGoalGenerator implements ClientQuestGoal {

    private final Quest quest;

    @Override
    public List<String> generateClientDescription() {
        final ArrayList<String> description = new ArrayList<>();
        quest.getRequirements().forEach(questRequirement -> description.addAll(questRequirement.goalDescription()));
        return description;
    }

}
