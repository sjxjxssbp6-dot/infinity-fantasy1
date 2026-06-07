package cupcake.nikky.imperium.tower.item.generator.description;

import cupcake.nikky.imperium.tower.item.generator.description.exact.AttackInformationProcess;
import cupcake.nikky.imperium.tower.item.generator.description.exact.AttributeInformationProcess;
import cupcake.nikky.imperium.tower.item.generator.description.exact.BottomInformationProcess;
import cupcake.nikky.imperium.tower.item.generator.description.exact.TopInformationProcess;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DescriptionProvider {

    @Getter
    private static final DescriptionProvider instance = new DescriptionProvider();

    private final DescriptionProcess[] storage = {
            new TopInformationProcess(),
            new AttackInformationProcess(),
            new AttributeInformationProcess(),
            new BottomInformationProcess()
    };

    public void process(@NotNull final DescriptionContainer container) {
        for (DescriptionProcess descriptionProcess : storage) {
            descriptionProcess.process(container);
        }
    }

}
