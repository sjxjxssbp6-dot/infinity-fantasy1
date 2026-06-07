package cupcake.nikky.imperium.tower.attack.damage;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.Attributable;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ParticipantCallback {

    private final LivingEntity participant;
    private boolean user, monster;

    public ParticipantCallback ifAttributablePresent(@NotNull final Consumer<Attributable> attributableConsumer) {
        this.ifUserPresent(attributableConsumer::accept).ifMonsterPresent(attributableConsumer::accept);
        return this;
    }

    public ParticipantCallback ifUserPresent(@NotNull final Consumer<User> userConsumer) {
        if (monster || !isPresent()) return this;

        val user = Fantasy.userService().findById(participant.getUniqueId()).orElse(null);

        if (user != null) {
            userConsumer.accept(user);
            this.user = true;
        }

        return this;
    }

    public ParticipantCallback ifMonsterPresent(@NotNull final Consumer<Monster> monsterConsumer) {
        if (user || !isPresent()) return this;

        val monster = Fantasy.monsterProvider().findByUUID(participant.getUniqueId()).orElse(null);

        if (monster != null) {
            monsterConsumer.accept(monster);
            this.monster = true;
        }

        return this;
    }

    public boolean isPresent() {
        return participant != null;
    }

}
