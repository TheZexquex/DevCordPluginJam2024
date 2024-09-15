package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.message.Messenger;
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
        var teamOptional = game.getTeam(player);

        game.clearTeam(player);
        game.sideBarScoreboard().hide(player);

        if (game.gameStage() == GameStage.IN_GAME) {
            game.handlePotentialWin();
        }

        var teamColor = NamedTextColor.DARK_AQUA;
        if (teamOptional.isPresent()) {
            teamColor = teamOptional.get().teamColor().textColor();
        }

        event.quitMessage(Component.empty());
        messenger.broadcast(Messenger.PREFIX + "<" + teamColor.asHexString() + ">" + player.getName() + " <gray>hat das Spiel verlassen");
    }
}
