package net.rodald.captureHorse.mechanics.item.usableItem.katanaStates;

import net.rodald.captureHorse.interfaces.KatanaAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Slot2Ability implements KatanaAbility {
    @Override
    public String getAbilityName() {
        return "Critical Strike";
    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public void prepareItem(ItemStack item) {

    }

    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        return false;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {

            if (isCritical(player) && !player.isOnGround()) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10.0f, 1.5f);
                event.setDamage(2*event.getDamage());
                Entity target = event.getEntity();
                double width = target.getWidth();
                double height = target.getHeight();
                target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, height/2, 0), (int) (20*width*height), width/3.75, height/3.75, width/3.75, 0.1);
            }
        }
    }

    @Override
    public void handleTick(Player player) {

    }

    public boolean isCritical(Player player) {
        return (player.getVelocity().getY() + 0.0784000015258789) <= 0;
    }
}
