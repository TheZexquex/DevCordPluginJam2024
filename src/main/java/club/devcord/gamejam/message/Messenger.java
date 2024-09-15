package club.devcord.gamejam.message;

import club.devcord.gamejam.logic.team.Team;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Messenger {
    private final MiniMessage minimessage = MiniMessage.miniMessage();
    private final Server server;

    public Messenger(Server server) {
        this.server = server;
    }

    public void broadcast(String miniMessage) {
        server.getOnlinePlayers().forEach(player -> player.sendMessage(minimessage.deserialize(miniMessage)));
    }

    public String getDeathMessage(Player player, Team team) {
        return Messenger.PREFIX + "<white>☠ " + team.mmColor() + player.getName();
    }

    public String getDeathMessage(Player player, Team team, Player damager, Team damagerTeam) {
        return getDeathMessage(player, team, damager, damagerTeam.mmColor());
    }

    public String getDeathMessage(Player player, Team team, Player damager, String prefix) {
        return getDeathMessage(player, team) + " <gray>durch " + prefix + damager.getName();
    }

    public static final String PREFIX = "<white>[<#eb8034>BuggyBedwars<white>] ";
}
