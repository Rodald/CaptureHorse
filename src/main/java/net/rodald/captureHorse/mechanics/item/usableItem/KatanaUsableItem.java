package net.rodald.captureHorse.mechanics.item.usableItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.rodald.captureHorse.interfaces.KatanaAbility;
import net.rodald.captureHorse.mechanics.item.UsableItem;
import net.rodald.captureHorse.mechanics.item.usableItem.katanaStates.Slot0Ability;
import net.rodald.captureHorse.mechanics.item.usableItem.katanaStates.Slot1Ability;
import net.rodald.captureHorse.mechanics.item.usableItem.katanaStates.Slot2Ability;
import net.rodald.captureHorse.mechanics.item.usableItem.katanaStates.Slot3Ability;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    private final Map<KatanaAbility, Long> cooldowns = new HashMap<>();

    public KatanaUsableItem() {
        abilities.put(0, new Slot0Ability());
        abilities.put(1, new Slot1Ability());
        abilities.put(2, new Slot2Ability());
        abilities.put(3, new Slot3Ability());
        abilities.put(4, new Slot3Ability());
        abilities.put(5, new Slot3Ability());
        abilities.put(6, new Slot3Ability());
        abilities.put(7, new Slot3Ability());
        abilities.put(8, new Slot3Ability());
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
    public boolean handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        KatanaAbility ability = abilities.getOrDefault(selectedSlot, null);

        if (ability != null) {
            if (getRemainingTimeInTicks() > 0) {
                double remainingTime = getRemainingTimeInTicks();
                player.sendMessage(Component.text(String.format("Your ability is on cooldown: %.2f seconds.", remainingTime), NamedTextColor.RED));
                player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
            } else {
                setCooldown();
                player.setCooldown(this.getMaterial(), (int) getRemainingTimeInTicks());
                return ability.handleRightClick(event);
            }
        } else {
            player.sendMessage("No ability assigned for this slot.");
        }

        return false;
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
        int lastSelectedSlot = selectedSlot;
        selectedSlot = inventory.getHeldItemSlot();
        if (lastSelectedSlot != selectedSlot) {
            player.setCooldown(this.getMaterial(), (int) getRemainingTimeInTicks());
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

    }

    @Override
    public void playSound(Player player) {

    }

    private void setCooldown() {
        cooldowns.put(abilities.getOrDefault(selectedSlot, null), System.currentTimeMillis());
    }

    public long getRemainingTimeInTicks() {
        KatanaAbility ability = abilities.getOrDefault(selectedSlot, null);
        if (ability == null || !cooldowns.containsKey(ability)) {
            return 0;
        }

        long elapsedTime = System.currentTimeMillis() - cooldowns.get(ability);
        long remainingTimeMillis = (ability.getCooldown() * 50L) - elapsedTime;

        if (remainingTimeMillis <= 0) {
            return 0;
        }

        return remainingTimeMillis / 50;
    }
}
