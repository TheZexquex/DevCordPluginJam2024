package club.devcord.gamejam.stage.ingame.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.message.Messenger;
import com.destroystokyo.paper.MaterialSetTag;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final Game game;

    public BlockPlaceListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var block = event.getBlock();
        if (block.getType() == Material.TNT) {
            block.setType(Material.AIR);
            block.getWorld().spawnEntity(block.getLocation(), EntityType.TNT);
        } else if (MaterialSetTag.BEDS.isTagged(block.getType())) {
            var targetTeamOptional = game.teams().stream()
                    .filter(team -> team.bedLocation().equalsBukkitLoc(event.getBlock().getLocation()))
                    .findAny();

            if (targetTeamOptional.isPresent()) {
                targetTeamOptional.get().setAlive(true);
            } else {
                event.setCancelled(true);

                var player = event.getPlayer();
                player.sendRichMessage(Messenger.PREFIX + "<red>Du kannst das Bett hier nicht platzieren!");
                player.playSound(Sound.sound(Key.key("block.anvil.land"), Sound.Source.MASTER, 1.0F, 1.0F));
            }
        }
    }
}
