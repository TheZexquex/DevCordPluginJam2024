package club.devcord.gamejam;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;

public class Messenger {
    private final MiniMessage minimessage = MiniMessage.miniMessage();
    private final Server server;

    public Messenger(Server server) {
        this.server = server;
    }

    public void broadCast(String miniMessage) {
        server.getOnlinePlayers().forEach(player -> player.sendMessage(minimessage.deserialize(miniMessage)));
    }

    public static String PREFIX = "<white>[<#eb8034>CursedBedwars<white>] ";
}
