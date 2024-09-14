package club.devcord.gamejam.stage.lobby.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.team.gui.TeamSelectGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final Game game;

    public PlayerInteractListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        var item = event.getItem();
        var player = event.getPlayer();

        if (game.gameStage() != GameStage.IN_GAME) {
            event.setCancelled(true);
        }

        if (item == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (item.getPersistentDataContainer().has(TeamSelectGUI.OPEN_ITEM_KEY)) {
            event.setCancelled(true);
            new TeamSelectGUI().open(player, game);
        }
    }
}
