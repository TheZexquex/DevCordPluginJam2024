package club.devcord.gamejam.logic.shop;

import club.devcord.gamejam.logic.team.TeamColor;
import club.devcord.gamejam.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorSet {
    private final String name;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    private ArmorSet(String name) {
        this.name = name;
    }

    public static ArmorSet builder(String name) {
        return new ArmorSet(name);
    }

    public ArmorSet helmet(Material material) {
        this.helmet = new ItemStack(material);
        return this;
    }

    public ArmorSet chestplate(Material material) {
        this.chestplate = new ItemStack(material);
        return this;
    }

    public ArmorSet leggings(Material material) {
        this.leggings = new ItemStack(material);
        return this;
    }

    public ArmorSet boots(Material material) {
        this.boots = new ItemStack(material);
        return this;
    }

    public ArmorSet color(TeamColor color) {
        if (helmet != null && helmet.getType() == Material.LEATHER_HELMET) {
            ItemUtils.colorizeLeatherArmor(helmet, color.textColor());
        }

        if (chestplate != null && chestplate.getType() == Material.LEATHER_CHESTPLATE) {
            ItemUtils.colorizeLeatherArmor(chestplate, color.textColor());
        }

        if (leggings != null && leggings.getType() == Material.LEATHER_LEGGINGS) {
            ItemUtils.colorizeLeatherArmor(leggings, color.textColor());
        }

        if (boots != null && boots.getType() == Material.LEATHER_BOOTS) {
            ItemUtils.colorizeLeatherArmor(boots, color.textColor());
        }

        return this;
    }

    public String name() {
        return name;
    }

    public void give(Player player) {
        var inventory = player.getInventory();

        if (helmet != null) inventory.setHelmet(helmet);
        if (chestplate != null) inventory.setChestplate(chestplate);
        if (leggings != null) inventory.setLeggings(leggings);
        if (boots != null) inventory.setBoots(boots);
    }
}
