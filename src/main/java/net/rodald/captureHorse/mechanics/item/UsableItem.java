package net.rodald.captureHorse.mechanics.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.rodald.captureHorse.mechanics.Item;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class UsableItem extends Item {
    // Eine Map, die alle registrierten Item enthält
    private static final Map<Component, UsableItem> usableItems = new HashMap<>();
    private static final Map<Player, Long> cooldowns = new HashMap<>();

    public UsableItem() {
        usableItems.put(getItemName(), this);
    }
    public boolean clearItemOnUse() { return false; };
    public abstract void handleRightClick(PlayerInteractEvent e);
    public abstract void handleAttack(EntityDamageByEntityEvent e);
    public abstract void handleTick(Player player);

    public abstract void spawnParticles(Player p);
    public abstract void playSound(Player p);
    public int getCooldown() { return 0; };
    
    // Methode zum Behandeln der Item-Action
    public void handleItemAction(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (isOnCooldown(p)) {
            double remainingTime = getRemainingTime(p);
            p.sendMessage(Component.text(String.format("Your ability is on cooldown: %.2f seconds.", remainingTime), NamedTextColor.RED));
            p.playSound(p, Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
        } else {
            ItemStack item = e.getItem();
            if (clearItemOnUse()) {
                int amount = item.getAmount() - 1;
                item.setAmount(amount);
                p.getInventory().setItemInMainHand(amount > 0 ? item : null);
            }

            handleRightClick(e);

            if (p.getGameMode() != GameMode.CREATIVE) {
                setCooldown(p);
            }

            spawnParticles(p);
            playSound(p);
        }
    }

    private boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player)) {
            return false;
        }
        long lastUse = cooldowns.get(player);
        return (System.currentTimeMillis() - lastUse) < (getCooldown() * 50L);
    }

    private void setCooldown(Player player) {
        player.setCooldown(this.createItem(), getCooldown());
        cooldowns.put(player, System.currentTimeMillis());
    }

    public double getRemainingTime(Player p) {
        if (cooldowns.get(p) == null) {
            return 0;
        }
        return (getCooldown() * 50 - (System.currentTimeMillis() - cooldowns.get(p))) / 1000.0;
    }

    public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
        handleAttack(e);
    }
    public void handleItemTick(Player player) {
        handleTick(player);
    }

    public static UsableItem getItemByName(Component name) {
        return usableItems.get(name);
    }
}
