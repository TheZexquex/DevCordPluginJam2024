package club.devcord.gamejam.stage.ingame.listener;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var block = event.getBlock();
        if (block.getType() == Material.TNT) {
            block.setType(Material.AIR);
            block.getWorld().spawnEntity(block.getLocation(), EntityType.TNT);
        }
    }
}
