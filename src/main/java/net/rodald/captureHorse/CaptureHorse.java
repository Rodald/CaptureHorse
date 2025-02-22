package net.rodald.captureHorse;

import net.rodald.captureHorse.command.WeaponsCommand;
import net.rodald.captureHorse.listener.BlockFadeListener;
import net.rodald.captureHorse.listener.EntityDamageByEntityListener;
import net.rodald.captureHorse.listener.PlayerInteractListener;
import net.rodald.captureHorse.listener.UsableItemTickHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CaptureHorse extends JavaPlugin {

    private static CaptureHorse instance;
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        Objects.requireNonNull(getCommand("weapon")).setExecutor(new WeaponsCommand());

        new UsableItemTickHandler();
        getServer().getPluginManager().registerEvents(new BlockFadeListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CaptureHorse getInstance() {
        return instance;
    }
}
