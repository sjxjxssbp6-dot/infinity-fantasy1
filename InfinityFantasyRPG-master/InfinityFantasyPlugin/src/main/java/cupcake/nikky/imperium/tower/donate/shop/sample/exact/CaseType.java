package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@Getter
@RequiredArgsConstructor
public enum CaseType {

    GENERAL("Обычный кейс", Material.CHEST),
    STAR("Звездный кейс", Material.ENDER_CHEST);

    private final String title;
    private final Material texture;

}
