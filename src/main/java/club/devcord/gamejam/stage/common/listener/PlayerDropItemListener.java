package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.team.gui.TeamSelectGUI;
import com.destroystokyo.paper.MaterialSetTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        var item = event.getItemDrop();

        if (MaterialSetTag.BEDS.isTagged(event.getItemDrop().getItemStack().getType())) {
            if (item.getPersistentDataContainer().has(TeamSelectGUI.OPEN_ITEM_KEY)) {
                event.setCancelled(true);
            }
        }
    }
}
