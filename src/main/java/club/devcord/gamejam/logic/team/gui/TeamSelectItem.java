package club.devcord.gamejam.logic.team.gui;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.team.TeamColor;
import club.devcord.gamejam.message.Messenger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class TeamSelectItem extends SimpleItem {
    private final TeamColor teamColor;
    private final Game game;

    public TeamSelectItem(@NotNull ItemProvider itemProvider, TeamColor teamColor, Game game) {
        super(itemProvider);
        this.teamColor = teamColor;
        this.game = game;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        super.handleClick(clickType, player, event);
        game.switchPlayerToTeam(player, game.getTeamFromColor(teamColor));
        player.sendRichMessage(Messenger.PREFIX + "<gray>Du bist jetzt in Team <" + teamColor.textColor() + ">"  + teamColor.displayName());
        player.closeInventory();
        player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0F, 1.0F));
    }
}
