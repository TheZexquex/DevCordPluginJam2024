package club.devcord.gamejam.logic.shop.gui;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class ShopBackItem extends PageItem {


    public ShopBackItem() {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        var builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setDisplayName("Vorherige Seite")
                .addLoreLines(gui.hasPreviousPage()
                        ? "Gehe zu Seite " + gui.getCurrentPage() + "/" + gui.getPageAmount()
                        : "Keine vorherige Seite");

        return builder;
    }
}
