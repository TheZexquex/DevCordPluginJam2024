package club.devcord.gamejam.logic.shop.gui;

import club.devcord.gamejam.logic.team.Team;
import club.devcord.gamejam.utils.KeyValue;
import club.devcord.gamejam.utils.MiniComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class ShopGUI {
    public void open(Player player, NamedTextColor teamColor) {
        var border = new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""));

        if (teamColor == NamedTextColor.GRAY) {
            return;
        }

        List<Item> items = List.of(
                new ShopBuyItem(
                        new ItemBuilder(ItemConstants.wool(teamColor)),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 5)
                )
        );

        var gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # # # # # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL) // where paged items should be put
                .addIngredient('#', border)
                //.addIngredient('<', new ShopBackItem())
                //.addIngredient('>', new ShopForwardItem())
                .setContent(items)
                .build();

        Window.single()
                .setTitle(new AdventureComponentWrapper(Component.text("Shop").color(TextColor.fromHexString("#e042f5"))))
                .setGui(gui)
                .open(player);
    }
}
