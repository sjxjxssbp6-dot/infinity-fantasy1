package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.battlepass.quest.BattlePassQuest;
import cupcake.nikky.imperium.tower.battlepass.reward.RewardSettings;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;
import lombok.Setter;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.Anime;
import me.func.mod.ui.battlepass.BattlePassData;
import me.func.mod.ui.battlepass.BattlePassPageAdvanced;
import me.func.protocol.profile.BattlePassSale;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.coupons.ICouponsService;
import ru.cristalix.core.invoice.IInvoiceService;
import ru.cristalix.core.invoice.Invoice;
import ru.cristalix.core.invoice.TransactionCategory;
import ru.cristalix.wada.Wada;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
public class SyncBattlePassSettings extends SavableUserSettings {

    private static final Type typeToken = new TypeToken<List<Integer>>(){}.getType();

    private static final int EXP_REQUIREMENT = 35;
    private static final int MAXIMUM_BATTLEPASS_LEVEL = 30;

    private BattlePassData lastData;
    private List<BattlePassSale> lastSales;

    private final IntSet passedQuests = new IntArraySet();
    private final IntSet passedRewards = new IntArraySet();
    private final Set<BattlePassQuest> activeQuests = new HashSet<>();

    private boolean advanced;
    private int currentExp;

    public SyncBattlePassSettings(User user) {
        super(user);
    }

    public int getCurrentLevel() {
        return getCurrentExp() / EXP_REQUIREMENT;
    }

    public BattlePassData getLastData() {
        if (lastData == null)
            this.generateData();
        return lastData;
    }

    public void changeExp(final int exp) {
        this.lastData = null;
        this.currentExp += exp;
    }

    public void applyActiveQuest(@NotNull final BattlePassQuest quest) {
        this.lastData = null;
        this.activeQuests.add(quest);
    }

    public void applyActiveQuest(@NotNull final List<BattlePassQuest> quest) {
        this.lastData = null;
        this.activeQuests.addAll(quest);
    }

    public void applyReward(final int rewardId) {
        this.lastData = null;
        this.passedRewards.add(rewardId);
    }

    public void invalidateReward(final int rewardId) {
        this.lastData = null;
        this.passedRewards.remove(rewardId);
    }

    public boolean isRewardApplied(final int rewardId) {
        return getPassedRewards().contains(rewardId);
    }

    public void applyPassedQuest(@NotNull final BattlePassQuest quest) {
        this.lastData = null;
        this.activeQuests.remove(quest);
        if (this.passedQuests.add(quest.getId()))
            this.changeExp(quest.getRewardExp());
    }

    private List<BattlePassQuest> checkQuests(@NotNull final List<BattlePassQuest> quests) {
        final List<BattlePassQuest> result = new ArrayList<>();

        for (BattlePassQuest quest : quests) {
            var isDone = quest.getIsDone().test(getPlayer());
            if (isDone) {
                this.applyPassedQuest(quest);
                var next = quest.getChain().next(quest.getId() + 1);
                if (next != null) {
                    this.applyActiveQuest(next);
                    result.add(next);
                }
            }
        }

        return result;
    }

    private List<BattlePassQuest> checkActiveQuests() {
        return this.checkQuests(new ArrayList<>(getActiveQuests()));
    }

    public void processQuests() {
        if (activeQuests.isEmpty()) return;

        var oldLevels = getCurrentExp() / EXP_REQUIREMENT;
        var nextQuests = this.checkActiveQuests();
        var someQuestDone = !nextQuests.isEmpty();

        while (!nextQuests.isEmpty()) {
            nextQuests = this.checkQuests(nextQuests);
        }

        var newLevels = getCurrentExp() / EXP_REQUIREMENT;
        if (someQuestDone && oldLevels == newLevels) {
            Fantasy.clientProvider().notification(getPlayer(), "&6Боевой пропуск", "&fЗадание выполнено!");
        } else if (someQuestDone && oldLevels < newLevels) {
            Fantasy.clientProvider().notification(getPlayer(), "&6Боевой пропуск", "&fЗадание выполнено, доступна &6награда&f!");
        }

        if (getCurrentLevel() >= MAXIMUM_BATTLEPASS_LEVEL) {
            this.activeQuests.clear();
        }
    }

    public void initialize() {
        if (getActiveQuests().isEmpty() && getCurrentLevel() < MAXIMUM_BATTLEPASS_LEVEL) {
            this.applyActiveQuest(Fantasy.battlePassProvider().generateQuests(getPlayer()));
        }
    }

    public int getSale() {
        return Fantasy.gameServer().isTest() ? 100 : ICouponsService.get().getDiscount(getUser().getUniqueId());
    }

