package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.BlockRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final BlockRegistry blockRegistry;

    public BlockBreakListener(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!blockRegistry.isPlayerPlaced(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}
