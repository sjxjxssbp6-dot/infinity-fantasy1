package cupcake.nikky.imperium.tower.character;

import cupcake.nikky.imperium.tower.character.ineract.DefaultInteract;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.conversation.data.NpcSmart;
import me.func.mod.world.Npc;
import me.func.protocol.world.npc.NpcBehaviour;
import me.func.protocol.world.npc.NpcData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ImperiumCharacter {

    @Getter private final int npcId;
    @Getter @Setter private int entityId;
    @Getter @Setter private NPCs type;
    @Getter @Setter private DefaultInteract interacting;
    private final ArrayList<Integer> quests = new ArrayList<>();
    private final Set<UUID> personalization;

    @Getter private final NpcData data = new NpcData();
    private NpcSmart smart;

    public ImperiumCharacter(int npcId, final Set<UUID> personalization) {
        this.npcId = npcId;
        this.personalization = personalization;
    }

    public void updateData(@NotNull final NPCs type) {
        this.setType(type);

        data.setBehaviour(NpcBehaviour.STARE_AT_PLAYER);
        entityId = data.getId();
    }

    public void initializeSmartNpc(@NotNull final Location location) {
        if (smart != null) {
            throw new RuntimeException("Attempt to spawn already initialized character");
        }

        if (!location.isChunkLoaded()) location.getChunk().load(false);

        val clone = new NpcData();
        clone.setId(data.getId());
        clone.setBehaviour(data.getBehaviour());
        clone.setName(data.getName());
        clone.setSkinValue(data.getSkinValue());
        clone.setSkinSignature(data.getSkinSignature());
        clone.setSkinUrl(data.getSkinUrl());

        Npc.INSTANCE.location(clone, location);

        this.smart = Npc.link(clone);
        smart.setPersonalization(personalization);
        smart.setWorld(location.getWorld());
        smart.setWorldUuid(location.getWorld().getUID());
    }

    public Location findLocation() {
        return new Location(Bukkit.getWorld(smart.getWorldUuid()), getData().getX(), getData().getY(), getData().getZ());
    }

    public void onConnect(@NotNull final Player player) {
        //smart.update(player);
    }

    public List<Integer> getStoredQuests() {
        return new ArrayList<>(quests);
    }

    public void addQuests(Collection<Integer> ids) {
        this.quests.addAll(ids);
    }

}
