package club.devcord.gamejam.logic.spawner;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Phantom;

import java.util.concurrent.ThreadLocalRandom;

public class PhantomScheduler {
    public PhantomScheduler(BuggyBedwarsPlugin plugin) {
        var game = plugin.game();

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            var targets = Bukkit.getOnlinePlayers().stream()
                    .filter(game::isPlayerInAnyTeam)
                    .toList();

            if (targets.isEmpty()) {
                return;
            }

            var target = targets.get(ThreadLocalRandom.current().nextInt(targets.size()));
            target.getWorld().spawn(target.getLocation().add(0, 10, 0), Phantom.class, phantom -> {
                phantom.setAnchorLocation(target.getLocation());

                phantom.getScheduler().runAtFixedRate(plugin, task -> {
                    phantom.setTarget(target);
                }, null, 5L, 20L * 10L);
            });
        }, TimeUtils.durationToTicks(GameSettings.PHANTOM_SPAWN_DURATION), TimeUtils.durationToTicks(GameSettings.PHANTOM_SPAWN_DURATION));
    }
}
