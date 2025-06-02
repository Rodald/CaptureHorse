package net.rodald.captureHorse.mechanics.item.usableItem;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.mechanics.item.usableItem.SacrificeBlade.SacrificeBladePlayerState;
import net.rodald.captureHorse.mechanics.item.usableItem.monkStaff.MonkStaffPlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
    public int bloodCharge=0;
    private SacrificeBladePlayerState playerState = SacrificeBladePlayerState.CHARGING;
    @Override
    public boolean handleLeftClick(PlayerInteractEvent event){
        return false;
    }
    @Override

    public boolean handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.setHealth(player.getHealth()-2);
        Vector velocity = null;
        List<Entity> nearbyEntities;
        switch (playerState) {
            case SacrificeBladePlayerState.CHARGING:
                if (bloodCharge <= 96) {
                    bloodCharge +=4;
                }else {
                    bloodCharge = 100;
                }
                break;
            case SacrificeBladePlayerState.VAMPIRE:
                if (bloodCharge>=1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 3));
                    bloodCharge--;
                }

                break;
            case SacrificeBladePlayerState.BURST:
                nearbyEntities = player.getNearbyEntities(2, 2, 2);
                for (Entity entity : nearbyEntities) {
                    if (bloodCharge>=2) {
                        velocity.setX(0);
                        velocity.setY(3.5);
                        velocity.setZ(0);
                        bloodCharge -=2;
                    }
                }
                break;
            case SacrificeBladePlayerState.DARK_PALADIN:
                if (bloodCharge>=1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 5, 255));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5, 255));
                    bloodCharge--;
                }
                break;
            case SacrificeBladePlayerState.CURSES:
                nearbyEntities = player.getNearbyEntities(4, 4, 4);
                if (bloodCharge>=4) {
                    for (Entity entity : nearbyEntities) {
                        player.setHealth(player.getHealth()-0.5);
                        velocity.setX((Math.random()*4)-2);
                        velocity.setY(0.5);
                        velocity.setZ((Math.random()*4)-2);
                        entity.setVelocity(velocity);
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0));
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0));
                        bloodCharge -= 4;
                    }
                }
                break;
            case SacrificeBladePlayerState.BUFF:
                if (bloodCharge>=1) {
                    player.clearActivePotionEffects();
                    bloodCharge--;
                }
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
    @Override
    public void handleChatMessage(AsyncChatEvent event) {
        if (event.message().toString().contains("Sacrifice")&&event.message().toString().contains("Complete")) {
            randomMode();
            checkState();
        }
    }
    private void onSneak(Player player) {

    }
    private void randomMode() {
        bloodCharge -= 2;
        int rando = (int)Math.round(Math.random()*5);
        switch (rando){
            case 0:
                playerState = SacrificeBladePlayerState.BUFF;
                break;
            case 1:
                playerState = SacrificeBladePlayerState.CURSES;
                break;
            case 2:
                playerState = SacrificeBladePlayerState.BURST;
                break;
            case 3:
                playerState = SacrificeBladePlayerState.DARK_PALADIN;
                break;
            case 4:
                playerState = SacrificeBladePlayerState.VAMPIRE;
                break;

        }
    }

    @Override
    public void spawnParticles(Player player) {

    }

    @Override
    public void playSound(Player player) {

    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_SWORD;
    }

    private boolean checkState() {
        if (bloodCharge == 0 && playerState != SacrificeBladePlayerState.CHARGING) {
            playerState = SacrificeBladePlayerState.CHARGING;
            return true;
        } else if (bloodCharge == 100 && playerState == SacrificeBladePlayerState.CHARGING) {
            randomMode();
            return true;
        }
        return false;
    }
    @Override
    public Component getItemName() {
        return Component.text("Blade of Blood").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public ArrayList<Component> getItemLore() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));

        return lore;
    }

    @Override
    public int getCustomModelData() {
        return 260;
    }
}
