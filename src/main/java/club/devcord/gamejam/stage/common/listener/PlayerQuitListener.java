package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.message.Messenger;
import com.mojang.brigadier.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final Game game;
    private final Messenger messenger;

    public PlayerQuitListener(Game game, Messenger messenger) {
        this.game = game;
        this.messenger = messenger;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var teamColorOptional = game.getTeamColor(player);

        game.clearTeam(player);

        var teamColor = NamedTextColor.DARK_AQUA;
        if (teamColorOptional.isPresent()) {
            teamColor = teamColorOptional.get();
        }

        event.quitMessage(Component.empty());
        messenger.broadCast(Messenger.PREFIX + "<" + teamColor.asHexString() + ">" + player.getName() + " <gray>hat das Spiel verlassen");
    }
}
