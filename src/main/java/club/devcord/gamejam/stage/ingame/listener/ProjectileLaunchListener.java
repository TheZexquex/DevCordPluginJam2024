package club.devcord.gamejam.stage.ingame.listener;

import club.devcord.gamejam.logic.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;

public class ProjectileLaunchListener implements Listener {
    private final Plugin plugin;
    private final Game game;

    public ProjectileLaunchListener(Plugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    @EventHandler
    public void onProjectileHit(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Egg egg && event.getEntity().getShooter() instanceof Player player) {
            game.getTeam(player).ifPresent(team -> {
                var woolMaterial = Material.valueOf(team.teamColor().textColor().toString().toUpperCase() + "_WOOL");

                egg.getScheduler().runAtFixedRate(plugin, (scheduledTask) -> {
                    if (egg.getLocation().getY() < 30) {
                        egg.remove();
                        return;
                    }

                    var block = egg.getLocation().getBlock();
                    if (block.getType() == Material.AIR) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> block.setType(woolMaterial), 10L);
                    }
                }, null, 5L, 1L);
            });
        }
    }
}
