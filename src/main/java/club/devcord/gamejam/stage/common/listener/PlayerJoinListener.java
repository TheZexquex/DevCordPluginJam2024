package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.CursedBedwarsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final CursedBedwarsPlugin plugin;

    public PlayerJoinListener(CursedBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        switch (plugin.game().gameStage()) {
            case LOBBY -> {
                plugin.game().switchPlayerToTeam(PL);
            }
            default -> plugin.game()
        }
    }
}
