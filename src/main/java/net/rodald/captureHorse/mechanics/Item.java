package net.rodald.captureHorse.mechanics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class Item {
    public abstract Material getMaterial();

    public abstract String getItemName();

    public abstract ArrayList<Component> getItemLore();

    public abstract int getCustomModelData();

    /**
     * Optional method to prepare item-specific customizations.
     * Can be overridden by subclasses to apply additional meta-settings.
     *
     * @param item The item stack to prepare.
     */
    protected void prepareItem(ItemStack item) {}

    public void giveItem(Player player, int slot) {
        ItemStack item = createItem();
        player.getInventory().setItem(slot, item);
    }

    public void giveItem(Player player) {
        ItemStack item = createItem();
        player.getInventory().addItem(item);
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(this.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + getItemName());
            List<Component> modifiedLore = getItemLore().stream()
                    .map(component -> component.decoration(TextDecoration.ITALIC, false)) // Italics entfernen
                    .toList();

            meta.lore(modifiedLore);
            meta.setCustomModelData(getCustomModelData());
            item.setItemMeta(meta);
        }

        prepareItem(item);

        return item;
    }
}
