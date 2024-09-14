package club.devcord.gamejam.stage.common.listener;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {
    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        event.message(Component.empty());
    }

    @EventHandler
    public void onAdvancementProgress(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }
}
