package cupcake.nikky.imperium.tower.data;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.ModifierList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CraftBenchStorage {

    @Getter
    private static final CraftBenchStorage instance = new CraftBenchStorage();

    @Getter private final Map<String, ModifierList> prefixes = new HashMap<>();
    @Getter private final Map<String, ModifierList> suffixes = new HashMap<>();

    public void reset() {
        this.prefixes.clear();
        this.suffixes.clear();
    }

}
