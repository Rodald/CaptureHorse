package net.rodald.captureHorse.listener;

import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.mechanics.item.usableItem.IceSmash;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
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
                            String displayName = meta.getDisplayName();
                            UsableItem usableItem = UsableItem.getItemByName(ChatColor.RESET + displayName);

                            if (usableItem != null) {
                                applyPowderSnowEffectAroundPlayer(player);
                            }
                        }
                }
            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 20L); // Alle 20 Ticks (1 Sekunde)
    }

    private void applyPowderSnowEffectAroundPlayer(Player player) {
        for (Player nearbyPlayer : player.getWorld().getPlayers()) {
            if (nearbyPlayer.getLocation().distance(player.getLocation()) <= EFFECT_RADIUS) {

                if (Teams.getEntityTeam(player) != null && Teams.getEntityTeam(nearbyPlayer) != null) {
                    if (Teams.getEntityTeam(player) == Teams.getEntityTeam(nearbyPlayer)) {
                        continue;
                    }
                } else if (player == nearbyPlayer) {
                    continue;
                }


                nearbyPlayer.setFreezeTicks(200);
                nearbyPlayer.getWorld().spawnParticle(Particle.SNOWFLAKE, nearbyPlayer.getLocation(), 20, 1, 2, 1, 0);
            }
        }
    }
}
