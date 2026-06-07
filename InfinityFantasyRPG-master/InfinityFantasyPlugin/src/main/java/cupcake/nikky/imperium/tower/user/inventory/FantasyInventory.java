package cupcake.nikky.imperium.tower.user.inventory;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class FantasyInventory {

    private final User user;

    private AtomicReference<ImperiumItem> heldItem = new AtomicReference<>();

    private AtomicReference<ImperiumItem> helmet = new AtomicReference<>();
    private AtomicReference<ImperiumItem> body = new AtomicReference<>();
    private AtomicReference<ImperiumItem> leggings = new AtomicReference<>();
    private AtomicReference<ImperiumItem> boots = new AtomicReference<>();

    private AtomicBoolean updateArmor = new AtomicBoolean(true);
    private AtomicInteger armorCheck = new AtomicInteger(2);

    private AtomicBoolean updateHeldItem = new AtomicBoolean(true);
    private AtomicInteger heldCheck = new AtomicInteger(2);

    public void updateAll() {
        this.updateAll(user.getSpigot());
    }

    public void updateHeldItem() {
        this.updateHeldItem(user.getSpigot());
    }

    public void updateArmor() {
        this.updateArmor(user.getSpigot());
        this.setArmorCheck(this.getArmorCheck() - 1);
        if (this.getArmorCheck() == 0) this.setUpdateArmor(false);
    }

    public void updateHeldItem(@NotNull final Player player) {
        this.setHeldItem(Fantasy.itemProvider().find(player.getInventory().getItemInMainHand()));
        this.setHeldCheck(this.getHeldCheck() - 1);
        if (this.getHeldCheck() == 0) this.setUpdateHeldItem(false);
    }

    public void updateArmor(@NotNull final Player player) {
        this.setHelmet(Fantasy.itemProvider().find(player.getInventory().getHelmet()));
        this.setBody(Fantasy.itemProvider().find(player.getInventory().getChestplate()));
        this.setLeggings(Fantasy.itemProvider().find(player.getInventory().getLeggings()));
        this.setBoots(Fantasy.itemProvider().find(player.getInventory().getBoots()));
    }

    public void updateAll(@NotNull final Player player) {
        this.setHeldItem(Fantasy.itemProvider().find(player.getInventory().getItemInMainHand()));
        this.setHelmet(Fantasy.itemProvider().find(player.getInventory().getHelmet()));
        this.setBody(Fantasy.itemProvider().find(player.getInventory().getChestplate()));
        this.setLeggings(Fantasy.itemProvider().find(player.getInventory().getLeggings()));
        this.setBoots(Fantasy.itemProvider().find(player.getInventory().getBoots()));
    }

    public int getAverageArmorLevel() {
        var sumLevel = 0;
        for (ImperiumItem item : new ImperiumItem[] {getBoots(), getLeggings(), getBody(), getHelmet()}) {
            sumLevel += item == null ? 0 : item.getGeneralSettings().itemLevel();
        }

        return sumLevel / 4;
    }

    public void setArmorCheck(int armorCheck) {
        this.armorCheck.set(armorCheck);
    }

    public int getArmorCheck() {
        return armorCheck.get();
    }

    public void setHeldCheck(int heldCheck) {
        this.heldCheck.set(heldCheck);
    }

    public int getHeldCheck() {
        return heldCheck.get();
    }

    public void setUpdateHeldItem(final boolean value) {
        this.updateHeldItem.set(value);
        if (value) this.heldCheck.set(2);
    }

    public void setUpdateArmor(final boolean value) {
        this.updateArmor.set(value);
        if (value) this.armorCheck.set(2);
    }

    public boolean isUpdateArmor() {
        return updateArmor.get();
    }

    public boolean isUpdateHeldItem() {
        return updateHeldItem.get();
    }

    public ImperiumItem getHeldItem() {
        return heldItem.get();
    }

    public void setHeldItem(ImperiumItem heldItem) {
        this.heldItem.set(heldItem);
    }

    public ImperiumItem getHelmet() {
        return helmet.get();
    }

    public void setHelmet(ImperiumItem helmet) {
        this.helmet.set(helmet);
    }

    public ImperiumItem getBody() {
        return body.get();
    }

    public void setBody(ImperiumItem body) {
        this.body.set(body);
    }

    public ImperiumItem getLeggings() {
        return leggings.get();
    }

    public void setLeggings(ImperiumItem leggings) {
        this.leggings.set(leggings);
    }

    public ImperiumItem getBoots() {
        return boots.get();
    }

    public void setBoots(ImperiumItem boots) {
        this.boots.set(boots);
    }

}
