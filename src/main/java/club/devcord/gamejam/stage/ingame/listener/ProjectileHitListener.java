package club.devcord.gamejam.stage.ingame.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.SNOWBALL) {
            var block = event.getHitBlock();
            block.getWorld().createExplosion(block.getLocation().add(0.5, 0.5, 0.5), 3f, false, true, Bukkit.getPlayer(event.getEntity().getOwnerUniqueId()));
        }
    }
}
