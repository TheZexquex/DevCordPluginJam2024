package club.devcord.gamejam.stage.ingame.listener;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener implements Listener {
    private final BuggyBedwarsPlugin plugin;

    public EntitySpawnListener(BuggyBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof IronGolem ironGolem)) {
            return;
        }

        ironGolem.clearLootTable();

        ironGolem.getScheduler().runAtFixedRate(plugin, task -> {
            var targets = Bukkit.getOnlinePlayers().stream()
                            .filter(player -> plugin.game().isPlayerInAnyTeam(player))
                            .toList();

            if (targets.isEmpty()) {
                return;
            }

            Player target = null;
            double minimumDistance = Double.MAX_VALUE;
            for (var player : targets) {
                var distance = ironGolem.getLocation().distanceSquared(player.getLocation());
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    target = player;
                }
            }

            ironGolem.setTarget(target);
        }, null, 5 * 20L, 5 * 20L);
    }
}
