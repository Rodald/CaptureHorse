package net.rodald.captureHorse.mechanics.item.usableItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class IceSmashUsableItem extends UsableItem {
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
        lore.add(Component.text("Ice Hammer Description.", NamedTextColor.GRAY));
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
        return 255;
    }
    private static final double BRIDGE_LENGTH = 100;
    private static final double CURVE_STRENGTH = 2;
    private static final int BLOCKS_PER_SECOND = 10;
    private static final int BRIDGE_WIDTH = 1;

    @Override
    public boolean handleRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        buildIceBridge(player.getLocation().add(0, -1, 0));
        player.setCooldown(this.createItem(), getCooldown());
        return true;
    }

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

    @Override
    public void handleTick(Player player) {
        for (FallingBlock fallingBlock : player.getWorld().getEntitiesByClass(FallingBlock.class)) {
            if (fallingBlock.getBlockData().getMaterial() != Material.FROSTED_ICE) {
                continue;
            }

            for (Entity nearbyEntity : fallingBlock.getNearbyEntities(2, 2, 2)) {
                if (nearbyEntity instanceof LivingEntity livingEntity) {
                    if (nearbyEntity instanceof Player nearbyPlayer) {
                        if (Teams.getEntityTeam(player) != null && Teams.getEntityTeam(nearbyPlayer) != null) {
                            if (Teams.getEntityTeam(player).equals(Teams.getEntityTeam(nearbyPlayer))) {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }

                    if (!(livingEntity.getFreezeTicks() > 0)) {
                        fallingBlock.remove();

                        livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                        livingEntity.getWorld().spawnParticle(Particle.SNOWFLAKE, livingEntity.getLocation(), 20, 1, 2, 1, 0);
                        livingEntity.setFreezeTicks(400);
                    }
                    livingEntity.setFreezeTicks(400);

                    break;
                }
            }
        }
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
        new BukkitRunnable() {
            final double baseRadius = 0.25;
            int step = 0;
            final int maxSteps = 5;
            final int particlesPerRing = 64;
            final double playerX = p.getLocation().getX();
            final double playerZ = p.getLocation().getZ();
            final double playerY = p.getLocation().getY();

            @Override
            public void run() {
                if (step >= maxSteps) {
                    this.cancel();
                    return;
                }

                double currentRadius = baseRadius + step * 0.2;
                double yOffset = step * 0.075;

                for (int i = 0; i < particlesPerRing; i++) {
                    double angle = 2 * Math.PI * i / particlesPerRing;
                    double noiseX = (Math.random() - 0.5) * 0.5;
                    double noiseZ = (Math.random() - 0.5) * 0.5;
                    double noiseY = (Math.random() - 0.3) * 0.25;

                    double x = playerX + Math.cos(angle) * currentRadius + noiseX;
                    double z = playerZ + Math.sin(angle) * currentRadius + noiseZ;
                    double y = playerY + yOffset + noiseY;

                    if (Math.random() < 0.3) {
                        p.getWorld().spawnParticle(Particle.SNOWFLAKE, x, y, z, 1, 0, 0, 0, 0);
                    } else {
                        Color startColor = Color.fromRGB(150 + (int) (Math.random() * 50), 200 + (int) (Math.random() * 30), 255);
                        Color endColor = Color.fromRGB(80 + (int) (Math.random() * 50), 130 + (int) (Math.random() * 30), 255);
                        float size = 1.0f + (float) Math.random();

                        p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, x, y, z, 1, new Particle.DustTransition(startColor, endColor, size));
                    }
                }

                step++;
            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0, 1);
    }

    @Override
    public void playSound(Player p) {

    }
}
