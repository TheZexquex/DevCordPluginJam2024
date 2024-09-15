package club.devcord.gamejam.logic;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashSet;
import java.util.Set;

public class BlockRegistry implements Listener {
    private final Set<Location> placedBlocks = new HashSet<>();

    public void add(Location location) {
        placedBlocks.add(location);
    }

    public boolean isPlayerPlaced(Block block) {
        return placedBlocks.contains(block.getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        placedBlocks.add(event.getBlock().getLocation());
    }
}
