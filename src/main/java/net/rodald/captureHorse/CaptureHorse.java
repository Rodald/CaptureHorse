package net.rodald.captureHorse;

import net.rodald.captureHorse.command.WeaponsCommand;
import net.rodald.captureHorse.listener.*;
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
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockFadeListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInventorySlotChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJumpListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CaptureHorse getInstance() {
        return instance;
    }
}
