package club.devcord.gamejam.stage.lobby.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.team.gui.TeamSelectGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryInteractListener implements Listener {

    private final Game game;

    public InventoryInteractListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (game.gameStage() != GameStage.IN_GAME) {
            event.setCancelled(true);
        }

        var item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        if (item.getPersistentDataContainer().has(TeamSelectGUI.OPEN_ITEM_KEY)) {
            event.setCancelled(true);
        }
    }
}
