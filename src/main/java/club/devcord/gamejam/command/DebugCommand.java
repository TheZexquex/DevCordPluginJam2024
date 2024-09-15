package club.devcord.gamejam.command;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.logic.team.TeamColor;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.command.core.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import java.util.Locale;

import static org.incendo.cloud.parser.standard.BooleanParser.booleanParser;
import static org.incendo.cloud.parser.standard.EnumParser.enumParser;

public class DebugCommand extends BaseCommand {
    public DebugCommand(BuggyBedwarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register(CommandManager<CommandSender> manager) {
        manager.command(manager.commandBuilder("debug")
                .permission("buggybedwars.command.debug")
                .senderType(Player.class)
                .literal("world")
                .handler(this::handleDebugWorld)
        );

        manager.command(manager.commandBuilder("debug")
                .permission("buggybedwars.command.debug")
                .senderType(Player.class)
                .literal("setteamalive")
                .required("team", enumParser(TeamColor.class))
                .required("state", booleanParser())
                .handler(this::handleDebugAlive)
        );

        manager.command(manager.commandBuilder("debug")
                .permission("buggybedwars.command.debug")
                .senderType(Player.class)
                .literal("location")
                .handler(this::handleDebugLocation)
        );

        manager.command(manager.commandBuilder("debug")
                .permission("buggybedwars.command.debug")
                .senderType(Player.class)
                .literal("teamtp")
                .required("team", enumParser(TeamColor.class))
                .required("type", enumParser(TpType.class))
                .handler(this::handleDebugTeamTp)
        );
    }

    private enum TpType {
        BED, SPAWN
    }

    private void handleDebugTeamTp(@NonNull CommandContext<Player> playerCommandContext) {
        var sender = playerCommandContext.sender();
        var team = plugin.game().getTeamFromColor(playerCommandContext.get("team"));
        var type = (TpType) playerCommandContext.get("type");

        if (type == TpType.BED) {
            sender.teleport(team.bedLocation().toBukkitLocation(plugin.game().gameMap().bukkitWorld()));
            sender.sendRichMessage(Messenger.PREFIX + "<gray>Teleportiere zur Bed Location von Team " + team.getFormattedName());
        }
        if (type == TpType.SPAWN) {
            sender.teleport(team.spawnLocation().toBukkitLocation(plugin.game().gameMap().bukkitWorld()));
            sender.sendRichMessage(Messenger.PREFIX + "<gray>Teleportiere zur Spawn Location von Team " + team.getFormattedName());
        }
    }

    private void handleDebugLocation(@NonNull CommandContext<Player> playerCommandContext) {
        var sender = playerCommandContext.sender();
        var location = sender.getLocation();
        var locString = String.format(Locale.US, "%.1f, %.1f, %.1f, %.1f, %.1f", location.x(), location.y(), location.z(), location.getYaw(), location.getPitch());
        sender.sendRichMessage(Messenger.PREFIX + "<gray>Location: <yellow><click:copy_to_clipboard:'RelativeLocation.of(" + locString + ")'>" + locString + " <gray>(COPY)</click>");
    }

    private void handleDebugWorld(@NonNull CommandContext<Player> playerCommandContext) {
        var sender = playerCommandContext.sender();
        sender.sendRichMessage(Messenger.PREFIX + "<gray>Welt:  <yellow>" + sender.getWorld().getName());
    }

    private void handleDebugAlive(@NonNull CommandContext<Player> playerCommandContext) {
        var sender = playerCommandContext.sender();
        var team = plugin.game().getTeamFromColor(playerCommandContext.get("team"));
        var state = (boolean) playerCommandContext.get("state");

        team.setAlive(state);
        sender.sendRichMessage(
                Messenger.PREFIX
                        + "<gray>Alive von <" +  team.teamColor().toString() + ">"
                        + team.teamColor().displayName() + " <gray> auf <yellow>" + state
                        + " <gray>gesetzt!");
    }
}
