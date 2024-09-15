package club.devcord.gamejam.stage.common.listener;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.logic.GameStage;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.logic.spawner.ItemSpawnScheduler;
import club.devcord.gamejam.logic.team.gui.TeamSelectGUI;
import club.devcord.gamejam.message.Messenger;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
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
    private final BuggyBedwarsPlugin plugin;
    private boolean firstJoin = true;

    public PlayerJoinListener(BuggyBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        event.joinMessage(Component.empty());

        if (firstJoin) {
            firstJoin = false;
            new ItemSpawnScheduler(plugin);
        }

        plugin.game().setupNPCs();

        player.teleport(GameSettings.SPAWN_LOCATION.toBukkitLocation(Bukkit.getWorld("game")));
        player.getInventory().clear();
        player.setGlowing(false);

        player.setScoreboard(plugin.game().teamsScoreBoard());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            FancyNpcsPlugin.get().getNpcManager().getAllNpcs().forEach(npc -> {
                npc.spawn(player);
            });
        });

        if (plugin.game().gameStage() == GameStage.LOBBY) {
            lobbySetup(event);
        } else {
            plugin.game().sideBarScoreboard().show(player);
            plugin.game().spectators().add(player);
            player.setGameMode(GameMode.SPECTATOR);
            player.sendRichMessage(Messenger.PREFIX + "<grey>Das Spiel hat bereits begonnen, du bist Beobachter!");
        }
    }

    private void lobbySetup(PlayerJoinEvent event) {
        var player = event.getPlayer();

        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealth(20);
        plugin.messenger().broadcast(Messenger.PREFIX + "<dark_aqua>" + player.getName() + " <gray>hat das Spiel betreten");

        var onlinePlayerCount = Bukkit.getServer().getOnlinePlayers().size();
        var lobbyCountdown = plugin.game().lobbyCountdown();
        if (onlinePlayerCount == GameSettings.MIN_PLAYERS && (lobbyCountdown == null || !lobbyCountdown.isRunning())) {
            plugin.game().startGameCountDown(GameSettings.LOBBY_COUNTDOWN_SECONDS);
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
