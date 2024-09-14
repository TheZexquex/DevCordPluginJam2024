package club.devcord.gamejam.utils;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemUtils {
    public static void colorizeLeatherArmor(ItemStack itemStack, NamedTextColor color) {
        var itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.setColor(Color.fromRGB(color.value()));
        itemStack.setItemMeta(itemMeta);
    }
}
