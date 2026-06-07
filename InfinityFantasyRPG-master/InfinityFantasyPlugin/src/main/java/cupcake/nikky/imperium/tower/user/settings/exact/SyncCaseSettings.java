package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.box.CaseSessionData;
import cupcake.nikky.imperium.tower.box.CaseType;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.CaseOpenPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.CaseRollPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseCurrency;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.protocol.packet.GameGlobalMessagePacket;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.rng.exact.box.CaseWrapper;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.permissions.IPermissionService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SyncCaseSettings extends BaseUserSettings {

    @Getter
    private CaseSessionData caseSessionData;

    public SyncCaseSettings(User user) {
        super(user);
    }

    public void openCase(@NotNull final CaseType caseType) {
        this.caseSessionData = new CaseSessionData(caseType);
        getUser().sendPacket(new CaseOpenPacket(caseType.getCaseData()));
    }

    public void openCase(@NotNull final CaseType caseType, final int amount) {
        this.caseSessionData = new CaseSessionData(caseType);
        this.caseSessionData.setCurrentAmount(amount);
        getUser().sendPacket(new CaseOpenPacket(caseType.getCaseData()));
    }

    public void setRollAmount(final int amount) {
        caseSessionData.setCurrentAmount(amount);
    }

    private boolean withdrawCurrency(@NotNull final CaseCurrency caseCurrency, final int amount) {
        return switch (caseCurrency) {
            case GRAN -> getUser().getCurrencyHandler().withdrawCurrency(CurrencyType.GRAN, amount);
            case SPECIAL -> getUser().getCurrencyHandler().withdrawDonateCurrencyWithPriority(amount);
            case MINES -> getUser().getCurrencyHandler().withdrawCurrency(CurrencyType.MINES, amount);
        };
    }

    public boolean roll() {
        var caseServerData = caseSessionData.getCaseType().getCaseServerData();
        if (!caseSessionData.getCaseType().tryRoll(getPlayer(), caseSessionData.getCurrentAmount())) return false;
        if (!this.withdrawCurrency(caseServerData.getCaseData().getCurrency(), caseServerData.getCaseData().getPrice() * getCaseSessionData().getCurrentAmount())) {
            Fantasy.clientProvider().notification(getPlayer(), SystemNotificationType.ERROR, "Недостаточно средств");
            return false;
        }

        final List<CaseWrapper<?>> winWrappers = new ArrayList<>();

        for (int i = 0; i < caseSessionData.getCurrentAmount(); i++) {
            var weight = caseServerData.getWeightPool().generatePool().getFirst();
            winWrappers.add(weight.weightWrapper());
            caseSessionData.getCaseType().getCaseReward().apply(getPlayer(), weight).reward(getPlayer());
        }

        winWrappers.forEach(weightCaseItemWrapper -> Fantasy.bukkitProvider().sendMessage(getPlayer(), ChatType.SYSTEM,
                "&fВы открыли &6%s &fи получили &6%s".formatted(caseSessionData.getCaseType().getCaseData().getTitle(), weightCaseItemWrapper.getTitle())
        ));

        val greatRewards = winWrappers.stream()
                .filter(weightCaseItemWrapper -> weightCaseItemWrapper.getCaseRarity() == CaseRarity.EDGE || weightCaseItemWrapper.getCaseRarity() == CaseRarity.UNIQUE)
                .toList();
        if (!greatRewards.isEmpty()) {
            IPermissionService.get().getPermissionContext(getUser().getUniqueId()).thenAccept(context -> {
                var name = context.toDisplayName();

                greatRewards.forEach(caseWrapper -> {
                    var message = "%s &fвыбил из кейса %s".formatted(name, caseWrapper.getTitle());
                    PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameGlobalMessagePacket(message));

                    Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION, message, Bukkit.getOnlinePlayers());
                });
            });
        }

        PacketRegistry.write(
                Fantasy.gameServer().nameSpace(),
                new UserDonateLogPacket(getUser().getUniqueId(), DonateType.CASE, caseSessionData.getCaseType().name(), Instant.now().toEpochMilli())
        );

        getUser().sendPacket(new CaseRollPacket(winWrappers.stream().mapToInt(CaseWrapper::getIndex).toArray()));
        Fantasy.runLater(() -> {
            if (getUser().isInvalidated()) return;
            this.openCase(caseSessionData.getCaseType(), caseSessionData.getCurrentAmount());
        }, 20 * 8);
        return true;
    }

}
