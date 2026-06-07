package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.data.FragmentStorage;
import lombok.Getter;
import lombok.val;
import ru.cristalix.enginex.element.HorizontalLayout;
import ru.cristalix.enginex.util.Relative;

import java.time.Instant;

public class AdvancedHotBarBox extends HorizontalLayout {

    @Getter
    private final DataBox fragmentTimeBox = new DataBox();
    @Getter
    private final DataBox fragmentRespawnBox = new DataBox();

    public AdvancedHotBarBox() {
        super(5D);

        this.initialize();
    }

    private void initialize() {
        this.fragmentTimeBox.updateText(FragmentStorage.getInstance().toStringTime() + "\uE0C4");
        this.fragmentRespawnBox.updateText(FragmentStorage.getInstance().getRespawns() + "\uE19C");

        this.onPreTick(element -> {
            val value = FragmentStorage.getInstance().getTime() > Instant.now().toEpochMilli();
            this.fragmentTimeBox.setEnabled(value);
            this.fragmentRespawnBox.setEnabled(value);
        });

        this.setOrigin(Relative.LEFT).setAlign(Relative.LEFT).addChild(fragmentRespawnBox, fragmentTimeBox);
    }

    public void update() {
        this.fragmentTimeBox.updateText(FragmentStorage.getInstance().toStringTime() + "\uE0C4");
        this.fragmentRespawnBox.updateText(FragmentStorage.getInstance().getRespawns() + "\uE19C");
    }

    public boolean isExist() {
        return fragmentTimeBox.isEnabled() || fragmentRespawnBox.isEnabled();
    }

}
