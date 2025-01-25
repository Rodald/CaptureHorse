package net.rodald.captureHorse.interfaces;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface KatanaAbility {
    String getAbilityName();

    ItemStack prepareItem(ItemStack item);
    void handleRightClick(PlayerInteractEvent event);
    void handleAttack(EntityDamageByEntityEvent event);
    void handleTick(Player player);
}
