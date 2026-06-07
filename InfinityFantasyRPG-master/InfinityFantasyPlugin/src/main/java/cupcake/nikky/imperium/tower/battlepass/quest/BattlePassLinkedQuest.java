package cupcake.nikky.imperium.tower.battlepass.quest;

public class BattlePassLinkedQuest {

    private final BattlePassQuest[] quests;
    private final int startedId;

    public BattlePassLinkedQuest(BattlePassQuest[] quests) {
        this.quests = quests;
        for (BattlePassQuest quest : quests) {
            quest.setChain(this);
        }

        this.startedId = first().getId();
    }

    public BattlePassQuest first() {
        return quests[0];
    }

    public BattlePassQuest next(final int currentId) {
        var find = currentId - startedId;
        if (find < 0 || find >= quests.length) return null;
        return quests[find];
    }

    public BattlePassQuest nextIndex(final int index) {
        if (index >= quests.length) return null;
        return quests[index];
    }

    public int count() {
        return quests.length;
    }

}
