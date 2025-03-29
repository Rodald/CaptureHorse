package net.rodald.captureHorse.listener;

import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();


        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            int customModelData = meta.getCustomModelData();
            UsableItem usableItem = UsableItem.getItemByCustomModelData(customModelData);

            if (usableItem != null) {
                usableItem.onDisable(event.getPlayer());
            }
        }
    }
}
