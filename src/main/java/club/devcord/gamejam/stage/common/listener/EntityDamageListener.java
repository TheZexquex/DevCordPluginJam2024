package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.team.Team;
import club.devcord.gamejam.message.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

// TODO: Add kill sounds
public class EntityDamageListener implements Listener {
    private final Game game;
    private final Messenger messenger;

    public EntityDamageListener(Game game, Messenger messenger) {
        this.game = game;
        this.messenger = messenger;
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
                    var killerOptional = game.damagerRegistry().getDamager(player);
                    if (killerOptional.isPresent()) {
                        var killer = killerOptional.get();
                        game.getTeam(killer).ifPresent(damagerTeam -> {
                            for (var onlinePlayer : player.getServer().getOnlinePlayers()) {
                                onlinePlayer.sendRichMessage(messenger.getDeathMessage(player, team, killer, damagerTeam));
                            }
                        });
                    } else {
                        for (var onlinePlayer : player.getServer().getOnlinePlayers()) {
                            onlinePlayer.sendRichMessage(messenger.getDeathMessage(player, team));
                        }

                        game.handleKill(player);
                    }
                });
            }
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

            if (event.getDamager() instanceof Player damager) {
                game.damagerRegistry().add(player, damager);
            }

            if (event.getDamage() >= player.getHealth()) {
                event.setCancelled(true);
                if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    return;
                }

                game.getTeam(player).ifPresent(team -> {
                    if (!(event.getDamager() instanceof Player damager)) {
                        for (var onlinePlayer : player.getServer().getOnlinePlayers()) {
                            onlinePlayer.sendRichMessage(messenger.getDeathMessage(player, team));
                        }
                    } else {
                        game.getTeam(damager).ifPresent(damagerTeam -> {
                            for (var onlinePlayer : player.getServer().getOnlinePlayers()) {
                                onlinePlayer.sendRichMessage(messenger.getDeathMessage(player, team, damager, damagerTeam));
                            }
                        });
                    }
                    
                    game.handleKill(player);
                });
            }
        }
    }
}
