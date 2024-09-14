package club.devcord.gamejam.logic.shop.gui;

import club.devcord.gamejam.logic.shop.ArmorSet;
import club.devcord.gamejam.utils.KeyValue;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;

public class ShopArmorItem extends ShopPurchasableItem {
    private final ArmorSet armorSet;

    public ShopArmorItem(@NotNull ItemProvider displayItem, ArmorSet armorSet, KeyValue<ItemStack, Integer> price) {
        super(displayItem, armorSet.name(), price);
        this.armorSet = armorSet;
    }

    @Override
    public void giveItem(Player player) {
        armorSet.give(player);
    }
}
