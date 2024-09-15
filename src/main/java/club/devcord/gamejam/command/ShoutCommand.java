package club.devcord.gamejam.command;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.command.core.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

public class ShoutCommand extends BaseCommand {
    public ShoutCommand(BuggyBedwarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register(CommandManager<CommandSender> manager) {
        manager.command(manager.commandBuilder("shout", "all", "a")
                .senderType(Player.class)
                .required("message", greedyStringParser())
                .handler(this::handle)
        );
    }

    private void handle(@NonNull CommandContext<Player> commandSenderCommandContext) {
        var message = (String) commandSenderCommandContext.get("message");
        commandSenderCommandContext.sender().chat("@all " + message);
    }
}
