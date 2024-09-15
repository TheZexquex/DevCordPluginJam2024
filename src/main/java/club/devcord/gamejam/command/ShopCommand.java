package club.devcord.gamejam.command;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.command.core.BaseCommand;
import club.devcord.gamejam.logic.shop.gui.ShopGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import static org.incendo.cloud.bukkit.parser.PlayerParser.playerParser;

public class ShopCommand extends BaseCommand {
    public ShopCommand(BuggyBedwarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register(CommandManager<CommandSender> manager) {
        manager.command(manager.commandBuilder("shop")
                .senderType(ConsoleCommandSender.class)
                .permission("buggybedwars.command.shop")
                .required("player", playerParser())
                .handler(this::handle)
        );
    }

    private void handle(@NonNull CommandContext<ConsoleCommandSender> consoleCommandSenderCommandContext) {
        var player = (Player) consoleCommandSenderCommandContext.get("player");

        plugin.game().getTeam(player).ifPresent(team -> {
            new ShopGUI().open(player, team.teamColor());
        });
    }
}
