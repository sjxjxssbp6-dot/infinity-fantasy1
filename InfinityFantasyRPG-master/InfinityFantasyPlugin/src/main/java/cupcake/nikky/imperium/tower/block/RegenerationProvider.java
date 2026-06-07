package cupcake.nikky.imperium.tower.block;

import lombok.val;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class RegenerationProvider {

    private final List<RegenerationBlockData> data = new CopyOnWriteArrayList<>();

    public RegenerationBlockData findByLocation(@NotNull final Location location) {
        return data.stream().filter(data -> data.getBlockLocation().equals(location)).findAny().orElse(null);
    }

    public Collection<RegenerationBlockData> findAll() {
        return new ArrayList<>(data);
    }

    public void registerBlock(@NotNull final Block block, @NotNull final Material material, final byte data, final int reloadInTicks) {
        this.data.add(new RegenerationBlockData(block, material, data, reloadInTicks));
        block.setType(Material.COBBLESTONE);
    }

    public boolean isRegistered(@NotNull final Block block) {
        return this.findByLocation(block.getLocation()) != null;
    }

    public void unregister(@NotNull final Block block) {
        val data = this.findByLocation(block.getLocation());

        block.setType(data.getBlockMaterial());
        block.setData(data.getBlockData());

        this.data.removeIf(data2 -> data2.getBlockLocation().equals(data.getBlockLocation()));
    }

    public void unregisterAll() {
        this.findAll().forEach(data -> this.unregister(data.getBlock()));
    }

}
