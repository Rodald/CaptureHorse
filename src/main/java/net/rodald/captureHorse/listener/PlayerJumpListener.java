package net.rodald.captureHorse.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJumpListener implements Listener {

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            int customModelData = meta.getCustomModelData();
            UsableItem usableItem = UsableItem.getItemByCustomModelData(customModelData);

            if (usableItem != null) {
                usableItem.handleJumpEvent(event);
            }
        }

    }
}
