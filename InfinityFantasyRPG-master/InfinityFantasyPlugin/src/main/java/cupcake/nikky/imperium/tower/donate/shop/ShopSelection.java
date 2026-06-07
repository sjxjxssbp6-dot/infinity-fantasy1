package cupcake.nikky.imperium.tower.donate.shop;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraAnimationTitleConverter;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.donate.SubscriptionDurationType;
import cupcake.nikky.imperium.tower.item.Icons;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.QuestUtility;
import lombok.Getter;
import lombok.val;
import me.func.mod.Anime;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import me.func.protocol.data.emoji.Emoji;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.core.invoice.BalanceData;
import ru.cristalix.core.invoice.IInvoiceService;
import ru.cristalix.core.item.Items;
import ru.cristalix.core.util.UtilTime;
import ru.cristalix.wada.module.confirm.ConfirmRequest;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class ShopSelection {

    @Getter
    private static final ShopSelection instance = new ShopSelection();

    @Getter
    private final List<UUID> confirmRequestMarks = new ArrayList<>();
    private final Cache<UUID, Boolean> clickDelay = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(200, TimeUnit.MILLISECONDS)
            .build();

    public void open(@NotNull final Player player, @NotNull final DonateData donateData, final int sale) {
        val premium = FantasyImage.PREMIUM.toTexture();
        val currency = FantasyImage.COINS_2.toTexture();

        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> this.executeStep(donateData, player, currency, premium, Fantasy.gameServer().isTest() ? 100 : sale));
    }

    private void executeStep(@NotNull final DonateData donateData, @NotNull final Player player, @NotNull final String currency, @NotNull final String premium, final int sale) {
        val balanceData = IInvoiceService.get().getBalanceData(player.getUniqueId());

        balanceData.whenComplete((balanceData1, throwable1) -> {
            if (throwable1 != null) {
                throwable1.printStackTrace();
                return;
            }

            Selection.builder()
                    .title("Магазин | Выбор категории")
                    .rows(4)
                    .columns(3)
                    .storage(new ReactiveButton()
                                    .texture(currency)
                                    .hint("Открыть")
                                    .title("Специальная\nвалюта")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findCurrencySelection(balanceData1, donateData, sale).open(player1)),
                            new ReactiveButton()
                                    .texture(premium)
                                    .hint("Открыть")
                                    .title("Премиум")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findPremiumSelection(donateData, premium, 0).open(player1)),
                            new ReactiveButton()
                                    //.texture("\uE0CC")
                                    .texture(FantasyImage.LOOT_FILTER.toTexture())
                                    .hint("Открыть")
                                    .title("Расширенный\nфильтр предметов")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findLootFilterSelection(donateData, 0).open(player1)),
                            new ReactiveButton()
                                    .texture(FantasyImage.BUFFS.toTexture())
                                    .hint("Открыть")
                                    .title("Усиливающие\nэффекты")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findBuffSelection(player, donateData, 0).open(player1)),
                            new ReactiveButton()
                                    .texture(AuraObject.CHRISTMAS_AURA.getFantasyAura().resourceLocation())
                                    .hint("Открыть")
                                    .title("Ауры")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findAuraSelection(player, donateData, 0).open(player1)),
                            new ReactiveButton()
                                    .item(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.HORSE)).build())
                                    .hint("Открыть")
                                    .title("Спутники")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findMountSelection(player, donateData, 0).open(player1)),
                            new ReactiveButton()
                                    .item(Icons.BASE_BLOOD)
                                    .hint("Открыть")
                                    .title("Эффекты крови")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findBloodSelection(donateData, 0).open(player1)),
                            new ReactiveButton()
                                    .texture(FantasyImage.DEATH_RED_FADE.toTexture())
                                    .hint("Открыть")
                                    .title("Эффекты смерти")
                                    .onLeftClick((player1, i, reactiveButton) -> this.findDeathSelection(donateData, 0).open(player1))
                    ).build().open(player);
        });
    }

    private Selection findDeathSelection(@NotNull final DonateData donate, final int sale) {
        final List<ReactiveButton> buttons = new ArrayList<>();
        val selection = Selection.builder()
                .title("Магазин | Эффекты смерти")
                .rows(5)
                .columns(2)
                .hint("Купить")
                .money(String.valueOf(donate.getTotalCurrency()))
                .vault(FantasyImage.CURRENCY.toTexture())
                .build();

        DonateSamples.deathSample().findPrices().forEach((deathObject, price) -> {
            val button = new ReactiveButton();

            if (deathObject.getIcon() != null)
                button.item(deathObject.getIcon());
            else button.texture(deathObject.getTexture());

            buttons.add(button.title(deathObject.getTitle().replace(" ", "\n"))
                    .price(price)
                    .sale(sale)
                    .hover(deathObject.getTitle())
                    .description("\uE3FF Предпросмотр")
                    .onRightClick((player, i, reactiveButton) -> {
                        Anime.close(player);
                        Fantasy.particleProvider().sendLocalLocationDeath(player, player.getLocation(), deathObject.getDeathType());
                        deathObject.getDeathSound().playLocalSound(player, player.getLocation());
                    })
                    .onLeftClick((player, i, reactiveButton) -> {
                        if (confirmRequestMarks.contains(player.getUniqueId()) || clickDelay.getIfPresent(player.getUniqueId()) != null) return;

                        if (donate.containsMetadata(deathObject.name())) {
                            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "У Вас уже имеется данный эффект смерти");
                            return;
                        }

                        confirmRequestMarks.add(player.getUniqueId());
                        ConfirmRequest.get().request(player, String.format("&fЖелаете купить %s?", deathObject.getTitle()), accepted -> {
                            confirmRequestMarks.remove(player.getUniqueId());
                            clickDelay.put(player.getUniqueId(), true);
                            if (!accepted) return;
                            DonateSamples.deathSample().createProcess(donate, deathObject, reactiveButton.withSale()).accept(player);
                            selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                        });
                    }));
        });

        selection.setStorage(buttons);

        return selection;
    }

    private Selection findBloodSelection(@NotNull final DonateData donate, final int sale) {
        final List<ReactiveButton> buttons = new ArrayList<>();
        val selection = Selection.builder()
                .title("Магазин | Эффект крови")
                .rows(5)
                .columns(2)
                .hint("Купить")
                .money(String.valueOf(donate.getTotalCurrency()))
                .vault(FantasyImage.CURRENCY.toTexture())
                .build();

        DonateSamples.bloodSample().findPrices().forEach((bloodObject, price) -> {
            val button = new ReactiveButton();

            if (bloodObject.getIcon() != null)
                button.item(bloodObject.getIcon());
            else button.texture(bloodObject.getTexture());

            buttons.add(button.title(bloodObject.getTitle().replace(" ", "\n"))
                    .price(price)
                    .sale(sale)
                    .hover(bloodObject.getTitle())
                    .description("\uE3FF Предпросмотр")
                    .onRightClick((player, i, reactiveButton) -> {
                        Anime.close(player);
                        Fantasy.particleProvider().sendLocalEntityBleed(player, player.getEntityId(), bloodObject.getBloodType());
                    })
                    .onLeftClick((player, i, reactiveButton) -> {
                        if (confirmRequestMarks.contains(player.getUniqueId()) || clickDelay.getIfPresent(player.getUniqueId()) != null) return;

                        if (donate.containsMetadata(bloodObject.name())) {
                            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "У Вас уже имеется данный эффект крови");
                            return;
                        }

                        confirmRequestMarks.add(player.getUniqueId());
                        ConfirmRequest.get().request(player, String.format("&fЖелаете купить %s?", bloodObject.getTitle()), accepted -> {
                            confirmRequestMarks.remove(player.getUniqueId());
                            clickDelay.put(player.getUniqueId(), true);
                            if (!accepted) return;
                            DonateSamples.bloodSample().createProcess(donate, bloodObject, reactiveButton.withSale()).accept(player);
                            selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                        });
                    }));
        });

        selection.setStorage(buttons);

        return selection;
    }

    private Selection findMountSelection(@NotNull final Player player, @NotNull final DonateData donate, final int sale) {
        final List<ReactiveButton> buttons = new ArrayList<>();
        val selection = Selection.builder()
                .title("Магазин | Спутники")
                .rows(5)
                .columns(2)
                .hint("Купить")
                .money(String.valueOf(donate.getTotalCurrency()))
                .vault(FantasyImage.CURRENCY.toTexture())
                .build();

        DonateSamples.mountSample().findPrices().forEach((mountType, price) -> {
            val mountSettings = mountType.getSettings();
            val button = new ReactiveButton();

            if (mountSettings.getIcon() != null)
                button.item(mountSettings.getIcon());
            else button.texture(mountSettings.getTexture());

            buttons.add(button.title(mountSettings.getTitle().replace(" ", "\n"))
                    .price(price)
                    .sale(sale)
                            .hover("&6" + mountSettings.getTitle(), "Увеличение скорости: &6" + mountSettings.getSpeed() + "%")
                    .onLeftClick((player1, i, reactiveButton) -> {
                        if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player.getUniqueId()) != null) return;

                        if (donate.containsMetadata(mountType.name())) {
                            Fantasy.clientProvider().notification(player1, SystemNotificationType.ERROR, "У Вас уже имеется данный спутник");
                            return;
                        }

                        confirmRequestMarks.add(player1.getUniqueId());
                        ConfirmRequest.get().request(player, String.format("&fЖелаете купить &6%s?", mountSettings.getTitle()), accepted -> {
                            confirmRequestMarks.remove(player1.getUniqueId());
                            clickDelay.put(player1.getUniqueId(), true);
                            if (!accepted) return;
                            DonateSamples.mountSample().createProcess(donate, mountType, reactiveButton.withSale()).accept(player);
                            selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                        });
                    }));
        });

        selection.setStorage(buttons);

        return selection;
    }

    private Selection findAuraSelection(@NotNull final Player player, @NotNull final DonateData donate, final int sale) {
        final List<ReactiveButton> buttons = new ArrayList<>();
        val selection = Selection.builder()
                .title("Магазин | Ауры")
                .rows(4)
                .columns(2)
                .hint("Купить")
                .money(String.valueOf(donate.getTotalCurrency()))
                .vault(FantasyImage.CURRENCY.toTexture())
                .build();

        DonateSamples.auraSample().findPrices().forEach((auraObject, price) -> {
            val aura = auraObject.getFantasyAura();

            buttons.add(new ReactiveButton().title(aura.title())
                    .description("Анимация: &6" + AuraAnimationTitleConverter.convert(aura.animation()))
                    .price(price)
                    .sale(sale)
                    .texture(aura.resourceLocation())
                    .onLeftClick((player1, i, reactiveButton) -> {
                        if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player.getUniqueId()) != null) return;

                        if (donate.containsMetadata(auraObject.name())) {
                            Fantasy.clientProvider().notification(player1, SystemNotificationType.ERROR, "У Вас уже имеется данная аура");
                            return;
                        }

                        confirmRequestMarks.add(player1.getUniqueId());
                        ConfirmRequest.get().request(player, String.format("&fЖелаете купить &6%s?", aura.title()), accepted -> {
                            confirmRequestMarks.remove(player1.getUniqueId());
                            clickDelay.put(player1.getUniqueId(), true);
                            if (!accepted) return;
                            DonateSamples.auraSample().createProcess(donate, auraObject, reactiveButton.withSale()).accept(player);
                            selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                        });
                    }));
        });

        selection.setStorage(buttons);

        return selection;
    }

    private Selection findBuffSelection(@NotNull final Player player, @NotNull final DonateData donate, final int sale) {
        final List<ReactiveButton> buttons = new ArrayList<>();
        val selection = Selection.builder()
                .title("Магазин | Усиливающие эффекты")
                .rows(4)
                .columns(3)
                .hint("Купить")
                .money(String.valueOf(donate.getTotalCurrency()))
                .vault(FantasyImage.CURRENCY.toTexture())
                .build();

        DonateSamples.buffSample().findPrices().forEach((buff, price) -> {
            val level = Fantasy.userService().findById(player.getUniqueId()).map(User::getLevel).orElse(1);
            buff.setLevel(level);
            buff.getAttributeHandlers().forEach(handler -> handler.accept(level));

            buttons.add(new ReactiveButton()
                    .description(String.join("\n", QuestUtility.convertDescription("&6" + buff.getTitle(), 19)))
                    .price(price)
                    .sale(sale)
                    .texture(buff.getBuffType().getImage().toTexture())
                    .hover("&6" + buff.getTitle(), "\n&e" + String.join("\n", buff.getDescription()), "\n&cДлительность: " + UtilTime.makeStr(buff.getDurationSeconds() * 1000L))
                    .onLeftClick((player1, i, reactiveButton) -> {
                        if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player.getUniqueId()) != null) return;

                        val buffedEntity = Fantasy.buffService().findById(player1.getUniqueId()).orElse(null);
                        if (buffedEntity == null) {
                            Fantasy.clientProvider().notification(player1, SystemNotificationType.ERROR, "Попробуйте позже");
                            return;
                        }

                        if (buffedEntity.find(buff.getBuffType()).isPresent()) {
                            Fantasy.clientProvider().notification(player1, SystemNotificationType.ERROR, "У Вас уже имеется данное усиление");
                            return;
                        }

                        confirmRequestMarks.add(player1.getUniqueId());
                        ConfirmRequest.get().request(player, String.format("&fЖелаете купить &6%s &fна &62 &fчаса?", buff.getTitle()), accepted -> {
                            confirmRequestMarks.remove(player1.getUniqueId());
                            clickDelay.put(player1.getUniqueId(), true);
                            if (!accepted) return;
                            DonateSamples.buffSample().createProcess(donate, buff, reactiveButton.withSale()).accept(player);
                            selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                        });
                    }));
        });

        selection.setStorage(buttons);

        return selection;
    }

    private Selection findPremiumSelection(@NotNull final DonateData donate, @NotNull final String premiumTexture, final int sale) {
        val hover = String.join("\n", Arrays.asList(
                "&fПремиум подписка предоставляет: ",
                " &6Удаленный доступ к складу (по стандарту кнопка 'U')",
                " &6Удаленный доступ к Фирмологу (по стандарту кнопка 'H')",
                " &6Удаленно принимать и сдавать задания (по стандарту кнопка 'J')",
                " &6+30% боевого опыта",
                " &6Получение на 50% больше опыта инструментом",
                " &6На 50% больше добычи при вскапывании и ловли рыбы",
                " &6Уменьшение потери опыта после смерти в два раза",
                " &6Мгновенное перемещение через меню телепортов",
                " &6Смену фракции силы без ограничений (и сброс текущих ограничений)",
                " &6Специальную ауру",
                " &6Специальную иконку перед Вашим ником",
                " &6+50% длительность временных Осколков",
                " &6+2 возрождения на временных Осколках",
                " &6+1 к лимиту одновременного использования кристаллов-инкубаторов",
                " &6Возможность открывать сразу несколько кейсов",
                " &6Доступ к ежедневным наградам"
        ));

        val selection = Selection.builder()
                .title("Магазин | Премиум подписка")
                .rows(3)
                .columns(3)
                .hint("Купить")
                .money(String.valueOf(donate.getTotalCurrency()))
                .vault(FantasyImage.CURRENCY.toTexture())
                .build();

        selection.setStorage(Arrays.asList(new ReactiveButton().hover(hover).description("&6Премиум на 30 день")
                .price(DonateSamples.premiumSample().findPrices().get(SubscriptionDurationType.WEEK_4)).sale(sale).texture(premiumTexture)
                .onLeftClick((player1, i, reactiveButton) -> {
                    if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player1.getUniqueId()) != null) return;
                    confirmRequestMarks.add(player1.getUniqueId());

                    ConfirmRequest.get().request(player1, "&fЖелаете продлить &6Премиум подписку &fна &630 &fдней?", accepted -> {
                        confirmRequestMarks.remove(player1.getUniqueId());
                        clickDelay.put(player1.getUniqueId(), true);
                        if (!accepted) return;
                        DonateSamples.premiumSample().createProcess(donate, SubscriptionDurationType.WEEK_4, reactiveButton.withSale()).accept(player1);
                        selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                    });
                }), new ReactiveButton().hover(hover).description("&6Премиум на 60 дней")
                .price(DonateSamples.premiumSample().findPrices().get(SubscriptionDurationType.MONTH_2)).sale(sale).texture(premiumTexture)
                .onLeftClick((player1, i, reactiveButton) -> {
                    if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player1.getUniqueId()) != null) return;
                    confirmRequestMarks.add(player1.getUniqueId());

                    ConfirmRequest.get().request(player1, "&fЖелаете продлить &6Премиум подписку &fна &660 &fдней?", accepted -> {
                        confirmRequestMarks.remove(player1.getUniqueId());
                        clickDelay.put(player1.getUniqueId(), true);
                        if (!accepted) return;
                        DonateSamples.premiumSample().createProcess(donate, SubscriptionDurationType.MONTH_2, reactiveButton.withSale()).accept(player1);
                        selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                    });
                }), new ReactiveButton().hover(hover).description("&6Премиум на 180 дней")
                .price(DonateSamples.premiumSample().findPrices().get(SubscriptionDurationType.MONTH_6)).sale(sale).texture(premiumTexture)
                .onLeftClick((player1, i, reactiveButton) -> {
                    if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player1.getUniqueId()) != null) return;
                    confirmRequestMarks.add(player1.getUniqueId());

                    ConfirmRequest.get().request(player1, "&fЖелаете продлить &6Премиум подписку &fна &6180 &fдней?", accepted -> {
                        confirmRequestMarks.remove(player1.getUniqueId());
                        clickDelay.put(player1.getUniqueId(), true);
                        if (!accepted) return;
                        DonateSamples.premiumSample().createProcess(donate, SubscriptionDurationType.MONTH_6, reactiveButton.withSale()).accept(player1);
                        selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                    });
                })));

        return selection;
    }

    private Selection findLootFilterSelection(@NotNull final DonateData donate, final int sale) {
        val hover = String.join("\n", Arrays.asList(
                "&6Расширенный фильтр предметов предоставляет",
                "&6автоматическую продажу предметов, которые не подходят",
                "&6по требованиям, вместо их удаления"
        ));

        val texture = FantasyImage.LOOT_FILTER.toTexture();
        val selection = Selection.builder()
                .title("Магазин | Подписка на расширенный фильтр предметов")
                .rows(3)
                .columns(3)
                .hint("Купить")
                .money(String.valueOf(donate.getTotalCurrency()))
                .vault(FantasyImage.CURRENCY.toTexture())
                .build();

        selection.setStorage(Arrays.asList(new ReactiveButton().hover(hover)
                .description(String.join("\n", QuestUtility.convertDescription("&6Расширенный фильтр предметов на 30 денй", 19)))
                .price(DonateSamples.lootFilterSample().findPrices().get(SubscriptionDurationType.WEEK_4)).sale(sale).texture(texture)
                .onLeftClick((player1, i, reactiveButton) -> {
                    if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player1.getUniqueId()) != null) return;
                    confirmRequestMarks.add(player1.getUniqueId());

                    ConfirmRequest.get().request(player1, "&fЖелаете продлить &6Расширенный фильтр &6предметов &fна &630 &fдней?", accepted -> {
                        confirmRequestMarks.remove(player1.getUniqueId());
                        clickDelay.put(player1.getUniqueId(), true);
                        if (!accepted) return;
                        DonateSamples.lootFilterSample().createProcess(donate, SubscriptionDurationType.WEEK_4, reactiveButton.withSale()).accept(player1);
                        selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                    });
                }), new ReactiveButton().hover(hover)
                .description(String.join("\n", QuestUtility.convertDescription("&6Расширенный фильтр предметов на 60 дней", 19)))
                .price(DonateSamples.lootFilterSample().findPrices().get(SubscriptionDurationType.MONTH_2)).sale(sale).texture(texture)
                .onLeftClick((player1, i, reactiveButton) -> {
                    if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player1.getUniqueId()) != null) return;
                    confirmRequestMarks.add(player1.getUniqueId());

                    ConfirmRequest.get().request(player1, "&fЖелаете продлить &6Расширенный фильтр &6предметов &fна &660 &fдней?", accepted -> {
                        confirmRequestMarks.remove(player1.getUniqueId());
                        clickDelay.put(player1.getUniqueId(), true);
                        if (!accepted) return;
                        DonateSamples.lootFilterSample().createProcess(donate, SubscriptionDurationType.MONTH_2, reactiveButton.withSale()).accept(player1);
                        selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                    });
                }), new ReactiveButton().hover(hover)
                .description(String.join("\n", QuestUtility.convertDescription("&6Расширенный фильтр предметов на 180 дней", 19)))
                .price(DonateSamples.lootFilterSample().findPrices().get(SubscriptionDurationType.MONTH_6)).sale(sale).texture(texture)
                .onLeftClick((player1, i, reactiveButton) -> {
                    if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player1.getUniqueId()) != null) return;
                    confirmRequestMarks.add(player1.getUniqueId());

                    ConfirmRequest.get().request(player1, "&fЖелаете продлить &6Расширенный фильтр &6предметов &fна &6180 &fдней?", accepted -> {
                        confirmRequestMarks.remove(player1.getUniqueId());
                        clickDelay.put(player1.getUniqueId(), true);
                        if (!accepted) return;
                        DonateSamples.lootFilterSample().createProcess(donate, SubscriptionDurationType.MONTH_6, reactiveButton.withSale()).accept(player1);
                        selection.setMoney(String.valueOf(donate.getTotalCurrency()));
                    });
                })));

        return selection;
    }

    private Selection findCurrencySelection(@Nullable final BalanceData balanceData, @NotNull final DonateData donate, final int sale) {
        val text = "&fЖелаете купить%n&e%d Фирм-монет&f?";

        val selection = Selection.builder()
                .title("Магазин | Специальная валюта")
                .rows(3)
                .columns(3)
                .hint("Купить")
                .info("Фирм-монеты останутся с Вами и после вайпа!")
                .money(String.valueOf(balanceData == null ? 0 : balanceData.getCrystals() + balanceData.getCoins()))
                .vault(Emoji.DONATE)
                .build();

        String[] textures = new String[] {FantasyImage.COINS_1.toTexture(), FantasyImage.COINS_2.toTexture(), FantasyImage.COINS_3.toTexture()};
        final List<ReactiveButton> storage = new ArrayList<>();
        val prices = DonateSamples.currencySample().findPrices();

        int count = 0;
        for (Integer currency : prices.keySet()) {
            val price = prices.get(currency);

            storage.add(new ReactiveButton().description(String.format("&6%s Фирм-монет", currency))
                    .price(price)
                    .sale(sale)
                    .texture(textures[count++])
                    .onLeftClick((player1, i, reactiveButton) -> {
                        if (confirmRequestMarks.contains(player1.getUniqueId()) || clickDelay.getIfPresent(player1.getUniqueId()) != null) return;
                        confirmRequestMarks.add(player1.getUniqueId());

                        ConfirmRequest.get().request(player1, String.format(text, currency), accepted -> {
                            confirmRequestMarks.remove(player1.getUniqueId());
                            clickDelay.put(player1.getUniqueId(), true);
                            if (!accepted) return;
                            DonateSamples.currencySample().createProcess(donate, currency, reactiveButton.withSale()).accept(player1);
                            selection.setMoney(String.valueOf(balanceData == null ? 0 : balanceData.getCrystals() + balanceData.getCoins()));
                        });
                    }));
        }

        selection.setStorage(storage);

        return selection;
    }

}
