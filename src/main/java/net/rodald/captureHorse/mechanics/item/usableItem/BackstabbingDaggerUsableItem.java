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

public class BackstabbingDaggerUsableItem extends UsableItem {
    // amount of flesh charge
    private boolean backstab;
    private int charge;
    @Override
    public int getCooldown() {
        return 600;
    }

    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        return false;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {

    }

    @Override
    public void handleTick(Player player) {

    }


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
        return Material.IRON_SWORD;
    }

    @Override
    public Component getItemName() {
        return Component.text("Backstabbing Dagger").decoration(TextDecoration.ITALIC, false);
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
