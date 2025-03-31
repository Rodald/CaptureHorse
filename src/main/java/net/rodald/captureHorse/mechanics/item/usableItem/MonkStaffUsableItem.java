package net.rodald.captureHorse.mechanics.item.usableItem;

import com.google.common.collect.Sets;
import net.kyori.adventure.text.Component;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class MonkStaffUsableItem extends UsableItem {

    // chargeable hop distance
    private double hopAmount;
    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {

        return false;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {

    }

    @Override
    public void handleTick(Player player) {
        if (player.isSneaking()) {
            onSneak(player);
        } else {
            if (hopAmount != 0) {
                player.setVelocity(player.getLocation().getDirection().normalize());
            }
        }

        double jumpVelocity = player.getAttribute(Attribute.JUMP_STRENGTH).getBaseValue();
        if (player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
            jumpVelocity += ((float) (player.getPotionEffect(PotionEffectType.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
        }
        
        if (player.getLocation().getBlock().getType() != Material.LADDER) {
            if (!player.isOnGround() && player.getVelocity().getY() == jumpVelocity && player.getLastDamage() > 0.05) {//hurrrrr
                player.sendMessage("Jumping!");
            }
        }
    }

    private void onSneak(Player player) {
        if (player.isOnGround()) {
            hopAmount += 0.05;

        }
    }

    @Override
    public void spawnParticles(Player player) {

    }

    @Override
    public void playSound(Player player) {

    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public Component getItemName() {
        return null;
    }

    @Override
    public ArrayList<Component> getItemLore() {
        return null;
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }
}
