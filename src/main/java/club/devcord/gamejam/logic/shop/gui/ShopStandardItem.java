package club.devcord.gamejam.logic.shop.gui;

import club.devcord.gamejam.utils.KeyValue;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;

public class ShopStandardItem extends ShopPurchasableItem {
    public ShopStandardItem(@NotNull ItemProvider itemProvider, KeyValue<ItemStack, Integer> price) {
        super(itemProvider, price);
    }

    @Override
    public void giveItem(Player player) {
        player.getInventory().addItem(getItemProvider().get());
    }
}
