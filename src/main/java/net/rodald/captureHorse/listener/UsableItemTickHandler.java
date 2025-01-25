package net.rodald.captureHorse.listener;

import net.kyori.adventure.text.Component;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class UsableItemTickHandler {

    public UsableItemTickHandler() {
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
                            int customModelData = meta.getCustomModelData();
                            UsableItem usableItem = UsableItem.getItemByCustomModelData(customModelData);

                            if (usableItem != null) {
                                usableItem.handleItemTick(player);
                            }
                        }
                }
            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 1L);
    }
}
