package net.rodald.captureHorse.interfaces;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface KatanaAbility {
    String getAbilityName();
    long getCooldown();
    void prepareItem(ItemStack item);
    boolean handleRightClick(PlayerInteractEvent event);
    void handleAttack(EntityDamageByEntityEvent event);
    void handleTick(Player player);
}
