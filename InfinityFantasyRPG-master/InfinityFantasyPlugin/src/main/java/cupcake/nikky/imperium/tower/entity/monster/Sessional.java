package cupcake.nikky.imperium.tower.entity.monster;

import cupcake.nikky.imperium.tower.session.Session;
import ru.cristalix.wada.module.border.WorldBorder;

public interface Sessional {

    void setSession(Session session);

    Session getSession();

    void setWorldBorder(WorldBorder worldBorder);

    WorldBorder getWorldBorder();

}
