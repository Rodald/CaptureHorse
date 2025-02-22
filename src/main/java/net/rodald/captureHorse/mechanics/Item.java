package net.rodald.captureHorse.mechanics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class Item {
    public abstract Material getMaterial();

    public abstract Component getItemName();

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
            meta.displayName(getItemName());
            List<Component> modifiedLore = getItemLore().stream()
                    .map(component -> component.decoration(TextDecoration.ITALIC, false))
                    .toList();

            meta.lore(modifiedLore);
            meta.setCustomModelData(getCustomModelData());
            item.setItemMeta(meta);
        }

        prepareItem(item);

        return item;
    }

    protected Component applyGradient(String text, TextColor startColor, TextColor endColor) {
        Component gradientBuilder = Component.empty();
        int textLength = text.length();

        for (int i = 0; i < textLength; i++) {
            double ratio = (double) i / (textLength - 1);
            TextColor currentColor = interpolateColor(startColor, endColor, ratio);
            gradientBuilder = gradientBuilder.append(Component.text(String.valueOf(text.charAt(i)), currentColor));
        }

        return gradientBuilder.decoration(TextDecoration.ITALIC, false);
    }


    private TextColor interpolateColor(TextColor start, TextColor end, double ratio) {
        int startRed = start.red();
        int startGreen = start.green();
        int startBlue = start.blue();

        int endRed = end.red();
        int endGreen = end.green();
        int endBlue = end.blue();

        int red = (int) (startRed + (endRed - startRed) * ratio);
        int green = (int) (startGreen + (endGreen - startGreen) * ratio);
        int blue = (int) (startBlue + (endBlue - startBlue) * ratio);

        return TextColor.color(red, green, blue);
    }
}
