package club.devcord.gamejam.stage.ingame.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        var projectile = event.getEntity().getLocation();
        var entityType = event.getEntityType();

        if (entityType == EntityType.SNOWBALL) {
            projectile.getWorld().createExplosion(projectile, 3f, false, true, Bukkit.getPlayer(event.getEntity().getOwnerUniqueId()));
        }
    }
}
