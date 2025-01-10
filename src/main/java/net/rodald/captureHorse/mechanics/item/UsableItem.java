package net.rodald.captureHorse.mechanics.item;

import net.rodald.captureHorse.mechanics.Item;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class UsableItem extends Item {
    // Eine Map, die alle registrierten Item enthält
    private static final Map<String, UsableItem> usableItems = new HashMap<>();
    public UsableItem() {
        usableItems.put(ChatColor.RESET + getItemName(), this);
    }
    public boolean clearItemOnUse() { return false; };
    public abstract void handleRightClick(PlayerInteractEvent e);
    public abstract void handleAttack(EntityDamageByEntityEvent e);

    public abstract void spawnParticles(Player p);
    public abstract void playSound(Player p);
    
    // Methode zum Behandeln der Item-Action
    public void handleItemAction(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (clearItemOnUse()) {
            int amount = item.getAmount() - 1;
            item.setAmount(amount);
            p.getInventory().setItemInMainHand(amount > 0 ? item : null);
        }

        handleRightClick(e);

        spawnParticles(p);
        playSound(p);
    }

    public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
        handleAttack(e);
    }

    public static UsableItem getItemByName(String name) {
        return usableItems.get(name);
    }
}
