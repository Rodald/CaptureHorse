package net.rodald.captureHorse.listener;

import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FallingBlockLandListener implements Listener {
    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        Entity ent = event.getEntity();
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            if (event.getBlockData().getMaterial() == Material.FROSTED_ICE) {
                UsableItem usableItem = UsableItem.getItemByCustomModelData(255);
                usableItem.handleFallingBlockLand(event);
            }
        }
    }
}
