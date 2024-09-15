package club.devcord.gamejam.stage.ingame.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.message.Messenger;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {
    private final Game game;
    private final Messenger messenger;

    public AsyncChatListener(Game game, Messenger messenger) {
        this.game = game;
        this.messenger = messenger;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        var player = event.getPlayer();
        var teamOptional = game.getTeam(player);

        if (game.gameStage() != GameStage.IN_GAME) {
            event.renderer((source, sourceDisplayName, message, viewer) -> {
                var prefix = "<gray>";
                if (teamOptional.isPresent()) {
                    prefix = teamOptional.get().getShortPrefix() + teamOptional.get().mmColor();
                }

                return MiniMessage.miniMessage().deserialize(prefix + source.getName() + " <dark_gray>» ").append(message.color(NamedTextColor.GRAY));
            });
            return;
        }

        event.setCancelled(true);

        var prefix = "<dark_gray>[<gray>Beobachter<dark_gray>] <gray>";
        if (teamOptional.isPresent()) {
            var team = teamOptional.get();
            prefix = team.mmColor();
        }

        var message = MiniMessage.miniMessage().serialize(event.message());
        var formattedMessage = prefix + player.getName() + " <dark_gray>» <gray>" + message.replaceAll("^@a(ll)?\\s*", "");

        if (message.startsWith("@a") || teamOptional.isEmpty()) {
            messenger.broadcast("<gray>[<yellow>ALL<gray>] " + formattedMessage);
        } else {
            var team = teamOptional.get();
            for (var teamPlayer : team.teamPlayers()) {
                teamPlayer.sendRichMessage("<gray>[" + team.mmColor() + "TEAM<gray>] " + formattedMessage);
            }
        }
    }
}
