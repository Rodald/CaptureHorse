package net.rodald.captureHorse.mechanics.item.usableItem;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.mechanics.item.usableItem.SacrificeBlade.SacrificeBladePlayerState;
import net.rodald.captureHorse.mechanics.item.usableItem.monkStaff.MonkStaffPlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BladeOfSacrificeUsableItem extends UsableItem {
    @Override
    public int getCooldown() {
        return 15;
    }
    private SacrificeBladePlayerState playerState = SacrificeBladePlayerState.CHARGING;
    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.setHealth(player.getHealth()-2);
        switch (playerState) {
            case SacrificeBladePlayerState.CHARGING:
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5, 2));
                break;
            case SacrificeBladePlayerState.VAMPIRE:
                break;
            case SacrificeBladePlayerState.BURST:
                List<Entity> nearbyEntities = player.getNearbyEntities(2, 2, 2);
                Vector velocity = null;
                for (Entity entity : nearbyEntities) {

                    velocity.setX(0);
                    velocity.setY(3.5);
                    velocity.setZ(0);
                    entity.setVelocity(velocity);
                }
                break;
            case SacrificeBladePlayerState.DARK_PALADIN:
                break;
            case SacrificeBladePlayerState.CURSES:
                break;
        }
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
        return 259;
    }
}
