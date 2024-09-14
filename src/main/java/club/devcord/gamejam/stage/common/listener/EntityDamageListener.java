package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.logic.team.Team;
import club.devcord.gamejam.message.Messenger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        } else {
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            if (event.getDamage() >= player.getHealth()) {
                event.setCancelled(true);
                if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    return;
                }
                game.getTeam(player).ifPresent(team -> {
                    for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                        onlinePlayer.sendRichMessage(Messenger.PREFIX + "<" + team.teamColor().textColor().toString().toLowerCase() + ">" + player.getName() + " <red>ist gestorben");
                    }

                    handleKill(player, team);
                });
            }
        }
    }

    private void handleKill(Player player, Team team) {
        if (team.isAlive()) {
            player.teleport(team.spawnLocation().toBukkitLocation(game.gameMap().bukkitWorld()));
            player.setHealth(20);
        } else {
            team.teamPlayers().remove(player);
            game.spectators().add(player);
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(GameSettings.SPAWN_LOCATION.toBukkitLocation(game.gameMap().bukkitWorld()));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (game.gameStage() != GameStage.IN_GAME) {
            event.setCancelled(true);
        } else {
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            if (event.getDamage() >= player.getHealth()) {
                event.setCancelled(true);
                if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    return;
                }
                
                game.getTeam(player).ifPresent(team -> {
                    if (!(event.getDamager() instanceof Player damager)) {
                        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                            onlinePlayer.sendRichMessage(Messenger.PREFIX + "<" + team.teamColor().textColor().toString().toLowerCase() + ">" + player.getName() + " <red>ist gestorben");
                        }
                    } else {
                        game.getTeam(damager).ifPresent(damaagerTeam -> {
                            for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                                onlinePlayer.sendRichMessage(Messenger.PREFIX +
                                        "<" + damaagerTeam.teamColor().textColor().toString().toLowerCase() + ">" + damager.getName() + " <red>hat " +
                                        "<" + team.teamColor().textColor().toString().toLowerCase() + ">" + player.getName() + " <red>getötet!" );
                            }
                        });
                    }
                    
                    handleKill(player, team);
                });
            }
        }
    }
}
