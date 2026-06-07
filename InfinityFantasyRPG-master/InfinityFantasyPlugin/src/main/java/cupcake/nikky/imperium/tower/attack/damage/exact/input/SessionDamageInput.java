package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.entity.monster.Sessional;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class SessionDamageInput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        container.getAttackInfluenceCallback().ifUserPresent(user -> container.getDefender().entityTypeCallback().ifMonsterPresent(monster -> {
            if (monster instanceof Sessional sessional) {
                if (sessional.getSession() != null && !sessional.getSession().getSessionUuid().equals(user.getSession().getSessionUuid()))
                    container.applyDamageFlag(DamageFlag.NOT_AVAILABLE);
            }
        }));
    }

}
