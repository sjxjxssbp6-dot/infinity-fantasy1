package cupcake.nikky.imperium.tower.entity.monster.target;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumTargetEntityPacket;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@RequiredArgsConstructor
public class TargetBarData {

    private final Set<UUID> users = new CopyOnWriteArraySet<>();
    @Getter private final Monster monster;

    public boolean isSubscribed(@NotNull final UUID userUuid) {
        return users.contains(userUuid);
    }

    public void subscribe(@NotNull final UUID userUuid) {
        this.users.add(userUuid);

        Fantasy.userService().findById(userUuid).ifPresent(user -> {
            if (user.getTargetEntity().updateTarget(this)) {
                this.sendRegister(user);
            } else this.update(userUuid);
        });

    }

    public void unsubscribe(@NotNull final UUID userUuid) {
        this.users.remove(userUuid);

        Fantasy.userService().findById(userUuid).ifPresent(user -> {
            if (user.getTargetEntity().updateTarget(null)) {
                this.sendUpdate(user);
            }
        });
    }

    public void unsubscribeAll() {
        getUsers().forEach(uuid -> Fantasy.userService().findById(uuid).ifPresent(user -> {
            val targetMonster = user.getTargetEntity().getTargetMonster();
            if (targetMonster == null || !getMonster().getMonsterUniqueId().equals(targetMonster.getMonsterUniqueId()))
                return;

            if (user.getTargetEntity().updateTarget(null)) {
                this.sendUpdate(user);
            }
        }));

        this.users.clear();
    }

    public void update(@NotNull final UUID uuid) {
        Fantasy.userService().findById(uuid).ifPresent(user -> {
            if (!user.isLoaded() || user.isInvalidated()) return;

            val targetEntity = user.getTargetEntity();
            if (targetEntity.isEmpty() || getMonster() == null) return;

            val targetMonster = targetEntity.getTargetMonster();
            if (targetMonster == null || !targetEntity.getTargetMonster().isExist() || !targetMonster.getMonsterUniqueId().equals(getMonster().getMonsterUniqueId())) return;

            this.sendUpdate(user);
        });
    }

    public void updateAll() {
        getUsers().forEach(this::update);
    }

    public List<UUID> getUsers() {
        return new ArrayList<>(users);
    }

    private void sendRegister(@NotNull final User user) {
        if (!user.isLoaded()) return;
        new ImperiumTargetEntityPacket(user, getMonster()).notice(0);
    }

    private void sendUpdate(@NotNull final User user) {
        if (!user.isLoaded()) return;
        new ImperiumTargetEntityPacket(user, getMonster()).notice(1);
    }

}
