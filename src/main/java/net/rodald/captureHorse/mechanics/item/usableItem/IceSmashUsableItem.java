package net.rodald.captureHorse.mechanics.item.usableItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class IceSmashUsableItem extends UsableItem {
    // ability that converts all placed ice into strength
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
    public int getCooldown() {
        return 0;
    }

    @Override
    protected void prepareItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }

    @Override
    public int getCustomModelData() {
        return 255;
    }

    private static final double TIME_MIN = 50;
    private static final double TIME_MAX = 60;
    private static final double BRIDGE_LENGTH = 100;
    private static final double CURVE_STRENGTH = 2;
    private static final int BLOCKS_PER_SECOND = 10;
    private static final int BRIDGE_WIDTH = 1;
    private static final int BLOCK_PLACE_RAN_CHANCE = 4;
    private static final int GROUND_POUND_RANGE = 40;
    private int blockShield;
    private double rotater;
    private HashMap<FallingBlock, Double> blockPosition = new HashMap<>();
    private HashMap<FallingBlock, Double> blockLifeTime = new HashMap<>();
    private HashMap<FallingBlock, Double> projectileBlock = new HashMap<>();

    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        Bukkit.broadcastMessage("block has been right clicked.");
        // cancels the event so players cant put the item into an armor stand or something like that
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (event.getBlockFace() != BlockFace.UP) return false;
            // ground pound
            double groundPoundStrength = player.getVelocity().getY();

            // only activate ground pound if groundPoundStrength is < -1
            if (!(groundPoundStrength < -1)) return false;

            player.setFallDistance(0);
            player.setVelocity(new Vector(0, 0, 0));

            Location playerLocation = player.getLocation();

            double damage = player.getAttribute(Attribute.ATTACK_DAMAGE).getValue();
            int brokenIce;
            List<Entity> nearbyEntities = player.getNearbyEntities(GROUND_POUND_RANGE, GROUND_POUND_RANGE, GROUND_POUND_RANGE);

            brokenIce = breakIce(playerLocation, (int) Math.abs(groundPoundStrength) * 50);
            for (Entity entity : nearbyEntities) {
                Location entityLocation = entity.getLocation();

                double distance = playerLocation.distance(entityLocation);

                double knockbackStrength = calculateKnockbackMultiplier(groundPoundStrength, damage, distance);

                Bukkit.broadcastMessage("distance: " + entity.getName() + " = " + distance);
                // calculate knockback direction
                Vector direction = entityLocation.toVector().subtract(playerLocation.toVector())
                        .normalize().multiply(knockbackStrength);
                direction.setY(knockbackStrength);

                // update entity knockback to calculated one
                entity.setVelocity(direction);
                entity.setFreezeTicks(brokenIce * 700);
            }
        } else if (event.getAction().isRightClick()) {
            // ice brige
            buildIceBridge(player.getLocation().add(0, -1, 0));

            player.setCooldown(this.createItem(), getCooldown());
            return true;
        }

        return false;
    }

    private int breakIce(Location location, int distance) {
        int iceBreak = 0;
        if (distance == 0) {
            return iceBreak;
        }
        Bukkit.broadcastMessage("Recursion at " + location.toString() + " distance: " + distance);

        for (int x = -distance; x < distance; x++) {
            for (int y = -3; y < distance; y++) {
                for (int z = -distance; z < distance; z++) {
                    if (location.clone().add(x, y, z).getBlock().getType() == Material.FROSTED_ICE) {
                        location.clone().add(x, y, z).getBlock().setType(Material.AIR);
                        iceBreak = breakIce(
                                new Location(location.getWorld(),
                                        location.getX() + x, location.getY() + y, location.getZ() + z),
                                distance / 3) + 1;
                    }
                }
            }
        }

        return iceBreak;
    }

    @Override
    public void handleBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
        Bukkit.broadcastMessage("cancelled event!");

        Block block = event.getBlock();
        World world = block.getWorld();
        Location location = block.getLocation();

        block.setType(Material.AIR);
        Bukkit.broadcastMessage("has broken correct block");
        blockShield += 1;
        FallingBlock iceBlock = (FallingBlock) world.spawnEntity(location.clone().add(.5f, 0, .5f), EntityType.FALLING_BLOCK);
        iceBlock.setBlockData(Material.FROSTED_ICE.createBlockData());
        iceBlock.setGravity(false);
        Random random = new Random();
        blockLifeTime.put(iceBlock, random.nextDouble(TIME_MIN, TIME_MAX));
        blockPosition.put(iceBlock, 1d);
        recalcBlocks();
    }

    private double calculateKnockbackMultiplier(double velocity, double damage, double distance) {
        if ((damage * (-24 * (velocity * damage) - (8 * distance) - 160)) / (10 * (distance + 20)) <= 0) {
            return 0;
        } else {
            return (damage * (-24 * (velocity * damage) - (8 * distance) - 160)) / (10 * (distance + 20));
        }
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
    public void handleAttack(EntityDamageByEntityEvent event) {
        Bukkit.broadcastMessage("block has been hit.");
        Player damager = (Player) event.getDamager();
        Entity target = event.getEntity();

        if (event.getEntity() instanceof Player targetPlayer) {
            if (Teams.getEntityTeam(damager) != null && Teams.getEntityTeam(targetPlayer) != null) {
                if (Teams.getEntityTeam(damager) == Teams.getEntityTeam(targetPlayer)) {
                    return;
                }
            }
        } else if (event.getEntityType() == EntityType.FALLING_BLOCK && ((FallingBlock) target).getBlockData().getMaterial() == Material.FROSTED_ICE && checkFallingBlock((FallingBlock)target,blockLifeTime)) {
            Bukkit.broadcastMessage("block has been hit.");
            blockDestroyer((FallingBlock)target);
            Vector fallingB = new Vector(damager.getLocation().getX()-target.getLocation().getX(),damager.getLocation().getY()-target.getLocation().getY(),damager.getLocation().getZ()-target.getLocation().getZ());
            ((FallingBlock)target).setVelocity(fallingB.multiply(damager.getVelocity().multiply(0.3)).multiply(damager.getAttribute(Attribute.ATTACK_DAMAGE).getValue()*0.2));
            projectileBlock.put((FallingBlock)target,damager.getAttribute(Attribute.ATTACK_DAMAGE).getValue()*1.2);
        }

        World world = target.getWorld();
        world.playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
        Location targetLocation = target.getLocation();

        spawnAndPlaceIceBlocks(targetLocation);
    }

    @Override
    public void handleTick(Player player) {
        final int ROTATION_DISTANCE = 3;
        rotater += 0.01;
        if (rotater >= 1) {
            rotater = -1;
        }

        for (Map.Entry<FallingBlock, Double> fallingBlock : blockLifeTime.entrySet()) {

            // skip loop if falling block got removed
            if (fallingBlock.getKey().isDead()) {
                blockDestroyer(fallingBlock.getKey());
                break;
            }

            double currentValue = blockLifeTime.get(fallingBlock.getKey());
            blockLifeTime.put(fallingBlock.getKey(), currentValue - 0.05);
            if (blockLifeTime.get(fallingBlock.getKey()) <= 0) {
                blockDestroyer(fallingBlock.getKey());
                Bukkit.broadcastMessage("block has timed out.");
            }
            Vector offsetFromUnitCircle = new Vector(
                    Math.cos(blockPosition.get(fallingBlock.getKey()) + (2*Math.PI*rotater)),
                    0.33,
                    Math.sin(blockPosition.get(fallingBlock.getKey()) + (2*Math.PI*rotater)));

            offsetFromUnitCircle.multiply(ROTATION_DISTANCE);
            Location unitCircleLocation = player.getLocation().add(offsetFromUnitCircle);

            Vector nextBlockPosition = unitCircleLocation.subtract(fallingBlock.getKey().getLocation()).toVector();

            fallingBlock.getKey().setVelocity(nextBlockPosition.multiply(0.3));
        }

        for (FallingBlock fallingBlock : player.getWorld().getEntitiesByClass(FallingBlock.class)) {
            if (fallingBlock.getBlockData().getMaterial() != Material.FROSTED_ICE) {
                continue;
            }

            for (Entity nearbyEntity : fallingBlock.getNearbyEntities(2, 2, 2)) {
                if (nearbyEntity instanceof LivingEntity livingEntity) {
                    if (nearbyEntity instanceof Player nearbyPlayer) {
                        if (Teams.getEntityTeam(player) != null && Teams.getEntityTeam(nearbyPlayer) != null) {
                            if (Objects.equals(Teams.getEntityTeam(player), Teams.getEntityTeam(nearbyPlayer))) {
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

    public void onDisable(Player player) {
        Bukkit.broadcast(Component.text("onDisable! Removing " + blockLifeTime.size() + " entries"
                , NamedTextColor.RED));
        for (Map.Entry<FallingBlock, Double> fallingBlock : blockLifeTime.entrySet()) {
            blockDestroyer(fallingBlock.getKey());
        }
    }
    public boolean checkFallingBlock(FallingBlock block, HashMap<FallingBlock, Double> hasher) {
        for (Map.Entry<FallingBlock, Double> fallingBlock : hasher.entrySet()) {
            if (block == (FallingBlock) fallingBlock) {
                return true;
            }
        }
        return false;
    }
    private void blockDestroyer(FallingBlock block) {
        block.setGravity(true);
        Bukkit.broadcastMessage("blocked destroyed");
        blockLifeTime.remove(block);
        blockPosition.remove(block);
        block.remove();
        blockShield--;
        recalcBlocks();
    }

    private void recalcBlocks() {
        int cntr = 0;
        Bukkit.broadcastMessage("racalculated");
        for (Map.Entry<FallingBlock, Double> fallingBlock : blockLifeTime.entrySet()) {
            cntr++;
            blockPosition.put(fallingBlock.getKey(), ((2 * Math.PI) * ((double) cntr / blockShield)));
        }
    }

    private void spawnAndPlaceIceBlocks(Location location) {
        World world = location.getWorld();
        int iceBlockCount = 6;
        double baseVelocityMultiplier = 0.5;

        for (int i = 0; i < iceBlockCount; i++) {
            double angle = 2 * Math.PI * i / iceBlockCount;

            double randomAngleOffset = (Math.random() * Math.PI / 3) - Math.PI / 6;
            angle += randomAngleOffset;

            double velocityMultiplier = baseVelocityMultiplier + (Math.random() * 0.3 - 0.15);

            Vector velocity = new Vector(Math.cos(angle), 0.5 + Math.random() * 0.2, Math.sin(angle))
                    .multiply(velocityMultiplier);

            FallingBlock iceBlock = (FallingBlock) world.spawnEntity(location.clone().add(.5f, 0, .5f), EntityType.FALLING_BLOCK);
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
    public void handleFallingBlockLand(EntityChangeBlockEvent event) {
        Bukkit.broadcastMessage("triggered");
        if (checkFallingBlock((FallingBlock) event,projectileBlock)) {
            if (Math.round(Math.random()*BLOCK_PLACE_RAN_CHANCE)!=BLOCK_PLACE_RAN_CHANCE) {
                event.setCancelled(true);
                projectileBlock.remove((FallingBlock) event);
                ((FallingBlock) event).remove();
            }else{
                projectileBlock.remove((FallingBlock) event);
            }
        }
    }
}
