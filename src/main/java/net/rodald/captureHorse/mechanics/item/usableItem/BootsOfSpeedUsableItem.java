package net.rodald.captureHorse.mechanics.item.usableItem;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class BootsOfSpeedUsableItem extends UsableItem {

    // amount of flesh charge
    private int charge;
    @Override
    public int getCooldown() {
        return 15;
    }

    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.setHealth(player.getHealth()-5);
        charge += 100;
        return true;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {

    }

    @Override
    public void handleTick(Player player) {

    }

    @Override
    public void handleJumpEvent(PlayerJumpEvent event) {

    }

    private void onSneak(Player player) {

    }

    @Override
    public void spawnParticles(Player player) {

    }

    @Override
    public void playSound(Player player) {

    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_BOOTS;
    }

    @Override
    public Component getItemName() {
        return Component.text("Boots Of Speed").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public ArrayList<Component> getItemLore() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));

        return lore;
    }

    @Override
    public int getCustomModelData() {
        return 258;
    }
}
