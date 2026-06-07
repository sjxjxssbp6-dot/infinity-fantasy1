package cupcake.nikky.imperium.tower.entity.monster.shard;

public interface ShardingMonster {

    void setIncreaseItemQuantity(final double quantity);

    void setIncreaseExperience(final double increaseExperience);

    double getIncreaseItemQuantity();

    double getIncreaseExperience();

    void setIncreaseDamage(final double increaseDamage);

    void setIncreaseHealth(final double increaseHealth);

    double getIncreaseHealth();

    double getIncreaseDamage();

    int getCorrupt();

    void setCorrupt(int corrupt);

}
