package cupcake.nikky.imperium.tower.chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.board.BoardType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.protocol.packet.GameGlobalMessagePacket;
import cupcake.nikky.imperium.tower.statistic.Statistics;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.core.IPlatform;
import ru.cristalix.core.IServerPlatform;
import ru.cristalix.core.chat.ChatContext;
import ru.cristalix.core.chat.ChatService;
import ru.cristalix.core.formatting.Formatting;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.core.realm.RealmId;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FantasyChatService extends ChatService implements Listener {

    private final Server server;
    private final Cache<UUID, Boolean> globalCooldown = CacheBuilder.newBuilder().concurrencyLevel(8).expireAfterWrite(6, TimeUnit.SECONDS).build();

    public FantasyChatService(IPermissionService permissionService, Server server) {
        super(IPlatform.get(), permissionService);

        this.server = server;
    }

    private void setGlobalCooldown(@NotNull final UUID uuid) {
        this.globalCooldown.put(uuid, Boolean.TRUE);
    }

    private boolean isGlobalCooldown(@NotNull final UUID uuid) {
        return this.globalCooldown.getIfPresent(uuid) != null;
    }

    public void processGlobalMessage(@NotNull final UUID playerUuid, @NotNull final FractionType fractionType, final int baseLevel,
                                     @NotNull final RealmId sourceRealm, @NotNull final String message, final boolean deathless, final boolean isSubscribed, @NotNull final String userDescription,
                                     @Nullable final ImperiumItem item) {
        Fantasy.getChatExecutor().execute(() -> {
            val chatView = this.getChatView(playerUuid);
            val context = new ChatContext(message, true, new HashSet<>(Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toSet())));

            val original = new HashSet<Player>();
            val filtered = chatView.filter(playerUuid, context);

            filtered.forEach(uuid1 -> {
                Player serverPlayer = this.server.getPlayer(uuid1);
                if (serverPlayer != null) original.add(serverPlayer);
            });

            val builder = new ComponentBuilder(new TextComponent(""));

            chatView.getFormattedComponent(playerUuid, context).thenCompose(message1 -> {
                builder.append(new ComponentBuilder(TextUtility.colorize("&7[&fР&6%d&7] ", sourceRealm.getId()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/realm " + sourceRealm.getId()))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Stream.of("Реалм #" + sourceRealm.getId(), "\nНажми на меня, чтобы переместиться").map(TextComponent::new).toArray(BaseComponent[]::new)))
                                .append("")
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[0]))
                                .event((ClickEvent) null)
                                .create())
                        .append(createPrefix(playerUuid, fractionType, baseLevel, isSubscribed, deathless, userDescription))
                        .append(PermissionUtility.isAttentiveRekt(playerUuid) ? TextUtility.colorize("&7[&6Преподобный&7] ") : "")
                        .append(message1)/*.append(createSuffix(deathless))*/.append(ChatColor.GRAY + " » ");

                return CompletableFuture.completedFuture(null);
            }).thenCompose(unused -> chatView.getMessageColorFor(playerUuid)
                    .thenApply(color -> builder.append(this.handleDefaultComponent(color, context.getLegacyMessage(), this.createItemLinkComponent(item)))))
                    .thenApply(unused -> builder.create())
                    .whenComplete((baseComponents, throwable) -> {
                        if (throwable != null) {
                            Fantasy.runLater(throwable::printStackTrace);
                        } else {
                            original.forEach(receiver -> receiver.spigot().sendMessage(baseComponents));
                        }
                    });
        });
    }

    @Override
    public void enable() throws Exception {
        super.enable();

        val eventExecutor = IServerPlatform.get().getPlatformEventExecutor();

        eventExecutor.registerListener(AsyncPlayerChatEvent.class, this, event -> {
            val extended = new ru.cristalix.core.event.AsyncPlayerChatEvent(event.getPlayer(), TextComponent.fromLegacyText(event.getMessage()), new HashSet<>(event.getRecipients()));
            event.getRecipients().clear();

            this.server.getPluginManager().callEvent(extended);

            if (extended.isCancelled()) {
                event.setCancelled(true);
            } else {
                val components = extended.getMessage();
                if (components != null) {
                    val recipients = extended.getRecipients();
                    if (recipients.isEmpty()) return;

                    components.thenAccept(message -> {
                        if (message.length == 0) return;

                        recipients.forEach(player -> {
                            if (player.isOnline()) {
                                player.spigot().sendMessage(message);
                            }
                        });
                    });

                }
            }
        }, EventPriority.HIGH, true);

        eventExecutor.registerListener(ru.cristalix.core.event.AsyncPlayerChatEvent.class, this, event -> {
            val player = event.getPlayer();
            val uuid = player.getUniqueId();
            val chatView = this.getChatView(uuid);

            String legacy = ChatColor.stripColor(TextComponent.toLegacyText(event.getOriginalMessage()));
            boolean global = legacy.charAt(0) == '!';

            if (chatView.isSilenced() && !player.hasPermission("core.chat.bypass.silence") && !PermissionUtility.isOperator(player)) {
                player.sendMessage(Formatting.error("Ты сейчас не можешь писать в чат!"));
                if (!global)
                    Fantasy.bukkitProvider().sendMessageToExactChat(ChatType.CURRENT_REALM, Formatting.error("Ты сейчас не можешь писать в чат!"), Collections.singleton(player));
                event.setCancelled(true);
                return;
            }

            val warnMessage = "Погоди перед отправкой следующего сообщения";

            if (!player.hasPermission("core.chat.bypass.cooldown") && chatView.isOnCooldown(uuid) && !PermissionUtility.isOperator(player)) {
                player.sendMessage(Formatting.error(warnMessage));
                if (!global)
                    Fantasy.bukkitProvider().sendMessageToExactChat(ChatType.CURRENT_REALM, Formatting.error(warnMessage), Collections.singleton(player));
                event.setCancelled(true);
                return;
            } else if (global && !player.hasPermission("core.chat.bypass.cooldown") && this.isGlobalCooldown(uuid) && !PermissionUtility.isOperator(player)) {
                player.sendMessage(Formatting.error(warnMessage));
                event.setCancelled(true);
                return;
            }

            val user = player.<User>getBungeePlayer();
            if (user == null || user.isInvalidated() || !user.isLoaded()) {
                event.setCancelled(true);
                return;
            }

            val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
            val isSubscribed = donateData != null && donateData.isSubscribed();
            val isDeathless = user.isDeathless();

            val baseLevel = user.getLevelController().getLevel();
            val fraction = user.getFractionType();

            if (global) {
                Bukkit.getLogger().info("<%s> %s".formatted(player.getName(), legacy));
            }

            val statisticSettings = user.findStatisticSettings();
            val userDescription = "\n&fУбито монстров: &6%s\n&fУбито игроков: &6%s\n&fУмер от монстров: &6%s\n&fУмер от игроков: &6%s".formatted(
                    statisticSettings.findStatistic(Statistics.MONSTER_KILL).data().toString(),
                    statisticSettings.findStatistic(Statistics.USER_KILL).data().toString(),
                    statisticSettings.findStatistic(Statistics.DEATHS_BY_MONSTER).data().toString(),
                    statisticSettings.findStatistic(Statistics.DEATHS_BY_USER).data().toString()
            );

            if (global) {
                this.setGlobalCooldown(uuid);
                legacy = legacy.substring(1);

                if (legacy.trim().isEmpty()) {
                    event.setCancelled(true);
                    return;
                }

                val item = (legacy.contains("#рука") || legacy.contains("#hand")) ? user.getFantasyInventory().getHeldItem() : null;
                val serializedItem = item == null ? null : item.serialize();
                val realmName = Fantasy.gameServer().toRealm().getRealmName();

                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameGlobalMessagePacket(player.getUniqueId(), realmName, legacy, fraction, baseLevel, isDeathless, isSubscribed, userDescription, serializedItem));
                this.processGlobalMessage(player.getUniqueId(), fraction, baseLevel, Fantasy.gameServer().toRealm(), legacy, isDeathless, isSubscribed, userDescription, item);
                event.setCancelled(true);
                return;
            }

            chatView.setOnCooldown(uuid);
            val original = event.getRecipients();
            val recipients = original.stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toSet());
            val context = new ChatContext(legacy, global, recipients);
            val filtered = chatView.filter(uuid, context);

            original.clear();

            filtered.forEach(uuid1 -> {
                Player serverPlayer = this.server.getPlayer(uuid1);
                if (serverPlayer != null) original.add(serverPlayer);
            });

            val builder = new ComponentBuilder("");

            event.setMessage(chatView.getFormattedComponent(uuid, context).thenCompose(message -> {
                if (user != null) builder.append(createPrefix(user.getUniqueId(), user.getFractionType(), baseLevel, isSubscribed, isDeathless, userDescription));

                builder.append(PermissionUtility.isAttentiveRekt(uuid) ? TextUtility.colorize("&7[&6Преподобный&7] ") : "").append(message)/*.append(createSuffix(isDeathless))*/.append(ChatColor.GRAY + " » ");

                return CompletableFuture.completedFuture(null);
            }).thenApply(unused -> {
                val linkedItem = this.createItemLinkComponent(user);
                return chatView.getMessageColorFor(uuid).thenApply(color -> builder.append(this.handleDefaultComponent(color, context.getLegacyMessage(), linkedItem)));
            }).thenApply(unused -> {
                val components = builder.create();
                Fantasy.bukkitProvider().sendMessageToExactChat(ChatType.CURRENT_REALM, BaseComponent.toLegacyText(components), original);
                return components;
            }));
        }, EventPriority.LOW, true);

        eventExecutor.registerListener(PlayerQuitEvent.class, this,
                event -> this.setChatView(event.getPlayer().getUniqueId(), null),
                EventPriority.NORMAL, false);
    }

    @Override
    public void disable() throws Exception {
        super.disable();
        HandlerList.unregisterAll(this);
    }

    private BaseComponent[] createPrefix(@NotNull final UUID playerUUID, @NotNull final FractionType fractionType,
                                         final int baseLevel, final boolean isSubscribed, final boolean deathless,
                                         @NotNull final String userDescription) {
        val levelLine = "&6" + baseLevel;
        val fractionLine = fractionType == FractionType.NONE ? "" : fractionType.getColoredName() + " ";

        val builder = new ComponentBuilder(TextUtility.colorize(fractionLine + "&7[" + levelLine + "&7] "));
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                new TextComponent(TextUtility.colorize("&fУровень игрока: &6" + baseLevel + "\n" + userDescription))
        })).append("").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[0]));

        val board = BoardType.GENERAL.getFantasyBoard();
        val placeIcon = board.completedPlaces() ? (board.firstPlace().equals(playerUUID) ? "\uE126" :
                board.secondPlace().equals(playerUUID) ? "\uE127" :
                        board.thirdPlace().equals(playerUUID) ? "\uE128" : "") : "";
        val placeTitle = board.completedPlaces() ? (board.firstPlace().equals(playerUUID) ? "Первое место в общем рейтинге" :
                board.secondPlace().equals(playerUUID) ? "Второе место в общем рейтинге" :
                        board.thirdPlace().equals(playerUUID) ? "Третье место в общем рейтинге" : "") : "";

        if (!placeIcon.isBlank())
            builder.append(placeIcon).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(placeTitle)}))
                    .append("").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[0]));
        if (isSubscribed)
            builder.append(placeIcon.isBlank() ? "\uE120" : " \uE120").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Премиум подписка")}))
                    .append("").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[0]));
        if (deathless)
            builder.append(new TextComponent((placeIcon.isBlank() && !isSubscribed) ? "\uE1A3" : " \uE1A3")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new BaseComponent[] {new TextComponent("Костлявая обходит его стороной\nИгрок не имеет смертей")}))
                    .append("").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[0]));

        return builder.append((isSubscribed || deathless || !placeIcon.isBlank()) ? " " : "").create();
    }

