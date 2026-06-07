package cupcake.nikky.imperium.tower.box.world;

import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.box.CaseType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.worldtext.WorldText;
import ru.cristalix.wada.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CaseWorldProvider {

    @Getter
    private static final CaseWorldProvider instance = new CaseWorldProvider();

    @Getter
    private final Map<Location, Pair<CaseType, BlockFace>> cases = Map.of(
            new Location(MapServiceProcess.GAME_WORLD, -4, 99, 36, 0, 0), new Pair<>(CaseType.GENERAL_ITEMS, BlockFace.EAST),
            new Location(MapServiceProcess.GAME_WORLD, -5, 99, 33, 0, 0), new Pair<>(CaseType.STAR_ITEMS, BlockFace.EAST),
            new Location(MapServiceProcess.GAME_WORLD, -6, 99, 30, 0, 0), new Pair<>(CaseType.BASE_VISUAL, BlockFace.EAST)
    );

    @Getter
    private final List<WorldText> worldTexts = new ArrayList<>();

    public void initialize() {
        getCases().forEach((location, caseType) -> {
            var worldTextModule = Wada.get().getWorldTextModule();
            var caseData = caseType.getFirst().getCaseData();
            var caseText = worldTextModule.createWorldText(
                    "%s\n%d%s".formatted(caseData.getTitle(), caseData.getPrice(), caseData.getCurrency().getIcon()),
                    location.clone().toCenterLocation().add(0, 1.5, 0),
                    true,
                    Color.BLACK.alpha(.62)
            );
            worldTexts.add(caseText);
        });
    }

}
