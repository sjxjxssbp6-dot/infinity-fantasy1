package cupcake.nikky.imperium.tower.protocol;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import ru.cristalix.core.realm.IRealmService;
import ru.cristalix.core.realm.RealmId;

@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Service {

    private static final String REALM_TYPE = "INFS";
    private static final int REALM_ID = 1;

    private static boolean checkedType = false;
    private static boolean isTest;

    public static RealmId toRealm() {
        if (!checkedType) {
            val currentRealmId = IRealmService.get().getCurrentRealmInfo().getRealmId();
            isTest = currentRealmId.getId() < 0 || currentRealmId.getTypeName().equals("TEST");
            checkedType = true;
        }
        return RealmId.of(REALM_TYPE, isTest ? -REALM_ID : REALM_ID);
    }

}