//    private BaseComponent[] createSuffix(final boolean deathless) {
//        var builder = new ComponentBuilder("");
//
//        if (deathless)
//            builder.append(new TextComponent(" \uE1A3")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
//                    new BaseComponent[] {new TextComponent("Костлявая обходит его стороной\nИгрок не имеет смертей")}))
//                    .append("").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[0]));
//
//        return builder.create();
//    }

    public BaseComponent[] handleDefaultComponent(@NotNull final String color, @NotNull final String legacyText, @Nullable final BaseComponent itemLink) {
        val lowerCaseText = legacyText.toLowerCase();
        val wantLink = lowerCaseText.contains("#рука") || lowerCaseText.contains("#hand");
        val newBuilder = new ComponentBuilder("");

        if (wantLink && itemLink != null) {
            val words = legacyText.split("\\s+");

            boolean hasPrevious = false;
            int countLinks = 0;

            for (final String word : words) {
                val tempWord = ChatColor.stripColor(word);

                newBuilder.append(hasPrevious ? " " : "");

                if ((tempWord.equalsIgnoreCase("#рука") || tempWord.equalsIgnoreCase("#hand")) && countLinks <= 3) {
                    newBuilder.append(itemLink);
                    countLinks += 1;
                } else {
                    newBuilder.append(new ComponentBuilder(word).color(ChatColor.getByChar(color.charAt(1))).create()).retain(ComponentBuilder.FormatRetention.FORMATTING);
                }

                hasPrevious = true;
            }
        } else newBuilder.append(new ComponentBuilder(legacyText).color(ChatColor.getByChar(color.charAt(1))).create()).retain(ComponentBuilder.FormatRetention.FORMATTING);

        return newBuilder.create();
    }

    public BaseComponent createItemLinkComponent(@Nullable final User user) {
        return this.createItemLinkComponent(user == null ? null : user.getFantasyInventory().getHeldItem());
    }

    public BaseComponent createItemLinkComponent(@Nullable final ImperiumItem imperiumItem) {
        if (imperiumItem == null) {
            return null;
        } else {
            val item = imperiumItem.createNew();
            val displayName = item.getItemMeta() == null ? imperiumItem.getRare().getColor() + imperiumItem.getImperiumType().getTypeColor() + imperiumItem.getName() : item.getItemMeta().getDisplayName();
            val descriptionContainer = imperiumItem.processDescriptionContainer();

            val lore = Optional.ofNullable(descriptionContainer.isWithAdvanced() ? descriptionContainer.getAdvancedLore() : descriptionContainer.getLore())
                    .orElse(new ArrayList<>()).stream().map(s -> s + "\n")
                    .collect(Collectors.toList());
            lore.addFirst(displayName + "\n");

            val result = new TextComponent(TextUtility.colorize("&7[" + displayName + "&7]"));
            result.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextUtility.colorize(lore).stream().map(TextComponent::new).toArray(BaseComponent[]::new)));

            return result;
        }
    }

}
