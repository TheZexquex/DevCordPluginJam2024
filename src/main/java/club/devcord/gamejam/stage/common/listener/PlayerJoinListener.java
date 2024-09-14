package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.logic.spawner.ItemSpawnScheduler;
import club.devcord.gamejam.logic.team.gui.TeamSelectGUI;
import club.devcord.gamejam.message.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class PlayerJoinListener implements Listener {
    private final CursedBedwarsPlugin plugin;
    private boolean firstJoin = true;

    public PlayerJoinListener(CursedBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        if (firstJoin) {
            firstJoin = false;
            plugin.game().setupNPCs();
            new ItemSpawnScheduler(plugin);
        }

        player.teleport(GameSettings.SPAWN_LOCATION.toBukkitLocation(Bukkit.getWorld("game")));
        player.getInventory().clear();
        player.setGlowing(false);

        player.setScoreboard(plugin.game().teamsScoreBoard());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.game().shopNPC().spawnForPlayer(player);
        });

        if (plugin.game().gameStage() == GameStage.LOBBY) {
            lobbySetup(event);
        } else {
            plugin.game().switchPlayerToTeam(player, NamedTextColor.GRAY);
            player.setGameMode(GameMode.SPECTATOR);
            player.sendRichMessage(Messenger.PREFIX + "<grey>Das Spiel hat bereits begonnen, du bist Beobachter!");
        }
    }

    private void lobbySetup(PlayerJoinEvent event) {
        var player = event.getPlayer();

        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealth(20);
        event.joinMessage(Component.empty());
        plugin.messenger().broadCast(Messenger.PREFIX + "<dark_aqua>" + player.getName() + " <gray>hat das Spiel betreten");

        if (plugin.getServer().getOnlinePlayers().size() == GameSettings.MIN_PLAYERS) {
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
}
