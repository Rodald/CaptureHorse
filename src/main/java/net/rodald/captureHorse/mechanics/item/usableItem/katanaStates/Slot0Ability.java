package net.rodald.captureHorse.mechanics.item.usableItem.katanaStates;

import net.rodald.captureHorse.interfaces.KatanaAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Slot0Ability implements KatanaAbility {

    @Override
    public String getAbilityName() {
        return "Basic Slash";
    }

    @Override
    public long getCooldown() {
        return 20;
    }

    @Override
    public void prepareItem(ItemStack item) {

    }





    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("Slot 0: Basic Sword Slash activated!");
        return false;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {
        event.getDamager().sendMessage("Slot 0: Attack logic executed!");
    }

    @Override
    public void handleTick(Player player) {

    }
}
