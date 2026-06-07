package cupcake.nikky.imperium.tower.bootstrap.exact.disable;

import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.invoice.IInvoiceService;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.core.realm.IRealmService;
import ru.cristalix.core.scoreboard.IScoreboardService;

public class CristalixServiceDisableProcess implements BootstrapProcess {

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        CoreApi coreApi = CoreApi.get();
        coreApi.unregisterService(IInvoiceService.class);
        coreApi.unregisterService(IRealmService.class);
        coreApi.unregisterService(IScoreboardService.class);
        coreApi.unregisterService(IPermissionService.class);
        coreApi.stop();
    }

}
