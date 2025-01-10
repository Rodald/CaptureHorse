package net.rodald.captureHorse.listener;

import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                Bukkit.broadcastMessage("EntityDamageByEntity");
                String displayName = meta.getDisplayName();
                UsableItem usableItem = UsableItem.getItemByName(ChatColor.RESET + displayName);

                if (usableItem != null) {
                    usableItem.handleEntityDamageByEntity(e);
                }
            }
        }
    }
}
