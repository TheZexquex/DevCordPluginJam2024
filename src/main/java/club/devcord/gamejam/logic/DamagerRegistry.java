package club.devcord.gamejam.logic;

import club.devcord.gamejam.logic.settings.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class DamagerRegistry {
    private final HashMap<UUID, DamageData> damageHistory = new HashMap<>();

    public void add(Player player, Player damager) {
        damageHistory.put(player.getUniqueId(), new DamageData(damager.getUniqueId(), System.currentTimeMillis()));
    }

    public void remove(Player player) {
        damageHistory.remove(player.getUniqueId());
    }

    public Optional<Player> getDamager(Player player) {
        if (!damageHistory.containsKey(player.getUniqueId())) {
            return Optional.empty();
        }

        var damageData = damageHistory.get(player.getUniqueId());
        if (damageData.timestamp + GameSettings.KILL_CREDIT_DURATION.toMillis() < System.currentTimeMillis()) {
            return Optional.empty();
        }

        return Optional.ofNullable(Bukkit.getPlayer(damageData.damager()));
    }

    private record DamageData(UUID damager, long timestamp) {}
}
