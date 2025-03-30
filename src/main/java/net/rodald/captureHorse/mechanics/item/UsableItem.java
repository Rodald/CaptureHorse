package net.rodald.captureHorse.mechanics.item;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.rodald.captureHorse.mechanics.Item;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class UsableItem extends Item {
    private static final Map<Integer, UsableItem> usableItems = new HashMap<>();
    private static final Map<Player, Long> cooldowns = new HashMap<>();

    public UsableItem() {
        usableItems.put(getCustomModelData(), this);
    }
    public boolean clearItemOnUse() { return false; };
    public abstract boolean handleRightClick(PlayerInteractEvent event);

    public abstract void handleAttack(EntityDamageByEntityEvent event);
    public abstract void handleTick(Player player);
    public abstract void spawnParticles(Player player);
    public abstract void playSound(Player player);
    public int getCooldown() { return 0; };
    
    public void handleItemAction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isOnCooldown(player)) {
            double remainingTime = getRemainingTime(player);
            player.sendMessage(Component.text(String.format("Your ability is on cooldown: %.2f seconds.", remainingTime), NamedTextColor.BLACK));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
        } else {
            ItemStack item = event.getItem();
            if (clearItemOnUse()) {
                int amount = item.getAmount() - 1;
                item.setAmount(amount);
                player.getInventory().setItemInMainHand(amount > 0 ? item : null);
            }

            if (player.getGameMode() != GameMode.CREATIVE && event.getAction().isRightClick() && handleRightClick(event)) {
                setCooldown(player);
            }

            spawnParticles(player);
            playSound(player);
        }
    }

    public void handleJumpEvent(PlayerJumpEvent event) {

    }

    public void handleBlockBreak(BlockBreakEvent event) {

    }

    /**
     *  Gets called when the player switches from an UsableItem to another item
     *
     * @param player PlayerInventorySlotChangeEvent
     */
    public void onDisable(Player player) {

    }

    private boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player)) {
            return false;
        }
        long lastUse = cooldowns.get(player);
        return (System.currentTimeMillis() - lastUse) < (getCooldown() * 50L);
    }

    private void setCooldown(Player player) {
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

    public static UsableItem getItemByCustomModelData(int customModelData) {
        return usableItems.get(customModelData);
    }
}
