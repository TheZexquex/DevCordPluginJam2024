package club.devcord.gamejam.stage.ingame.listener;

import club.devcord.gamejam.logic.BlockRegistry;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class ExplodeListener implements Listener {
    private final BlockRegistry blockRegistry;

    public ExplodeListener(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        filterExplosionBlocks(event.blockList());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        filterExplosionBlocks(event.blockList());
    }

    private void filterExplosionBlocks(List<Block> blocks) {
        blocks.removeIf(block -> !blockRegistry.isPlayerPlaced(block));
    }
}
