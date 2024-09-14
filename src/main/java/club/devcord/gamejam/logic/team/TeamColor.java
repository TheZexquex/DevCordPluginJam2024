package club.devcord.gamejam.logic.team;

import net.kyori.adventure.text.format.NamedTextColor;

public enum TeamColor {
    RED(NamedTextColor.RED, "Rot"),
    GREEN(NamedTextColor.GREEN, "Grün"),
    BLUE(NamedTextColor.BLUE, "Blau"),
    YELLOW(NamedTextColor.YELLOW, "Gelb");

    private final NamedTextColor textColor;
    private final String displayName;

    TeamColor(NamedTextColor textColor, String displayName) {
        this.textColor = textColor;
        this.displayName = displayName;
    }

    public NamedTextColor textColor() {
        return textColor;
    }

    public String displayName() {
        return displayName;
    }
}
