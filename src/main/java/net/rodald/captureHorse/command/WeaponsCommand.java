package net.rodald.captureHorse.command;

import net.rodald.captureHorse.mechanics.item.usableItem.IceSmashUsableItem;
import net.rodald.captureHorse.mechanics.item.usableItem.KatanaUsableItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WeaponsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            player.getInventory().addItem(new IceSmashUsableItem().createItem());
            player.getInventory().addItem(new KatanaUsableItem().createItem());
        }

        return true;
    }
}
