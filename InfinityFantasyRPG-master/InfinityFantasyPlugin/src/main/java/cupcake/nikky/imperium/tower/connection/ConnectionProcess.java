package cupcake.nikky.imperium.tower.connection;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.box.world.CaseWorldProvider;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.character.CharacterProvider;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.particle.ParticleZoneData;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.protocol.packet.*;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionState;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionUpdatePacket;
import cupcake.nikky.imperium.tower.resourcepack.ResourcePack;
import cupcake.nikky.imperium.tower.quest.requirement.exact.BlockInteractRequirement;
import cupcake.nikky.imperium.tower.user.FantasyUser;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import cupcake.nikky.imperium.tower.zone.Zone;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import me.func.mod.conversation.ModLoader;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.aura.Aura;
import ru.cristalix.wada.module.aura.AuraAnimation;
import ru.cristalix.wada.module.aura.AuraTexture;
import ru.cristalix.wada.module.worldtext.WorldText;
import ru.cristalix.wada.util.Color;

import java.util.Arrays;

@RequiredArgsConstructor
public class ConnectionProcess {

    private final User user;
    private static final String[] rektMessages = new String[] {
            "&6Разгадывающий тайны и загадки игрового мира %s &6на месте",
            "&6Внимание! Придержите свое золото, %s &6зашел на сервер!",
            "&6Смертельный %s &6явился забрать чью-то душу",
            "&6Пришел %s &6- сила и мудрость в одном лице!",
            "&6Судья смертных %s &6появился на сервере, прячьтесь..."
    };

    public void processBukkitConnect() {
        val player = user.getSpigot();

        ModLoader.send("InfinityFantasyMod-bundle.jar", player);

        Bukkit.getOnlinePlayers().forEach(targetPlayer -> {
            val currentUser = targetPlayer.<User>getBungeePlayer();
            if (currentUser == null || !currentUser.isLoaded() || currentUser.isInvalidated()) return;
            currentUser.findSyncHideSettings().onConnectPlayer(player);
        });

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(32);
        player.setResourcePack(ResourcePack.DEFAULT.getUrl(), ResourcePack.DEFAULT.findSha1());

        val questedPlayer = user.findQuestSettings();
        if (!questedPlayer.isPassed(1) && questedPlayer.findInProgress(1).isEmpty()) {
            questedPlayer.applyNewQuest(1, false);
            Fantasy.clientProvider().notification(player, "&aВам доступны &6Специальные предложения&a!", "&7Нажмите &6[DONATE_SHOP]&7, чтобы открыть меню");
        }

        if (questedPlayer.isPassed(1) && questedPlayer.isPassed(3000) && questedPlayer.findInProgress(3000).isEmpty()) {
            questedPlayer.applyNewQuest(3000, true);
        }

        questedPlayer.findAllInProgressQuests().stream().map(quest -> quest.findRequirementsByClass(BlockInteractRequirement.class).stream().filter(requirement -> !requirement.isDone()))
                .forEach(requirements -> {
                    requirements.forEach(requirement -> {
                        requirement.getGoals().keySet().forEach(location -> Wada.get().getBlocksOutlineModule().show(location, Color.GREEN, player));
                    });
                });

        Fantasy.runAsyncLater(() -> Fantasy.buffService().findById(user.getUniqueId()).ifPresent(entity -> entity.getAll().forEach(buff -> buff.onReceive(player))), 20);

        val scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        val team = scoreboard.registerNewTeam("fraction");
        team.setColor(user.getFractionType().getNameColor());
        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(false);
        team.addPlayer(player);

        player.setScoreboard(scoreboard);

        user.setTeam(team);

        for (Zone zone : ZoneList.findAll()) {
            if (zone.isIn(player)) {
                user.setCurrentZone(zone);
                break;
            }
        }

        if (user.getCurrentZone() == null)
            user.setCurrentZone(ZoneList.FARM_ZONE);

        ((FantasyUser) user).onDataLoad().forEach(handler -> handler.accept(user));

        PacketRegistry.write(Fantasy.gameServer().nameSpace(), new SessionUpdatePacket(player.getUniqueId(), SessionState.PLAYING));
        user.setLoaded(true);

        if (PermissionUtility.isAttentiveRekt(user.getUniqueId())) {
            Fantasy.buffService().appendBuff(user.getUniqueId(), BuffType.EXPERIENCE_BUFF_REKT);
            Fantasy.buffService().appendBuff(user.getUniqueId(), BuffType.SUPPORT_ATTACK_BUFF_REKT);
            IPermissionService.get().getPermissionContext(user.getUniqueId()).thenAccept(context -> {
                val message = rektMessages[MathUtility.randomInRange(0, rektMessages.length - 1)].formatted(context.toDisplayName());
                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameGlobalMessagePacket(message));

                Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION, message, Bukkit.getOnlinePlayers());
            });
        }

        Fantasy.treeProvider().initializeScheme(player);

        for (AuraTexture value : AuraTexture.values()) {
            val aura = new Aura(value, player, AuraAnimation.NONE);
            aura.send(player);
            aura.remove(player);
        }

        player.<User>getBungeePlayer().getCurrentAura().apply(player);
        player.<User>getBungeePlayer().findBattlePassSettings().initialize();
        CaseWorldProvider.getInstance().getWorldTexts().forEach(worldText -> worldText.send(player));
    }

    public void processModificationConnect() {
        val player = user.getSpigot();

        ((FantasyUser) user).onModificationLoad().forEach(handler -> handler.accept(user));

        val questedPlayer = user.findQuestSettings();
        questedPlayer.findAllInProgressQuests().forEach(quest -> quest.createRegisterPacket(player));

        Fantasy.buffService().findById(player.getUniqueId()).ifPresent(buffedEntity -> buffedEntity.getAll().forEach(buff -> new ImperiumBuffPacket(user, buff).notice(0)));

        CharacterProvider.getInstance().findAll().forEach(character -> character.onConnect(player));

        Fantasy.clientProvider().subscribeIndicator(user);
        Fantasy.clientProvider().inventoryOverlayText(user);
        Fantasy.clientProvider().hotbarLevel(user);
        Fantasy.clientProvider().attributeContext(user);
        Fantasy.clientProvider().updateCurrency(user);
        Fantasy.clientProvider().hotbarHealth(user);
        Fantasy.clientProvider().hotbarMana(user);
        Fantasy.clientProvider().updateIncubators(player);
        Fantasy.clientProvider().sendSettings(player);

        Fantasy.particleProvider().sendEdgeZone(
                player,
                Arrays.stream(ZoneList.findEdgeFragments()).map(zone -> new ParticleZoneData(zone.centerX(), zone.centerY(), zone.centerZ(), zone.xSize(), zone.ySize(), zone.zSize())).toList()
        );
    }

}
