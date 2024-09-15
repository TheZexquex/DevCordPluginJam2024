package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.BlockRegistry;
import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.message.Messenger;
import com.destroystokyo.paper.MaterialSetTag;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final BlockRegistry blockRegistry;
    private final Game game;
    private final Messenger messenger;

    public BlockBreakListener(BlockRegistry blockRegistry, Game game, Messenger messenger) {
        this.blockRegistry = blockRegistry;
        this.game = game;
        this.messenger = messenger;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        var block = event.getBlock();

        if (MaterialSetTag.BEDS.isTagged(block.getType())) {
            var bed = (Bed) block.getBlockData();

            var loc1 = block.getLocation();
            var loc2 = getTwinLocation(bed, block);

            game.getTeam(player).ifPresent(team -> {
                if (team.bedLocation().equalsBukkitLoc(loc1) || team.bedLocation().equalsBukkitLoc(loc2)) {
                    team.setAlive(false);
                } else {
                    event.setCancelled(true);
                    player.sendRichMessage(Messenger.PREFIX + "<red>Du kannst dein eigenes Bett nicht abbauen!");
                    player.playSound(Sound.sound(Key.key("entity.horse.angry"), Sound.Source.MASTER, 1.0F, 1.0F));
                }
            });
            return;
        }

        if (!blockRegistry.isPlayerPlaced(block)) {
            event.setCancelled(true);
        }
    }

    private Location getTwinLocation(Bed bed, Block bedBlock) {
        if(bed.getPart() == Bed.Part.HEAD){
            return (bedBlock.getRelative(bed.getFacing())).getLocation();
        }else{
            return (bedBlock.getRelative(bed.getFacing().getOppositeFace())).getLocation();
        }
    }
}
