package cupcake.nikky.imperium.service.time;

import cupcake.nikky.imperium.service.Fantasy;
import cupcake.nikky.imperium.service.board.user.general.UserGeneralBoardService;
import cupcake.nikky.imperium.service.board.user.TopBoardService;
import cupcake.nikky.imperium.service.board.user.time.UserTopBinaBoardService;
import cupcake.nikky.imperium.service.board.user.time.UserTopHochmaBoardService;
import cupcake.nikky.imperium.tower.protocol.board.PlayerTimeData;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.protocol.top.UserGeneralScoreSearcher;
import cupcake.nikky.imperium.tower.protocol.top.UserTimeSearcher;
import lombok.val;
import ru.cristalix.core.permissions.IPermissionService;

import java.util.ArrayList;

public class BoardScheduler implements Runnable {

    @Override
    public void run() {
        UserGeneralBoardService.getInstance().clearAll();

        UserGeneralScoreSearcher.getInstance().findGeneralTop(Fantasy.gameServer()).thenApplyAsync(result -> {
            val keys = new ArrayList<>(result.keySet());

            return IPermissionService.get().getPermissionContextList(keys)
                    .thenAccept(list -> list.forEach(iPermissionContext -> UserGeneralBoardService.getInstance().applyName(iPermissionContext.getId(), iPermissionContext.toDisplayName())))
                    .thenAccept(unused -> {
                        result.forEach((uuid, globalScore) ->  UserGeneralBoardService.getInstance().applyData(uuid, globalScore));
                        UserGeneralBoardService.getInstance().sendPacket();
                    });
        }).whenComplete((v, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        });

        Object[][] instances = {{TimeType.BINA, UserTopBinaBoardService.getInstance()}, {TimeType.HOCHMA, UserTopHochmaBoardService.getInstance()}};

        for (Object[] instance : instances) {
            val type = (TimeType) instance[0];
            val service = (TopBoardService<PlayerTimeData>) instance[1];
            service.clearAll();

            UserTimeSearcher.getInstance().find(Fantasy.gameServer(), type).thenApplyAsync(result -> {
                val keys = new ArrayList<>(result.keySet());

                return IPermissionService.get().getPermissionContextList(keys)
                        .thenAccept(list -> list.forEach(iPermissionContext -> service.applyName(iPermissionContext.getId(), iPermissionContext.toDisplayName())))
                        .thenAccept(unused-> {
                            result.forEach(service::applyData);
                            service.sendPacket();
                        });
            }).whenComplete((v, throwable) -> {
                if (throwable != null)
                    throwable.printStackTrace();
            });
        }
    }

}
