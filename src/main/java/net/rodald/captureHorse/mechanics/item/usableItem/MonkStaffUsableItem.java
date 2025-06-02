package net.rodald.captureHorse.mechanics.item.usableItem;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.mechanics.item.usableItem.monkStaff.MonkStaffPlayerState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class MonkStaffUsableItem extends UsableItem {

    // chargeable hop distance
    private double hopAmount;

    // Default state = NONE
    private MonkStaffPlayerState playerState = MonkStaffPlayerState.NONE;
    private final int MAX_HOPS = 5;
    private final int DELAY_MEDITATION = 300;
    private final int BUFFS_MEDITATION = 600;
    @Override
    public boolean handleLeftClick(PlayerInteractEvent event){
        return false;
    }
    @Override
    public int getCooldown() {
        return 15;
    }

    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround()) return false;
        if (hopAmount != MAX_HOPS) return false;

        playerState = MonkStaffPlayerState.MEDITATION;
        player.setAllowFlight(true);
        Location location = player.getLocation();

        // stores players old gravity
        double oldGravity = player.getAttribute(Attribute.GRAVITY).getBaseValue();
        player.getAttribute(Attribute.GRAVITY).setBaseValue(0);
        new BukkitRunnable() {
            private int ticks;

            @Override
            public void run() {
                player.teleport(location);
                if (ticks >= DELAY_MEDITATION) {
                    player.setAllowFlight(false);
                    player.clearActivePotionEffects();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, BUFFS_MEDITATION, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, BUFFS_MEDITATION, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, BUFFS_MEDITATION, 1));
                    this.cancel();
                    return;
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 255));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 255));
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 600, 255));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 600, 255));
                ticks++;
            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0, 1);

        // reset player gravity to old one
        player.getAttribute(Attribute.GRAVITY).setBaseValue(oldGravity);

        playerState = MonkStaffPlayerState.NONE;
        hopAmount = 0;
        return true;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();

        if (victim instanceof LivingEntity livingEntity) {
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10, 0));
        }
    }

    @Override
    public void handleTick(Player player) {
        if (player.isSneaking()) {
            onSneak(player);
        } else if (player.isOnGround() && playerState != MonkStaffPlayerState.MEDITATION) {
            hopAmount = 0;
            playerState = MonkStaffPlayerState.NONE;
        } else if (playerState == MonkStaffPlayerState.AIR_HOPS_ACTIVE) {
            playerState = MonkStaffPlayerState.AIR_HOPS_INACTIVE;
        }
    }

    @Override
    public void handleJumpEvent(PlayerJumpEvent event) {
        Player player = event.getPlayer();

        if (hopAmount != 0 && player.isSneaking()) {
            player.setVelocity(player.getLocation().getDirection().normalize().multiply((-(50 / (hopAmount + 10))) + 5));
            hopAmount = 0;
            playerState = MonkStaffPlayerState.NONE;
        } else if (hopAmount == 0 && !player.isSneaking()) {
            hopAmount = MAX_HOPS;
            playerState = MonkStaffPlayerState.AIR_HOPS_INACTIVE;
        }

        if (!player.isSneaking()) return;

        if (hopAmount > 1) {


        }
    }

    private void onSneak(Player player) {
        if (player.isOnGround() && (playerState == MonkStaffPlayerState.LONG_JUMP || playerState == MonkStaffPlayerState.NONE)) {
            hopAmount += 0.5;
            playerState = MonkStaffPlayerState.LONG_JUMP;
        } else if (!player.isOnGround() && playerState == MonkStaffPlayerState.AIR_HOPS_INACTIVE && hopAmount > 0) {
            playerState = MonkStaffPlayerState.AIR_HOPS_ACTIVE;
            hopAmount = hopAmount - 1;
            Vector currentVelocity = player.getVelocity();
            currentVelocity.setY((player.getAttribute(Attribute.JUMP_STRENGTH).getValue())/2);
            player.setVelocity(currentVelocity);
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
        return Material.STICK;
    }

    @Override
    public Component getItemName() {
        return Component.text("Monk Staff").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public ArrayList<Component> getItemLore() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));

        return lore;
    }

    @Override
    public int getCustomModelData() {
        return 257;
    }
}
