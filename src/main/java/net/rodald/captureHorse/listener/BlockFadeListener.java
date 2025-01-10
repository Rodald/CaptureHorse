package net.rodald.captureHorse.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockFadeListener implements Listener {
    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (event.getBlock().getType() == Material.FROSTED_ICE) {
            event.setCancelled(true);


            Ageable block = (Ageable) event.getBlock().getBlockData();
            if (block.getAge() == 3) {
                Location location = event.getBlock().getLocation();
                location.getWorld().getBlockAt(location).setType(Material.AIR);
            }
        }
    }

}
