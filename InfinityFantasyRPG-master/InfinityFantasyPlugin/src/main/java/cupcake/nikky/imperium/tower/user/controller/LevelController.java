package cupcake.nikky.imperium.tower.user.controller;

import com.google.common.util.concurrent.AtomicDouble;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.tab.ITabService;
import ru.cristalix.wada.util.Color;

import java.util.concurrent.atomic.AtomicInteger;

public final class LevelController {

    private final User user;

    private final AtomicInteger level = new AtomicInteger(1);
    private final AtomicDouble exp = new AtomicDouble();
    private final AtomicDouble battleMultiplier = new AtomicDouble(1D);

    public LevelController(@NotNull final User user, final int level, final double exp) {
        this.user = user;
        this.level.set(level);
        this.exp.set(exp);
    }

    public double changeExp(final double delta, @NotNull final ExpType expType) {
        if (this.isMaximumLevel()) return 0D;

        boolean isLevelUp = false;
        val isContainsWeak = Fantasy.buffService().findById(user.getUniqueId()).map(buffedEntity -> buffedEntity.find(BuffType.WEAK_EXPERIENCE_BUFF).isPresent()).orElse(false);

        double result = delta * (isContainsWeak && expType == ExpType.BATTLE ? .75 : 1D);
        result *= (expType == ExpType.BATTLE ? battleMultiplier.get() : 1D);

        for (;;) {
            val oldExp = this.getExp();
            if (this.exp.compareAndSet(oldExp, Math.max(oldExp + result, 0))) break;
        }

        int oldLevel = this.getLevel();
        double oldExp = this.getExp();

        double requirement = LevelStorage.getInstance().getBasicRequirementToNextLevel(oldLevel);

        if (requirement != -1 && oldExp >= requirement) {
            for (;;) {
                if (oldExp >= requirement) {
                    if (this.level.compareAndSet(oldLevel, oldLevel + 1)) {
                        isLevelUp = true;
                        break;
                    }
                    oldLevel = this.getLevel();
                    oldExp = this.getExp();
                    requirement = LevelStorage.getInstance().getBasicRequirementToNextLevel(oldLevel);
                } else break;
            }

            for (;;) {
                if (oldExp < requirement) break;
                if (this.exp.compareAndSet(oldExp, oldExp - requirement)) break;
                oldLevel = this.getLevel();
                oldExp = this.getExp();
                requirement = LevelStorage.getInstance().getBasicRequirementToNextLevel(oldLevel);
            }

            if (this.getExp() > LevelStorage.getInstance().getBasicRequirementToNextLevel(getLevel())) this.changeExp(0, ExpType.BATTLE);
        } else if (requirement == -1) {
            this.setExp(0);
        }

        if (isLevelUp) {
            var player = user.getSpigot();
            Fantasy.clientProvider().hotbarHealth(user);
            Fantasy.clientProvider().hotbarLevel(user);

            Fantasy.bukkitProvider().sound(player.getLocation(), AdvancedSounds.LEVEL_UP);
            Fantasy.clientProvider().vignette(player, .1, Color.YELLOW);

            Fantasy.clientProvider().levelUpTitle(user, getLevel());
            Fantasy.clientProvider().hotbarMana(user);
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6Поздравляем! Вы повысили свой уровень до &a" + (oldLevel + 1));

            Fantasy.clientProvider().sendTreeIndicator(user);
            Fantasy.tradeNpcService().findByUuid(player.getUniqueId()).invalidateAllByNPCId(NPCs.TRADER_6.getMask().getNpcId());

            CoreApi.get().getService(ITabService.class).update(player);

            user.changeHealth(user.findAttribute(ImperiumAttribute.HEALTH).findValue());
            user.getManaController().changeMana(user.findAttribute(ImperiumAttribute.MANA).findValue());

            if (this.getLevel() == LevelStorage.getInstance().getStartShardEdgeLevel()) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВлияние Грани на Вас усиливается! С этого момента ваши сопротивления &cуменьшены &fна &615%&f!");
            }

            Fantasy.particleProvider().sendEntityLevelUp(player, player.getEntityId());
        } else if (delta != 0D) {
            Fantasy.clientProvider().hotbarLevel(user);
        }

        return result;
    }

    public boolean isMaximumLevel() {
        return LevelStorage.getInstance().getMaxBasicLevel() == getLevel();
    }

    public double getExp() {
        return exp.get();
    }

    public void setExp(final double exp) {
        this.exp.set(Math.max(exp, 0));
        Fantasy.clientProvider().hotbarLevel(user);
    }

    public void setLevel(final int level) {
        this.level.set(Math.max(level, 1));

        Fantasy.clientProvider().hotbarMana(user);
        Fantasy.clientProvider().hotbarLevel(user);
    }

    public int getLevel() {
        return level.get();
    }

    public double getBattleMultiplier() {
        return battleMultiplier.get();
    }

    public void setBattleMultiplier(final double multiplier) {
        this.battleMultiplier.set(multiplier);
    }

    public void changeBattleMultiplier(final double delta) {
        for (;;) {
            val old = this.battleMultiplier.get();
            if (this.battleMultiplier.compareAndSet(old, old + delta)) break;
        }
    }

}
