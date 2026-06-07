package pw.lach.p13n.network.tower;

import lombok.*;
import ru.cristalix.core.network.CorePackage;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiveModelToUserPackage extends CorePackage {

    private UUID user;
    private UUID model;

}
