package cupcake.nikky.imperium.tower.item.exact.aspect;

import cupcake.nikky.imperium.tower.attack.event.FantasyDamageEvent;
import cupcake.nikky.imperium.tower.entity.monster.event.AsyncFantasyMonsterDeath;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectInboundDamageEvent;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectMonsterKillEvent;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectOutboundDamageEvent;
import cupcake.nikky.imperium.tower.utility.AspectUtility;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class AspectListener implements DefaultListener {

    @EventHandler(priority = EventPriority.LOW)
    public void onAspectDamage(FantasyDamageEvent event) {
        val container = event.getDamageContainer();
        if (container == null) return;

        container.getAttacker().entityTypeCallback()
                .ifUserPresent(user -> container.getDefender().entityTypeCallback().ifMonsterPresent(monster ->
                        AspectUtility.findAspectEvents(user, AspectOutboundDamageEvent.class).forEach(pairedEvent -> {
                            val eventHandler = pairedEvent.getSecond();
                            val item = pairedEvent.getFirst();
                            val aspect = item.getAspectSettings().aspect();
                            val level = item.getAspectSettings().aspectLevel();

                            val aspectEvent = AspectOutboundDamageEvent.create(monster, user, level);
                            if (!aspect.aspectFilter(AspectOutboundDamageEvent.class, aspectEvent)) return;

                            if (aspect.costData().getCostProcessType().getProcess().apply(user, user.getAttributes(), aspect.costData().getCostValue().apply(level)))
                                eventHandler.accept(aspectEvent);
                        })))
                .ifMonsterPresent(monster -> container.getDefender().entityTypeCallback().ifUserPresent(user ->
                        AspectUtility.findAspectEvents(user, AspectInboundDamageEvent.class).forEach(pairedEvent -> {
                            val eventHandler = pairedEvent.getSecond();
                            val item = pairedEvent.getFirst();
                            val aspect = item.getAspectSettings().aspect();
                            val level = item.getAspectSettings().aspectLevel();

                            val aspectEvent = AspectInboundDamageEvent.create(monster, user, level);
                            if (!aspect.aspectFilter(AspectInboundDamageEvent.class, aspectEvent)) return;

                            if (aspect.costData().getCostProcessType().getProcess().apply(user, user.getAttributes(), aspect.costData().getCostValue().apply(level)))
                                eventHandler.accept(aspectEvent);
                        })));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAspectDeath(AsyncFantasyMonsterDeath event) {
        val container = event.getLastDamageContainer();
        val monster = event.getMonster();
        if (container == null) return;

        container.getAttacker().entityTypeCallback().ifUserPresent(user ->
                AspectUtility.findAspectEvents(user, AspectMonsterKillEvent.class).forEach(pairedEvent -> {
                    val eventHandler = pairedEvent.getSecond();
                    val item = pairedEvent.getFirst();
                    val aspect = item.getAspectSettings().aspect();
                    val level = item.getAspectSettings().aspectLevel();

                    val aspectEvent = AspectMonsterKillEvent.create(monster, user, level);
                    if (!aspect.aspectFilter(AspectMonsterKillEvent.class, aspectEvent)) return;

                    if (aspect.costData().getCostProcessType().getProcess().apply(user, user.getAttributes(), aspect.costData().getCostValue().apply(level)))
                        eventHandler.accept(aspectEvent);
                })
        );
    }

}
