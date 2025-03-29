package net.rodald.captureHorse.listener;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInventorySlotChangeListener implements Listener {

    @EventHandler
    private void onPlayerInventorySlotChange(PlayerInventorySlotChangeEvent event) {
        ItemStack itemStack = event.getOldItemStack();

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
