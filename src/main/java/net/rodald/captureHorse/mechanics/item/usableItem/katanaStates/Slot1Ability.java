package net.rodald.captureHorse.mechanics.item.usableItem.katanaStates;

import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.interfaces.KatanaAbility;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class Slot1Ability implements KatanaAbility {

    @Override
    public String getAbilityName() {
        return "Dash Strike";
    }

    @Override
    public long getCooldown() {
        return 80;
    }

    @Override
    public void prepareItem(ItemStack item) {

    }

    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Vector direction = player.getLocation().getDirection().normalize().multiply(1.5);
        player.setVelocity(direction);

        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 10, 0.5, 2, 0.5, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;
                if (ticks > 10) {
                    cancel();
                    return;
                }

                List<Entity> nearbyEntities = player.getNearbyEntities(1.0, 1.0, 1.0);

                for (Entity entity : nearbyEntities) {
                    if (!(entity instanceof LivingEntity target) || entity == player) {
                        continue;
                    }

                    Vector knockback = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
                    player.setVelocity(knockback.multiply(0.25));

                    Vector targetKnockback = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                    target.setVelocity(targetKnockback.multiply(0.5));

                    target.damage(4.0, player);
                    target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 10, 0.3, 0.3, 0.3, 0.1);
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);

                    cancel();
                    return;
                }
            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 1L);
        return true;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {

        Entity player = event.getDamager();

        Vector direction = player.getLocation().getDirection().normalize().multiply(1.5);
        player.setVelocity(direction);

        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 10, 0.5, 2, 0.5, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;
                if (ticks > 10) {
                    cancel();
                    return;
                }

                List<Entity> nearbyEntities = player.getNearbyEntities(1.0, 1.0, 1.0);

                for (Entity entity : nearbyEntities) {
                    if (!(entity instanceof LivingEntity target) || entity == player) {
                        continue;
                    }
                    cancel();
                    return;
                }
            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 1L);
    }

    @Override
    public void handleTick(Player player) {

    }

}
