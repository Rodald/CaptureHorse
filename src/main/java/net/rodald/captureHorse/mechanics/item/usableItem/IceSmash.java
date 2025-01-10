package net.rodald.captureHorse.mechanics.item.usableItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.rodald.captureHorse.CaptureHorse;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.scoreboard.Teams;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
    public String getItemName() {
        return ChatColor.GREEN + "Ice Hammer";
    }

    @Override
    public ArrayList<Component> getItemLore() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Drücke Rechtsklick, um das Spiel Menü aufzurufen.", NamedTextColor.GRAY));
        return lore;
    }

    @Override
    protected void prepareItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }
    private static final double BRIDGE_LENGTH = 100;  // Länge der Brücke
    private static final double CURVE_STRENGTH = 2; // Stärke der Kurve
    private static final int BLOCKS_PER_SECOND = 10; // Anzahl der Blöcke, die pro Sekunde gesetzt werden
    private static final int BRIDGE_WIDTH = 1; // Anzahl der Blöcke, die pro Sekunde gesetzt werden

    @Override
    public void handleRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        buildIceBridge(player.getLocation().add(0, -1, 0));  // Startpunkt der Brücke, leicht erhöht
    }

    // Methode zum Erstellen der Eisbrücke
    private void buildIceBridge(Location startLocation) {
        Vector direction = startLocation.getDirection().normalize();
        Vector startDirection = direction.clone();
        startDirection.multiply(3).setY(0);

        Location currentLocation = startLocation.add(startDirection);

        // Animationsschritt: Erstelle die Brücke schrittweise
        new BukkitRunnable() {
            int currentBlock = 0;

            @Override
            public void run() {
                // Berechne die Position des nächsten Blocks und eine leichte y-Kurve
                double offsetY = Math.sin(currentBlock * Math.PI / BRIDGE_LENGTH) * CURVE_STRENGTH; // Leichte Kurve

                // Berechne die Position des Blocks an der aktuellen Stelle
                Location blockLocation = currentLocation.clone().add(direction.clone().multiply(currentBlock));
                blockLocation.setY(blockLocation.getY() + offsetY);  // Kurve einbauen

                // Setze den Block (Frozen Ice)
                for (int x = -BRIDGE_WIDTH; x <= BRIDGE_WIDTH; x++) {
                    for (int z = -BRIDGE_WIDTH; z <= BRIDGE_WIDTH; z++) {
                        Block block = blockLocation.clone().add(x, 0, z).getBlock();
                        if (block.getBlockData().getMaterial() != Material.AIR && block.getBlockData().getMaterial() != Material.FROSTED_ICE) {
                            continue;
                        }
                        // Partikel zur Animation
                        block.setType(Material.FROSTED_ICE);
                        block.getWorld().spawnParticle(Particle.SNOWFLAKE, block.getLocation(), 5, 1, 1, 1, 0);

                    }
                }

                // Wenn die Brücke fertig ist, stoppen wir die Animation
                if (++currentBlock >= BRIDGE_LENGTH) {
                    cancel();
                }

            }
        }.runTaskTimer(CaptureHorse.getInstance(), 0L, 20L / BLOCKS_PER_SECOND);  // Zeit zwischen den Blocksetzungen
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent e) {
        Player damager = (Player) e.getDamager();
        if (!(e.getEntity() instanceof Player target)) return;

        if (Teams.getEntityTeam(damager) != null && Teams.getEntityTeam(target) != null) {
            if (Teams.getEntityTeam(damager) == Teams.getEntityTeam(target)) {
                return;
            }
        }

        World world = target.getWorld();
        Location targetLocation = target.getLocation();

        spawnAndPlaceIceBlocks(world, targetLocation);

        damager.sendMessage("Du hast " + target.getName() + " geschlagen und Eisblöcke erzeugt!");
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
