package cupcake.nikky.imperium.tower.character.ineract;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.calendar.CalendarProvider;
import cupcake.nikky.imperium.tower.character.ImperiumCharacter;
import cupcake.nikky.imperium.tower.fraction.FractionChoicer;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumGlowPacket;
import cupcake.nikky.imperium.tower.protocol.packet.ItemAnalyzePacket;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.func.mod.Anime;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum InteractType {

    QUEST_MENU(QuestMenuInteract::new),
    BATTLE_PASS(imperiumCharacter -> new DefaultInteract(imperiumCharacter) {
        @Override
        public void interact(@NotNull Player player) {
            Anime.close(player);
            Fantasy.battlePassProvider().open(player);
        }
    }),
    MINE_TRADER(imperiumCharacter -> new TradeInteract(imperiumCharacter, TradeType.MINE_TRADE)),
    POTION_TRADER(imperiumCharacter -> new TradeInteract(imperiumCharacter, TradeType.POTIONS_TRADE)),
    WAY_TRADER(imperiumCharacter -> new TradeInteract(imperiumCharacter, TradeType.WAY_TRADE)),
    EQUIP_TRADER(imperiumCharacter -> new TradeInteract(imperiumCharacter, TradeType.EQUIP_TRADE)),
    MASK_TRADER(imperiumCharacter -> new TradeInteract(imperiumCharacter, TradeType.MASK_TRADE)),
    FRACTION_SELECTION(character -> new DefaultInteract(character) {
        @Override
        public void interact(@NotNull Player player) {
            new FractionChoicer(player.getBungeePlayer()).open();
        }
    }),
    TREE_SELECTION(imperiumCharacter -> new DefaultInteract(imperiumCharacter) {
        @Override
        public void interact(@NotNull Player player) {
            Anime.close(player);
            Fantasy.treeProvider().openScheme(player);
        }
    }),
    CALENDAR_MENU(imperiumCharacter -> new DefaultInteract(imperiumCharacter) {
        @Override
        public void interact(@NotNull Player player) {
            Anime.close(player);
            CalendarProvider.getInstance().openMenu(player);
            new ImperiumGlowPacket(player, imperiumCharacter.getEntityId()).notice(1);
        }
    }),
    GUIDE_SELECTION(imperiumCharacter -> new DefaultInteract(imperiumCharacter) {
        @Override
        public void interact(@NotNull Player player) {
            Anime.close(player);
            Fantasy.clientProvider().openGuide(player);
        }
    }),
    ITEM_ANALYZE(imperiumCharacter -> new DefaultInteract(imperiumCharacter) {
        @Override
        public void interact(@NotNull Player player) {
            Anime.close(player);
            new ItemAnalyzePacket(player, null).notice(0);
        }
    });

    private final Function<ImperiumCharacter, DefaultInteract> interact;

}
