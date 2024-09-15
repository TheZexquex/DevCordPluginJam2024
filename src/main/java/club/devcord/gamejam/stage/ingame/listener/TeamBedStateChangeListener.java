package club.devcord.gamejam.stage.ingame.listener;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.event.TeamBedDestroyEvent;
import club.devcord.gamejam.event.TeamBedRebuildEvent;
import club.devcord.gamejam.message.Messenger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamBedStateChangeListener implements Listener {
    private BuggyBedwarsPlugin plugin;

    public TeamBedStateChangeListener(BuggyBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeamBedDestroy(TeamBedDestroyEvent event) {
        var team = event.team();

        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendRichMessage(Messenger.PREFIX + "<red>Das Bett von Team " + team.getFormattedName() + " <red>wurde zerstört");
            player.playSound(Sound.sound(Key.key("entity.ender_dragon.growl"), Sound.Source.MASTER, 1.0F, 1.0F));
        });
    }

    @EventHandler
    public void onTeamBedRebuild(TeamBedRebuildEvent event) {
        var team = event.team();

        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendRichMessage(Messenger.PREFIX + "<green>Das Bett von Team " + team.getFormattedName() + " <green>wurde wieder aufgebaut");
            player.playSound(Sound.sound(Key.key("entity.breeze.idle_air"), Sound.Source.MASTER, 1.0F, 1.0F));
        });
    }
}
