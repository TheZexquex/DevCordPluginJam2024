package club.devcord.gamejam.stage.common.listener;

import com.destroystokyo.paper.MaterialSetTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (MaterialSetTag.BEDS.isTagged(event.getItemDrop().getItemStack().getType())) {
            event.setCancelled(true);
        }
    }
}