package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.team.gui.TeamSelectGUI;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.logic.settings.GameSettings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class PlayerJoinListener implements Listener {
    private final CursedBedwarsPlugin plugin;

    public PlayerJoinListener(CursedBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        player.teleport(plugin.game().gameMap().getBukkitWorld().getSpawnLocation());
        player.getInventory().clear();

        switch (plugin.game().gameStage()) {
            case LOBBY -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.setFoodLevel(20);
                player.setHealth(20);
                event.joinMessage(Component.empty());
                plugin.messenger().broadCast(Messenger.PREFIX + "<yellow>" + player.getName() + " <gray>hat das Spiel betreten");

                if (plugin.getServer().getOnlinePlayers().size() >= GameSettings.MIN_PLAYERS) {
                    plugin.game().startGameCountDown();
                }
                var teamSelectItem = new ItemBuilder(Material.RED_BED)
                        .setDisplayName(new AdventureComponentWrapper(Component.text("Team Auswahl").color(NamedTextColor.YELLOW)))
                        .get();

                var teamSelectItemMeta = teamSelectItem.getItemMeta();
                teamSelectItemMeta.getPersistentDataContainer().set(TeamSelectGUI.OPEN_ITEM_KEY, PersistentDataType.BOOLEAN, true);

                teamSelectItem.setItemMeta(teamSelectItemMeta);

                player.getInventory().setItem(8, teamSelectItem);
            }
            default -> {
                plugin.game().switchPlayerToTeam(player, NamedTextColor.GRAY);
                player.setGameMode(GameMode.SPECTATOR);
                player.sendRichMessage(Messenger.PREFIX + "<grey>Das Spiel hat begonnen, du bist Beobachter!");
            }
        }
    }

    private void teleportToSpawn(Player player) {
        //var spawnLocation = new Location()
    }
}
