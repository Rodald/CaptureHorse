package net.rodald.captureHorse.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChatEventListener implements Listener {
    public void onBlockFade(AsyncChatEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            int customModelData = meta.getCustomModelData();
            UsableItem usableItem = UsableItem.getItemByCustomModelData(customModelData);

            if (usableItem != null) {
                usableItem.handleChatMessage(event);
            }
        }
    }
}
