package cupcake.nikky.imperium.tower.utility;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.permissions.GroupType;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.core.permissions.StaffGroups;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PermissionUtility {

    private static final List<UUID> whitelist = List.of(
            UUID.fromString("032c15b2-e926-11ed-b209-1cb72cb014ae"),
            UUID.fromString("7b4eeece-e51a-11ea-acca-1cb72caa35fd"),
            UUID.fromString("8429ce90-2970-11ee-82fd-a620890c036c"),
            UUID.fromString("91ed0fa0-b766-11ed-8757-9e83c44ef012"),
            UUID.fromString("b8819254-67c7-11ef-be97-1cb72cb014ae")
    );

    public static boolean isAttentiveRekt(@NotNull final UUID uuid) {
        return "602036a1-3255-11ed-98d1-1cb72caa35fd".equals(uuid.toString());
    }

    public static boolean isOperator(@NotNull final Player player) {
        return "c989d6ef-afed-11ea-acca-1cb72caa35fd".equals(player.getUniqueId().toString());
    }

    public static boolean isNiceTester(@NotNull final Player player) {
        return "df8f81b7-4200-11eb-acca-1cb72caa35fd".equals(player.getUniqueId().toString());
    }

    public static CompletableFuture<Boolean> isModerator(@NotNull final Player player) {
        if (isOperator(player) || Fantasy.gameServer().isTest() && whitelist.contains(player.getUniqueId())) return CompletableFuture.completedFuture(true);
        return IPermissionService.get().getPlayerGroup(player.getUniqueId(), GroupType.STAFF)
                .thenApply(group -> group.isInRangeOf(StaffGroups.MODERATOR, StaffGroups.OWNER));
    }

    public static CompletableFuture<Boolean> isSupporter(@NotNull final Player player) {
        if (isOperator(player) || Fantasy.gameServer().isTest() && whitelist.contains(player.getUniqueId())) return CompletableFuture.completedFuture(true);
        return IPermissionService.get().getPlayerGroup(player.getUniqueId(), GroupType.STAFF)
                .thenApply(group -> Fantasy.gameServer().isTest() && group.isInRangeOf(StaffGroups.TESTER, StaffGroups.OWNER));
    }

    public static CompletableFuture<Boolean> canJoin(@NotNull final UUID uuid) {
        if (!Fantasy.gameServer().isTest() || Fantasy.gameServer().isTest() && whitelist.contains(uuid))
            return CompletableFuture.completedFuture(true);
        return IPermissionService.get().getPlayerGroup(uuid, GroupType.STAFF).thenApply(group -> group.isInRangeOf(StaffGroups.TESTER, StaffGroups.OWNER));
    }

}
