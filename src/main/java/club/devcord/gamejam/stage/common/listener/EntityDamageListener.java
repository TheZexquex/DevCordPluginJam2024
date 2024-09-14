package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final Game game;

    public EntityDamageListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (game.gameStage() != GameStage.IN_GAME) {
            event.setCancelled(true);
        }
    }
}
