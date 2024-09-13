package club.devcord.gamejam.logic.team.gui;

import club.devcord.gamejam.logic.Game;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class TeamSelectGUI {
    public static final NamespacedKey OPEN_ITEM_KEY = new NamespacedKey("cursed_bedwars", "team_select_open_item");

    public void open(Player player, Game game) {
        var structure = new Structure(
                "x x x x x x x x x",
                "x x R G x B Y x x",
                "x x x x x x x x x"
        )
                .addIngredient('x', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)))
                .addIngredient('R', new TeamSelectItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE), NamedTextColor.RED, game))
                .addIngredient('G', new TeamSelectItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE), NamedTextColor.GREEN, game))
                .addIngredient('B', new TeamSelectItem(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE), NamedTextColor.BLUE, game))
                .addIngredient('Y', new TeamSelectItem(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE), NamedTextColor.YELLOW, game));


        var gui = Gui.normal().setStructure(structure);

        Window.single().setGui(gui).open(player);
    }
}
