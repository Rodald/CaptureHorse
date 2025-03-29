package net.rodald.captureHorse.mechanics.item.usableItem.katanaStates;

import net.rodald.captureHorse.interfaces.KatanaAbility;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Slot3Ability implements KatanaAbility {
    @Override
    public String getAbilityName() {
        return "Heavy Strike";
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
        if (event.getDamager() instanceof Player) {
            Entity target = event.getEntity();

            event.setDamage(event.getDamage() + 4.0);

            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
            target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 10, 0.3, 0.5, 0.3, 0.1);
        }
    }

    @Override
    public void handleTick(Player player) {
        if (player.getFallDistance() > 0) {
            player.setFallDistance(Math.max(player.getFallDistance() - 4.0f, 0));
        }

        if (player.isSneaking()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 3));

            int currentDuration = player.getPotionEffect(PotionEffectType.STRENGTH) == null ? 0 : player.getPotionEffect(PotionEffectType.STRENGTH).getDuration();

            if (currentDuration % 20 == 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, currentDuration + 40, 0));
            }
        }
    }

}
