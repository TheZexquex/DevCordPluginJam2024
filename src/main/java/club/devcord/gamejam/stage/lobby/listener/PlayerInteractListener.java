package club.devcord.gamejam.stage.lobby.listener;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.team.gui.TeamSelectGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final CursedBedwarsPlugin plugin;

    public PlayerInteractListener(CursedBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        var item = event.getItem();
        var player = event.getPlayer();

        if (plugin.game().gameStage() != GameStage.IN_GAME) {
            event.setCancelled(true);
        }

        if (item == null) {
            return;
        }

        event.setCancelled(true);

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (item.getPersistentDataContainer().has(TeamSelectGUI.OPEN_ITEM_KEY)) {
            new TeamSelectGUI().open(player, plugin.game());
        }
    }
}
