package club.devcord.gamejam.logic.shop.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.TabGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.TabItem;

public class ShopTabItem extends TabItem {
    private final int tab;
    private final ItemProvider itemProvider;

    public ShopTabItem(int tab, ItemProvider itemProvider) {
        super(tab);
        this.tab = tab;
        this.itemProvider = itemProvider;
    }

    @Override
    public ItemProvider getItemProvider(TabGui gui) {
        var item = itemProvider.get();

        if (gui.getCurrentTab() == tab) {
            var itemMeta = item.getItemMeta();
            itemMeta.setEnchantmentGlintOverride(true);
            item.setItemMeta(itemMeta);
        }

        return new ItemBuilder(item);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        super.handleClick(clickType, player, event);
        player.playSound(Sound.sound(Key.key("block.bamboo_wood_button.click_off"), Sound.Source.MASTER, 1.0F, 1.0F));
    }
}
