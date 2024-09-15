package club.devcord.gamejam.command;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.command.core.BaseCommand;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.message.Messenger;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

public class ForceStartCommand extends BaseCommand {
    public ForceStartCommand(BuggyBedwarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register(CommandManager<CommandSender> manager) {
        manager.command(manager.commandBuilder("forcestart")
                .permission("buggybedwars.command.forcestart")
                .handler(this::handle)
        );
    }

    private void handle(@NonNull CommandContext<CommandSender> commandSenderCommandContext) {
        var sender = commandSenderCommandContext.sender();
        if (plugin.game().lobbyCountdown().isRunning()) {
            sender.sendRichMessage(Messenger.PREFIX + "<red>Das Spiel startet bereits!");
            return;
        }
        sender.sendRichMessage(Messenger.PREFIX + "<gray>Das spiel wurde geforcestarted!");
        plugin.game().startGameCountDown(GameSettings.LOBBY_COUNTDOWN_FORCE_START_SECONDS);
    }
}
