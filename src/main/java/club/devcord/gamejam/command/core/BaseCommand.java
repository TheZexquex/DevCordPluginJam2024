package club.devcord.gamejam.command.core;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.CommandManager;

public abstract class BaseCommand {
    protected final BuggyBedwarsPlugin plugin;

    public BaseCommand(BuggyBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void register(CommandManager<CommandSender> manager);
}
