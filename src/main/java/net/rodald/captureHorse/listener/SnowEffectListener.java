package net.rodald.captureHorse.listener;

import net.kyori.adventure.text.Component;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 20L); // Alle 20 Ticks (1 Sekunde)
    }

    private void applyPowderSnowEffectAroundPlayer(Player player) {
        for (FallingBlock fallingBlock : player.getWorld().getEntitiesByClass(FallingBlock.class)) {
            for (Entity nearbyEntity : player.getWorld().getEntities()) {
                if (nearbyEntity instanceof FallingBlock) {
                    continue;
                }

                if (nearbyEntity instanceof Player nearbyPlayer) {
                    if (Teams.getEntityTeam(player) != null && Teams.getEntityTeam(nearbyPlayer) != null) {
                        if (Teams.getEntityTeam(player) == Teams.getEntityTeam(nearbyPlayer)) {
                            continue;
                        }
                    } else if (player == nearbyPlayer) {
                        continue;
                    }
                }

                if (fallingBlock.getLocation().distance(nearbyEntity.getLocation()) < 1) {
                    nearbyEntity.setFreezeTicks(200);
                    nearbyEntity.getWorld().spawnParticle(Particle.SNOWFLAKE, nearbyEntity.getLocation(), 20, 1, 2, 1, 0);
                    fallingBlock.remove();
                }
            }
        }
    }
}
