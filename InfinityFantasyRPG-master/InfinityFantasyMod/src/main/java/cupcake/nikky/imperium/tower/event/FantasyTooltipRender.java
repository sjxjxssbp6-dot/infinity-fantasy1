package cupcake.nikky.imperium.tower.event;

import dev.xdark.clientapi.event.render.TooltipRender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FantasyTooltipRender implements TooltipRender {

    private final List<String> content;
    @Setter private boolean cancelled;

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public void setPos(int x, int y) {

    }

}
