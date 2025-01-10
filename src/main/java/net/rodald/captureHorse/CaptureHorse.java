package net.rodald.captureHorse;

import net.rodald.captureHorse.listener.BlockFadeListener;
import net.rodald.captureHorse.listener.EntityDamageByEntityListener;
import net.rodald.captureHorse.listener.PlayerInteractListener;
import net.rodald.captureHorse.listener.SnowEffectListener;
import net.rodald.captureHorse.mechanics.item.usableItem.IceSmash;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CaptureHorse extends JavaPlugin {

    private static CaptureHorse instance;
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic

        new SnowEffectListener();
        getServer().getPluginManager().registerEvents(new BlockFadeListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        for (Player player : getServer().getOnlinePlayers()) {
            player.getInventory().addItem(new IceSmash().createItem());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CaptureHorse getInstance() {
        return instance;
    }
}
