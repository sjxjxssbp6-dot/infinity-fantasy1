package cupcake.nikky.imperium.tower.command.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class HelpCommandListener implements DefaultListener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTypeHelp(PlayerCommandPreprocessEvent event) {
        val player = event.getPlayer();

        if (event.getMessage().equals("/help")) {
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/deposit [gran, firm] [игрок] [кол-во Гран/Фирм-монет] &f- перевести Граны/Фирм-монеты другому игроку");
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/suicide &f- преждевременная смерть");
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/donate &f- магазин специальных предложений");
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/spawn &f- меню перемещений");
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/realm [айди] &f- перемещение между реалмами");
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/trade [игрок] &f- предложить обмен");
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/dnd &f- включить/выключить режим `Не беспокоить` (распространяется на запросы предложения обмена)");
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/hide &f- скрыть/показать всех игроков");

            if (PermissionUtility.isAttentiveRekt(player.getUniqueId())) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/rekt &f- эксклюзивные вещи на текущий уровень");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/rektfarm &f- перемещение в особую зону");
            }

            if (Fantasy.gameServer().isTest()) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cКоманды тестового сервера:");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/id &f- посмотреть айди предмета в руке");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/get [айди] [кол-во] &f- получить предмет");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/view [игрок] [inventory/storage] &f- просмотр инвентаря или хранилища другого игрока");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/cleardata &f- вайп ваших данных (кроме предметов в инвентаре и хранилище)");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/fraction [drop, time] &f- сбросить фракцию силы, открыть меню выбора фракции");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/gran [значение] &f- добавить Гран на счет");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/mirn [значение] &f- добавить Мирн на счет");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/quest [id] &f- пометить конкретное задание как выполненное");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/exp [значение] &f- добавить опыта");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/level [значение] &f- установить уровень");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/tool [значение] &f- добавить опыт инструменту");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/reset [battlepass, premium, incubator, lootfilter, quest, trades, buff, storage, aura, mount] &f- сбросить конкретные данные");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/items [page] &f- открыть меню со всеми игровыми предметами");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/sub [time in seconds] &f- установить премиум подписку на определенное время");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/incubator &f- выполнить все условия для инкубаторов");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/gear &f- готовые вещи");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/achievements &f- получить все достижения");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/shard [уровень] &f- изменить уровень осколка прорыва");
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6/edgecore [число] &f- получить усиление ядра Грани");
            }

            event.setCancelled(true);
        }
    }

}
