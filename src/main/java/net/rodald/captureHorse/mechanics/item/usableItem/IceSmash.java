package net.rodald.captureHorse.mechanics.item.usableItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class IceSmash extends UsableItem {
    @Override
    public Material getMaterial() {
        return Material.DIAMOND_AXE;
    }
    @Override
    public Component getItemName() {
        return applyGradient("Ice Hammer", TextColor.color(0x00FFFF), TextColor.color(0xA3FF));
    }

    @Override
    public ArrayList<Component> getItemLore() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Drücke Rechtsklick, um das Spiel Menü aufzurufen.", NamedTextColor.GRAY));
        return lore;
    }

    @Override
    public int getCooldown() { return 600; };


    @Override
    protected void prepareItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }
    private static final double BRIDGE_LENGTH = 100;
    private static final double CURVE_STRENGTH = 2;
    private static final int BLOCKS_PER_SECOND = 10;
    private static final int BRIDGE_WIDTH = 1;

    @Override
    public void handleRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        buildIceBridge(player.getLocation().add(0, -1, 0));
    }

    // Methode zum Erstellen der Eisbrücke
    private void buildIceBridge(Location startLocation) {
        Vector direction = startLocation.getDirection().normalize();
        Vector startDirection = direction.clone();
        startDirection.multiply(3).setY(0);

        Location currentLocation = startLocation.add(startDirection);

        new BukkitRunnable() {
            int currentBlock = 0;

            @Override
            public void run() {
                double offsetY = Math.sin(currentBlock * Math.PI / BRIDGE_LENGTH) * CURVE_STRENGTH;

                Location blockLocation = currentLocation.clone().add(direction.clone().multiply(currentBlock));
                blockLocation.setY(blockLocation.getY() + offsetY);

                for (int x = -BRIDGE_WIDTH; x <= BRIDGE_WIDTH; x++) {
                    for (int z = -BRIDGE_WIDTH; z <= BRIDGE_WIDTH; z++) {
                        Block block = blockLocation.clone().add(x, 0, z).getBlock();
                        if (block.getBlockData().getMaterial() != Material.AIR && block.getBlockData().getMaterial() != Material.FROSTED_ICE) {
                            if (x == 0 && z == 0 && currentBlock >= 5) {
                                this.cancel();
                                return;
                            } else {
                                continue;
                            }
                        }
                        block.setType(Material.FROSTED_ICE);
                        block.getWorld().spawnParticle(Particle.SNOWFLAKE, block.getLocation(), 5, 1, 1, 1, 0);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_SNOW_PLACE, 1, 1);
                    }
                }

                if (++currentBlock >= BRIDGE_LENGTH) {
                    cancel();
                }

            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 20L / BLOCKS_PER_SECOND);
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent e) {
        Player damager = (Player) e.getDamager();
        Entity target = e.getEntity();
        if (e.getEntity() instanceof Player targetPlayer) {
            if (Teams.getEntityTeam(damager) != null && Teams.getEntityTeam(targetPlayer) != null) {
                if (Teams.getEntityTeam(damager) == Teams.getEntityTeam(targetPlayer)) {
                    return;
                }
            }
        }

        World world = target.getWorld();
        world.playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
        Location targetLocation = target.getLocation();

        spawnAndPlaceIceBlocks(world, targetLocation);
    }

    private void spawnAndPlaceIceBlocks(World world, Location location) {
        int iceBlockCount = 6;
        double baseVelocityMultiplier = 0.5;

        for (int i = 0; i < iceBlockCount; i++) {
            double angle = 2 * Math.PI * i / iceBlockCount;

            double randomAngleOffset = (Math.random() * Math.PI / 3) - Math.PI / 6;
            angle += randomAngleOffset;

            double velocityMultiplier = baseVelocityMultiplier + (Math.random() * 0.3 - 0.15);

            Vector velocity = new Vector(Math.cos(angle), 0.5 + Math.random() * 0.2, Math.sin(angle))
                    .multiply(velocityMultiplier);

            FallingBlock iceBlock = (FallingBlock) world.spawnEntity(location, EntityType.FALLING_BLOCK);
            iceBlock.setBlockData(Material.FROSTED_ICE.createBlockData());
            iceBlock.setVelocity(velocity);
            iceBlock.setDropItem(false);
        }
    }







    @Override
    public void spawnParticles(Player p) {

    }

    @Override
    public void playSound(Player p) {

    }
}
