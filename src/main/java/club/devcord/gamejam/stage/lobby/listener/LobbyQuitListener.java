package club.devcord.gamejam.stage.lobby.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.message.Messenger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyQuitListener implements Listener {
    private final Game game;
    private final Messenger messenger;

    public LobbyQuitListener(Game game, Messenger messenger) {
        this.game = game;
        this.messenger = messenger;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if (game.gameStage() != GameStage.LOBBY) {
            return;
        }

        if (Bukkit.getOnlinePlayers().size() != GameSettings.MIN_PLAYERS) {
            return;
        }

        game.lobbyCountdown().abort();
        game.sendPreCountdownActionbar();

        messenger.broadcast(Messenger.PREFIX + "<red>Der Countdown wurde abgebrochen, weil zu wenige Spieler auf dem Server sind!");
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(Sound.sound(Key.key("block.anvil.land"), Sound.Source.MASTER, 1.0F, 1.0F));
        });
    }
}
