package net.rodald.captureHorse.listener;

import net.kyori.adventure.text.Component;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class SnowEffectListener {

    private static final double EFFECT_RADIUS = 10.0;


    public SnowEffectListener() {
        startCheckingForItem();
    }

    private void startCheckingForItem() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                        ItemMeta meta = itemStack.getItemMeta();
                        if (meta != null && meta.hasDisplayName()) {
                            Component displayName = meta.displayName();
                            UsableItem usableItem = UsableItem.getItemByName(displayName);

                            if (usableItem != null) {
                                applyPowderSnowEffectAroundPlayer(player);
                            }
                        }
                }
            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 1L); // Alle 20 Ticks (1 Sekunde)
    }

    private void applyPowderSnowEffectAroundPlayer(Player player) {
        // Hole alle FallingBlocks in der Welt
        for (FallingBlock fallingBlock : player.getWorld().getEntitiesByClass(FallingBlock.class)) {
            // Stelle sicher, dass es Frosted Ice ist
            if (fallingBlock.getBlockData().getMaterial() != Material.FROSTED_ICE) {
                continue;
            }

            // Prüfe, ob der FallingBlock ein Entity berührt
            for (Entity nearbyEntity : fallingBlock.getNearbyEntities(2, 2, 2)) {
                if (nearbyEntity instanceof LivingEntity livingEntity) {
                    // Überprüfe, ob das Entity ein Spieler ist und zu einem Team gehört
                    if (nearbyEntity instanceof Player nearbyPlayer) {
                        if (Teams.getEntityTeam(player) != null && Teams.getEntityTeam(nearbyPlayer) != null) {
                            if (Teams.getEntityTeam(player).equals(Teams.getEntityTeam(nearbyPlayer))) {
                                continue; // Gleiche Teams ignorieren
                            }
                        } else if (player == nearbyPlayer) {
                            continue; // Spieler selbst ignorieren
                        } else {
                            continue;
                        }
                    }

                    // Wende den Eis-Effekt an
                    if (!(livingEntity.getFreezeTicks() > 0)) {
                        fallingBlock.remove();

                        livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                        livingEntity.getWorld().spawnParticle(Particle.SNOWFLAKE, livingEntity.getLocation(), 20, 1, 2, 1, 0);
                        livingEntity.setFreezeTicks(400); // 20 Ticks = 1 Sekunde
                    }
                    livingEntity.setFreezeTicks(400); // 20 Ticks = 1 Sekunde

                    // Entferne den FallingBlock nach der Kollision
                    break; // Nur eine Entität pro FallingBlock behandeln
                }
            }
        }
    }


}
