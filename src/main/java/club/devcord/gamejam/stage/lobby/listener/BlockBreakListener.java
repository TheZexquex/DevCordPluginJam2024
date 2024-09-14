package club.devcord.gamejam.stage.lobby.listener;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.GameStage;
import com.destroystokyo.paper.MaterialSetTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final CursedBedwarsPlugin plugin;

    public BlockBreakListener(CursedBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var type = event.getBlock().getType();

        if (plugin.game().gameStage() == GameStage.IN_GAME) {
            if (MaterialSetTag.WOOL.isTagged(type) || MaterialSetTag.PLANKS.isTagged(type)) {
                return;
            }
        }

        event.setCancelled(true);
    }
}