    public void generateData() {
        this.lastData = new BattlePassData.Builder()
                .price(799)
                .salePercent(ICouponsService.get().getDiscount(getUser().getUniqueId()))
                .tags(isAdvanced() ? (new String[]{"Боевой пропуск", "Выполняйте задания, чтобы получать опыт", "Новый уровень - новая награда"}) :
                        (new String[]{"Боевой пропуск", "Выполняйте задания, чтобы", "получать опыт", "Новый уровень - новая награда"}))
                .pages(Fantasy.battlePassProvider().getPages(getPlayer()).toArray(BattlePassPageAdvanced[]::new))
                .onClickReward((player, advanced, itemIndex) -> {
                    var page = (itemIndex - 1) / 10 + 1;
                    var id = itemIndex - 1 - (page - 1) * 10;

                    if (this.isRewardApplied((itemIndex - 1) * (advanced ? 100 : 1))) return;

                    var rewards = advanced ? Fantasy.battlePassProvider().getAdvancedRewards() :  Fantasy.battlePassProvider().getBaseRewards();
                    var rewardExecutor = rewards.get(page).getRewardExecutor(id);
                    if (rewardExecutor == null) {
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Произошла ошибка при выдаче награды", "Попробуйте позже");
                    } else {
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                        rewardExecutor.execute(player, RewardSettings.builder().rewardId(itemIndex - 1).advanced(advanced).build());
                    }
                })
                .onBuyAdvanced(player -> {
                    var totalPrice = (int) Math.round(799 * (1 - this.getSale() / 100D));
                    Wada.get().getConfirmRequest().request(player, "&fПриобрести &6расширенный боевой пропуск &fза &b%d&r\uE03A".formatted(totalPrice), accepted -> {
                        if (!accepted) {
                            Anime.close(player);
                            Fantasy.battlePassProvider().open(player);
                            return;
                        }

                        if (totalPrice <= 0) {
                            Anime.close(player);
                            setAdvanced(true);
                            Fantasy.battlePassProvider().open(player);

                            Fantasy.databaseProvider().asyncCollection(Collections.USERS).thenAcceptAsync(collection -> {
                                collection.updateOne(Filters.eq("_id", player.getUniqueId().toString()), Updates.set("battlepass_advanced", true));
                            }).whenComplete((skip, throwable2) -> {
                                if (throwable2 != null) throwable2.printStackTrace();
                            });
                            return;
                        }

                        IInvoiceService.get().bill(player.getUniqueId(), Invoice.builder()
                                .price(totalPrice)
                                        .category(TransactionCategory.BATTLE_PASS)
                                        .description("battlepass-%s".formatted(Fantasy.currentSeason().getNumber()))
                                .allowBonuses(true)
                                .build()).whenComplete((invoiceResult, throwable) -> {
                            if (throwable != null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Произошла ошибка во время выполнения операции: " + throwable.getMessage());
                                throwable.printStackTrace();
                                return;
                            }

                            if (!invoiceResult.isSuccess()) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Произошла ошибка во время выполнения операции: " + invoiceResult.getErrorMessage());
                                return;
                            }

                            Anime.close(player);
                            setAdvanced(true);
                            Fantasy.battlePassProvider().open(player);
                            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.BATTLE_PASS_ADVANCED, Fantasy.currentSeason().getNumber(), Instant.now().toEpochMilli()));

                            Fantasy.databaseProvider().asyncCollection(Collections.USERS).thenAcceptAsync(collection -> {
                                collection.updateOne(Filters.eq("_id", player.getUniqueId().toString()), Updates.set("battlepass_advanced", true));
                            }).whenComplete((skip, throwable2) -> {
                                if (throwable2 != null) throwable2.printStackTrace();
                            });
                        });
                    });
                })
                .onBuyLevels((player, price, requiredExp) -> {
                    var remainder = requiredExp % EXP_REQUIREMENT;
                    var levels = (remainder > 0 ? 1 : 0) + requiredExp / EXP_REQUIREMENT;
                    Wada.get().getConfirmRequest().request(player, "&fПриобрести &6%d &f%s боевого пропуска за &6%d&r\uE104"
                            .formatted(levels, TextUtility.transformViaAmount(levels, "уровень", "уровня", "уровней"), price), accepted -> {
                        if (!accepted) {
                            Anime.close(player);
                            Fantasy.battlePassProvider().open(player);
                            return;
                        }
                        if (getUser().getCurrencyHandler().withdrawDonateCurrencyWithPriority(price)) {
                            Anime.close(player);
                            changeExp(requiredExp);
                            Fantasy.battlePassProvider().open(player);
                            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.BATTLE_PASS_LEVEL, levels, Instant.now().toEpochMilli()));
                        } else {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Произошла ошибка, попробуйте позже.");
                        }
                    });
                })
                .build();

        if (activeQuests.isEmpty()) {
            lastData.setQuests(List.of("Для Вашего максимального уровня заданий больше нет!"));
        } else {
            lastData.setQuests(TextUtility.colorize(getActiveQuests().stream().map(quest -> quest.getDescription(getPlayer())).toList()));
        }

        lastData.setOutstandingDiscount(false);
        lastData.setAutoСalculationLevel(true);
        lastData.setUuid(getUser().getUniqueId());

        this.lastSales = new ArrayList<>();
        for (int i = 1; i <= MAXIMUM_BATTLEPASS_LEVEL; i++) {
            this.lastSales.add(new BattlePassSale(i, 0, i >= 20 ? 40 : i >= 10 ? 30 : 20));
        }
    }

    @Override
    public List<Bson> createUpdates() {
        return List.of(
                Updates.set("battlepass_quests", Fantasy.gson().toJson(new ArrayList<>(getPassedQuests()), typeToken)),
                Updates.set("battlepass_exp", getCurrentExp()),
                Updates.set("battlepass_advanced", isAdvanced()),
                Updates.set("battlepass_rewards", Fantasy.gson().toJson(new ArrayList<>(getPassedRewards()), typeToken))
        );
    }

    @Override
    public void readDocument(Document document) {
        final List<Integer> passedQuests = Fantasy.gson().fromJson(document.get("battlepass_quests", ""), typeToken);
        if (passedQuests != null)
            getPassedQuests().addAll(passedQuests);

        final List<Integer> passedRewards = Fantasy.gson().fromJson(document.get("battlepass_rewards", ""), typeToken);
        if (passedRewards != null)
            getPassedRewards().addAll(passedRewards);

        var exp = document.get("battlepass_exp", 0);
        var advanced = document.get("battlepass_advanced", false);

        this.changeExp(exp);
        this.setAdvanced(advanced);
    }

}
