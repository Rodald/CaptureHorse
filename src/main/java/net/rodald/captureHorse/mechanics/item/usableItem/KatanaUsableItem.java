package net.rodald.captureHorse.mechanics.item.usableItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.rodald.captureHorse.interfaces.KatanaAbility;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.mechanics.item.usableItem.katanaStates.Slot0Ability;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KatanaUsableItem extends UsableItem {
    private int selectedSlot;
    private final Map<Integer, KatanaAbility> abilities = new HashMap<>();

    public KatanaUsableItem() {
        // Hier die Fähigkeiten für jeden Slot registrieren
        abilities.put(0, new Slot0Ability());
//        abilities.put(1, new Slot1Ability());
//        abilities.put(2, new Slot2Ability());
        // Füge weitere Slots hinzu
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public Component getItemName() {
        return applyGradient("Katana", TextColor.color(0xDAE8E7), TextColor.color(0xAAAAB5));
    }

    @Override
    public ArrayList<Component> getItemLore() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("A legendary katana with multiple abilities.", NamedTextColor.GRAY));
        return lore;
    }

    @Override
    public int getCooldown() {
        return 600;
    }

    @Override
    protected void prepareItem(ItemStack item) {
        KatanaAbility ability = abilities.getOrDefault(selectedSlot, null);
        ability.prepareItem(item);
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
    }

    @Override
    public int getCustomModelData() {
        return 256;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        KatanaAbility ability = abilities.getOrDefault(selectedSlot, null);

        if (ability != null) {
            ability.handleRightClick(event);
        } else {
            player.sendMessage("No ability assigned for this slot.");
        }
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        KatanaAbility ability = abilities.getOrDefault(selectedSlot, null);

        if (ability != null) {
            ability.handleAttack(event);
        } else {
            player.sendMessage("No ability assigned for this slot.");
        }
    }

    @Override
    public void handleTick(Player player) {
        PlayerInventory inventory = player.getInventory();

        if (inventory.getItemInMainHand().isSimilar(this.createItem())) {
            selectedSlot = inventory.getHeldItemSlot();
        }

        KatanaAbility ability = abilities.getOrDefault(selectedSlot, null);

        if (ability != null) {
            ability.handleTick(player);

            ItemStack item = inventory.getItemInMainHand();

            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.displayName(Component.text("Katana (" + ability.getAbilityName() + ")")
                        .color(TextColor.color(0xDAE8E7)));
                item.setItemMeta(meta);
            }
        }
    }

    @Override
    public void spawnParticles(Player player) {
        // Partikel-Logik hier einfügen
    }

    @Override
    public void playSound(Player player) {
        // Sound-Logik hier einfügen
    }
}
