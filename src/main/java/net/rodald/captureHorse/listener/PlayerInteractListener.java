package net.rodald.captureHorse.listener;

import net.kyori.adventure.text.Component;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack itemStack = e.getItem();


        if (itemStack != null) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                int customModelData = meta.getCustomModelData();
                UsableItem usableItem = UsableItem.getItemByCustomModelData(customModelData);

                if (usableItem != null) {
                    usableItem.handleItemAction(e);
                    e.setCancelled(true);
                }
            }
        }
    }
}
