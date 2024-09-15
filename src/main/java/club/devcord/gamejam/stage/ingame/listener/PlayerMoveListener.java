package club.devcord.gamejam.stage.ingame.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.message.Messenger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final Game game;
    private final Messenger messenger;

    public PlayerMoveListener(Game game, Messenger messenger) {
        this.game = game;
        this.messenger = messenger;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var teamOptional = game.getTeam(player);

        if (teamOptional.isEmpty()) {
            return;
        }

        if (player.getY() < GameSettings.MIN_Y_LEVEL) {
            player.setFallDistance(0f);

            var killerOptional = game.damagerRegistry().getDamager(player);
            if (killerOptional.isPresent()) {
                var killer = killerOptional.get();
                var killerTeamOptional = game.getTeam(killer);

                killer.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1.0F, 1.0F));

                if (killerTeamOptional.isPresent()) {
                    messenger.broadcast(messenger.getDeathMessage(player, teamOptional.get(), killer, killerTeamOptional.get()));
                } else {
                    messenger.broadcast(messenger.getDeathMessage(player, teamOptional.get(), killer, "<gray>"));
                }

                // TODO: Add kill sounds
            } else {
                messenger.broadcast(messenger.getDeathMessage(player, teamOptional.get()));
            }

            game.handleKill(event.getPlayer());
        }
    }
}
