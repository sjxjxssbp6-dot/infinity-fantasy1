package cupcake.nikky.imperium.tower.fraction;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.Anime;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.choicer.Choicer;
import org.bukkit.Bukkit;
import ru.cristalix.wada.module.confirm.ConfirmRequest;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FractionChoicer {

    private final User user;

    public void open() {
        val choicer = Choicer.builder()
                .title("Меню фракции силы")
                .description("Фракция одной силы ведет постоянную войну с фракцией другой силы")
                .build();

        final List<ReactiveButton> buttons = new ArrayList<>();

        if (user.findQuestSettings().isPassed(29) || user.findQuestSettings().findInProgress(29).isPresent()) {
            for (FractionType value : new FractionType[]{FractionType.CHAOS, FractionType.ORDER}) {
                val buttonBuilder = new ReactiveButton()
                        .texture(value == FractionType.CHAOS ? FantasyImage.FRACTION_CHAOS.toTexture() : FantasyImage.FRACTION_ORDER.toTexture())
                        .title(value.getTitle());

                final List<String> description = new ArrayList<>();
                val topFraction = FractionProvider.getInstance().findTopFraction();

                if (value == topFraction) {
                    description.add(TextUtility.colorize("&cСлишком много\n&cигроков"));
                    buttonBuilder.hint("");
                }

                if (user.getFractionType() == value) {
                    description.add("&6Ваша фракция");
                    buttonBuilder.hint("");
                }

                if (user.getFractionType() != value && value != topFraction) {
                    val containsSwapDebuff = Fantasy.buffService().findById(user.getUniqueId())
                            .map(buffedEntity -> buffedEntity.find(BuffType.FRACTION_CHANGE_DEBUFF).isPresent())
                            .orElse(false);
                    if (user.getFractionType() != FractionType.NONE && !containsSwapDebuff) {
                        description.add("&fСменить фракцию\n&fза &650.000&r\uE0FE\n&fи &63.000&r\uE106");
                    } else if (containsSwapDebuff) {
                        description.add("&cСмена фракции\n&cневозможна из-за\n&cслабости");
                        buttonBuilder.hint("");
                    }

                    if (!containsSwapDebuff) {
                        buttonBuilder.hint(user.getFractionType() == FractionType.NONE ? "Выбрать" : "Сменить")
                                .onLeftClick((player, i, reactiveButton) -> {
                                    Anime.close(player);
                                    if (user.getFractionType() == FractionType.NONE) {
                                        ConfirmRequest.get().request(player, "&fЖелаете выбрать фракцию силы &c" + value.getTitle() + "&f?", accepted -> {
                                            if (!accepted) {
                                                choicer.open(player);
                                                return;
                                            }

                                            val old = user.getFractionType();
                                            user.setFractionType(value);
                                            FractionProvider.getInstance().sendChangePacket(old, user.getFractionType());
                                            Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Вы успешно выбрали фракцию силы " + value.getTitle());

                                            this.open();
                                        });
                                    } else {
                                        ConfirmRequest.get().request(player, "&fЖелаете сменить фракцию за &650.000&r\uE0FE &fи &63.000&r\uE106?", accepted -> {
                                            if (!accepted) {
                                                choicer.open(player);
                                                return;
                                            }

                                            if (user.getCurrencyHandler().withdrawCurrency(CurrencyType.GRAN, 50_000)) {
                                                if (user.getCurrencyHandler().withdrawCurrency(CurrencyType.MINES, 3_000)) {
                                                    val old = user.getFractionType();
                                                    user.setFractionType(value);
                                                    FractionProvider.getInstance().sendChangePacket(old, user.getFractionType());
                                                    if (!user.findDonateSettings().donateData().isSubscribed())
                                                        Fantasy.buffService().appendBuff(player.getUniqueId(), BuffType.FRACTION_CHANGE_DEBUFF);
                                                    Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Вы успешно сменили фракцию силы");
                                                } else {
                                                    user.getCurrencyHandler().depositCurrency(CurrencyType.GRAN, 50_000);
                                                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно средств");
                                                }
                                            } else {
                                                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно средств");
                                            }

                                            this.open();
                                        });
                                    }
                                });
                    }
                }

                buttons.add(buttonBuilder.description(description));
            }
        }

        buttons.add(new ReactiveButton().title("Осколок\nфракций").texture(FantasyImage.FRACTION_TELEPORT.toTexture()).description("Переместит Вас\nна Осколок\nфракций")
                .onLeftClick((player, i, reactiveButton) -> {
                    if (user.getLevel() >= 61) {
                        val location = ZoneList.FRACTION_ZONE.spawnLocation(user.getFractionType().name());
                        if (location == null) {
                            Bukkit.getLogger().warning("Attempt to teleport to nullable location (fraction)");
                            return;
                        }

                        location.setWorld(MapServiceProcess.GAME_WORLD);
                        player.teleport(location);
                        Anime.close(player);
                    } else {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Переместиться на данный остров", "можно только с 61 уровня");
                    }
                }));
        buttons.add(new ReactiveButton().title("Вражда и мир").texture(FantasyImage.FRACTION_GENERAL.toTexture())
                .description("%s &fна\nнейтральной\nтерритории".formatted(user.findPvpSettings().localPvpState() ? "&cВражда" : "&aМир"))
                .onLeftClick((player, i, reactiveButton) -> {
                    user.findPvpSettings().localPvpState(!user.findPvpSettings().localPvpState());
                    reactiveButton.description("%s &fна\nнейтральной\nтерритории".formatted(user.findPvpSettings().localPvpState() ? "&cВражда" : "&aМир"));
                    Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Ваше состояние на нейтральной", "территории изменено");
                }));

        choicer.setStorage(buttons);
        choicer.open(user.getSpigot());
    }

}
