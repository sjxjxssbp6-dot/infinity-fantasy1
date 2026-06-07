package cupcake.nikky.imperium.tower.box.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.blood.BloodObject;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseCurrency;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.death.DeathObject;
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.exact.box.CaseWrapper;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;

@RequiredArgsConstructor
public class CaseMetadataReward implements CaseReward {

    private final Weight<CaseWrapper<?>> weight;
    private final int duplicateReward;
    private final CaseCurrency caseCurrency;

    @Override
    public void reward(Player player) {
        var wrapper = weight.weightWrapper();
        var wrapped = wrapper.getWrapped();

        var metadata = switch (wrapped) {
            case AuraObject auraObject -> auraObject.name();
            case DeathObject deathObject -> deathObject.name();
            case BloodObject bloodObject -> bloodObject.name();
            case null, default -> null;
        };

        if (metadata == null) {
            Bukkit.getLogger().warning("Attempt to give null metadata case reward");
            return;
        }

        var donateData =player.<User>getBungeePlayer().findDonateSettings().donateData();
        if (donateData.containsMetadata(metadata)) {
            player.<User>getBungeePlayer().getCurrencyHandler().depositCurrency(convertCaseCurrency(), duplicateReward);
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВместо дубликата Вы получаете %d%s".formatted(duplicateReward, caseCurrency.getIcon()));
        } else {
            donateData.applyMetadata(metadata, Instant.now().toEpochMilli());
        }
    }

    private CurrencyType convertCaseCurrency() {
        return switch (caseCurrency) {
            case GRAN -> CurrencyType.GRAN;
            case SPECIAL -> CurrencyType.FIRM_PRIVATE;
            case MINES -> CurrencyType.MINES;
        };
    }

}
