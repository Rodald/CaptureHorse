package net.rodald.captureHorse.mechanics.item.usableItem.katanaStates;

import net.rodald.captureHorse.interfaces.KatanaAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Slot0Ability implements KatanaAbility {

    @Override
    public String getAbilityName() {
        return "Basic Slash";
    }

    @Override
    public long getCooldown() {
        return 20;
    }

    @Override
    public void prepareItem(ItemStack item) {
//        ItemMeta meta = item.getItemMeta();
//
//        if (meta != null) {
//            // UUIDs für die Attribute (für Eindeutigkeit)
//            UUID attackDamageUUID = UUID.randomUUID();
//            UUID attackSpeedUUID = UUID.randomUUID();
//
//            AttributeModifier attackDamageModifier = new AttributeModifier(
//                    attackDamageUUID,
//                    "generic.attackDamage", // Eindeutiger Name
//                    8.0, // Angriffsschaden-Wert
//                    AttributeModifier.Operation.ADD_NUMBER, // Hinzufügen
//                    EquipmentSlot.HAND // Gültig in der Haupthand
//            );
//            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, attackDamageModifier);
//
//            // Angriffsgeschwindigkeit-Modifier hinzufügen
//            AttributeModifier attackSpeedModifier = new AttributeModifier(
//                    attackSpeedUUID,
//                    "generic.attackSpeed", // Eindeutiger Name
//                    1.6, // Angriffsgeschwindigkeit-Wert
//                    AttributeModifier.Operation.ADD_NUMBER, // Hinzufügen
//                    EquipmentSlot.HAND // Gültig in der Haupthand
//            );
//            meta.addAttributeModifier(Attribute.ATTACK_SPEED, attackSpeedModifier);
//
//            // Aktualisiertes Meta setzen
//            item.setItemMeta(meta);
//        }
//
//        return item;
    }





    @Override
    public boolean handleRightClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("Slot 0: Basic Sword Slash activated!");
        return false;
    }

    @Override
    public void handleAttack(EntityDamageByEntityEvent event) {
        event.getDamager().sendMessage("Slot 0: Attack logic executed!");
    }

    @Override
    public void handleTick(Player player) {

    }
}
