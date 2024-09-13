package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.GameStage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final CursedBedwarsPlugin plugin;

    public EntityDamageListener(CursedBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (plugin.game().gameStage() != GameStage.IN_GAME) {
            event.setCancelled(true);
        }
    }
}
