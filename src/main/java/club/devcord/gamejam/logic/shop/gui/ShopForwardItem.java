package club.devcord.gamejam.logic.shop.gui;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class ShopForwardItem extends PageItem {

    public ShopForwardItem() {
        super(true);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        var builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
        builder.setDisplayName("Nächste Seite")
                .addLoreLines(gui.hasNextPage()
                        ? "Gehe zu Seite " + (gui.getCurrentPage() + 2) + "/" + gui.getPageAmount()
                        : "Keine weiteren Seiten");

        return builder;
    }
}
