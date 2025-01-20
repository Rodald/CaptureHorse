package net.rodald.captureHorse.mechanics.item.usableItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class KatanaUsableItem extends UsableItem {
    private int selectedSlot;
    @Override
    public Material getMaterial() {
        return Material.DIAMOND_AXE;
    }
    @Override
    public Component getItemName() {
        return applyGradient("Katana", TextColor.color(0xDAE8E7), TextColor.color(0xAAAAB5));
    }

    @Override
    public ArrayList<Component> getItemLore() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Ice Hammer Description.", NamedTextColor.GRAY));
        return lore;
    }

    @Override
    public int getCooldown() { return 600; };


    @Override
    protected void prepareItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }
    @Override
    public void handleRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent e) {

    }

    @Override
    public void handleTick(Player player) {
        selectedSlot = player.getInventory().getHeldItemSlot();
    }

    @Override
    public void spawnParticles(Player p) {

    }

    @Override
    public void playSound(Player p) {

    }
}
