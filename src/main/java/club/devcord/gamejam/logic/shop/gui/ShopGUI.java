package club.devcord.gamejam.logic.shop.gui;

import club.devcord.gamejam.logic.shop.ArmorSet;
import club.devcord.gamejam.logic.team.TeamColor;
import club.devcord.gamejam.utils.KeyValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.TabGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class ShopGUI {
    public void open(Player player, TeamColor teamColor) {
        var border = new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""));

        var blockGui = getBlockGui(teamColor);
        var equipmentGui = getEquipmentGui(teamColor);
        var toolGui = getToolGui();
        var specialGui = getSpecialGui();

        var gui = TabGui.normal()
                .setStructure(
                        "# 0 # 1 # 2 # 3 #",
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# # # # # # # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL) // where paged items should be put
                .addIngredient('#', border)
                .addIngredient('0', new ShopTabItem(0, new ItemBuilder(ItemConstants.wool(teamColor.textColor())).setDisplayName("Blöcke")))
                .addIngredient('1', new ShopTabItem(1, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("Waffen & Rüstung")))
                .addIngredient('2', new ShopTabItem(2, new ItemBuilder(Material.SHEARS).setDisplayName("Werkzeuge")))
                .addIngredient('3', new ShopTabItem(3, new ItemBuilder(Material.NETHER_STAR).setDisplayName("Spezial")))
                .setTabs(List.of(blockGui, equipmentGui, toolGui, specialGui))
                .build();

        Window.single()
                .setTitle(new AdventureComponentWrapper(Component.text("Shop").color(TextColor.fromHexString("#e042f5"))))
                .setGui(gui)
                .open(player);
    }

    private Gui getBlockGui(TeamColor teamColor) {
        var gui = Gui.empty(9, 2);

        gui.addItems(
                new ShopStandardItem(
                        new ItemBuilder(ItemConstants.wool(teamColor.textColor())).setAmount(16),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 5)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.WEATHERED_CUT_COPPER_STAIRS),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 64)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.SAND).setAmount(6),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 64)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.CHERRY_PLANKS).setAmount(2),
                        KeyValue.of(new ItemStack(Material.GOLD_INGOT), 1)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.DEEPSLATE_IRON_ORE).setAmount(3),
                        KeyValue.of(new ItemStack(Material.GOLD_INGOT), 6)
                )
        );

        return gui;
    }

    private Gui getEquipmentGui(TeamColor teamColor) {
        var gui = Gui.empty(9, 2);

        gui.addItems(
                new ShopStandardItem(
                        new ItemBuilder(ItemConstants.knockBackStick()),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 10)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.WOODEN_SWORD),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 20)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.STONE_SWORD),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 5)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.IRON_SWORD),
                        KeyValue.of(new ItemStack(Material.DIAMOND), 4)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.CROSSBOW),
                        KeyValue.of(new ItemStack(Material.DIAMOND), 5)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.SPECTRAL_ARROW).setAmount(2),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 1)
                ),
                new ShopArmorItem(
                        new ItemBuilder(Material.LEATHER_BOOTS),
                        ArmorSet.builder("Lederrüstung")
                                .helmet(Material.LEATHER_HELMET)
                                .chestplate(Material.LEATHER_CHESTPLATE)
                                .leggings(Material.LEATHER_LEGGINGS)
                                .boots(Material.LEATHER_BOOTS)
                                .color(teamColor),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 32)
                ),
                new ShopArmorItem(
                        new ItemBuilder(Material.GOLDEN_BOOTS),
                        ArmorSet.builder("Goldrüstung")
                                .helmet(Material.LEATHER_HELMET)
                                .chestplate(Material.GOLDEN_CHESTPLATE)
                                .leggings(Material.LEATHER_LEGGINGS)
                                .boots(Material.GOLDEN_BOOTS)
                                .color(teamColor),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 4)
                ),
                new ShopArmorItem(
                        new ItemBuilder(Material.IRON_BOOTS),
                        ArmorSet.builder("Eisenrüstung")
                                .helmet(Material.LEATHER_HELMET)
                                .chestplate(Material.IRON_CHESTPLATE)
                                .leggings(Material.LEATHER_LEGGINGS)
                                .boots(Material.IRON_BOOTS)
                                .color(teamColor),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 15)
                ),
                new ShopArmorItem(
                        new ItemBuilder(Material.DIAMOND_BOOTS),
                        ArmorSet.builder("Diamantrüstung")
                                .helmet(Material.LEATHER_HELMET)
                                .chestplate(Material.DIAMOND_CHESTPLATE)
                                .leggings(Material.LEATHER_LEGGINGS)
                                .boots(Material.DIAMOND_BOOTS)
                                .color(teamColor),
                        KeyValue.of(new ItemStack(Material.DIAMOND), 6)
                )
        );

        return gui;
    }

    private Gui getToolGui() {
        var gui = Gui.empty(9, 2);

        gui.addItems(
                new ShopStandardItem(
                        new ItemBuilder(Material.SHEARS),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 64)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.WOODEN_PICKAXE),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 32)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.STONE_PICKAXE),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 10)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.IRON_PICKAXE),
                        KeyValue.of(new ItemStack(Material.DIAMOND), 1)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.WOODEN_AXE),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 32)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.STONE_AXE),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 10)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.IRON_AXE),
                        KeyValue.of(new ItemStack(Material.DIAMOND), 1)
                )
        );

        return gui;
    }

    private Gui getSpecialGui() {
        var gui = Gui.empty(9, 2);

        gui.addItems(
                new ShopStandardItem(
                        new ItemBuilder(Material.ENDER_PEARL),
                        KeyValue.of(new ItemStack(Material.DIAMOND), 10)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.TNT),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 5)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.EGG).setDisplayName("Brückenei"),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 10)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.SNOWBALL).setDisplayName("Explosiver Schneeball"),
                        KeyValue.of(new ItemStack(Material.RAW_IRON), 128)
                ),
                new ShopStandardItem(
                        new ItemBuilder(Material.PUFFERFISH_BUCKET).setAmount(2),
                        KeyValue.of(new ItemStack(Material.RAW_GOLD), 4)
                )
        );

        return gui;
    }
}
