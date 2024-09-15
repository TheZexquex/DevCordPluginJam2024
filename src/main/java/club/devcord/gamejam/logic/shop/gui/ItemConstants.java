package club.devcord.gamejam.logic.shop.gui;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;


public class ItemConstants {
    public static final NamespacedKey GAME_ITEM_TYPE = new NamespacedKey("buggy_bedwars", "game_item_type");

    public enum Type {
        KNOCK_BACK_STICK, WOOL
    }

    public static ItemStack knockBackStick() {
        var itemStack = new ItemStack(Material.BREEZE_ROD);
        var itemMeta = itemStack.getItemMeta();

        itemMeta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        itemMeta.getPersistentDataContainer().set(GAME_ITEM_TYPE, PersistentDataType.STRING, Type.KNOCK_BACK_STICK.name());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack wool(NamedTextColor color) {
        var material = Material.valueOf(color.toString().toUpperCase() + "_WOOL");
        var itemStack = new ItemStack(material);
        var itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(GAME_ITEM_TYPE, PersistentDataType.STRING, Type.WOOL.name());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
