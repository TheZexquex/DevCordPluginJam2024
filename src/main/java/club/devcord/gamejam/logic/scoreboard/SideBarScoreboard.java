package club.devcord.gamejam.logic.scoreboard;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.message.Messenger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SideBarScoreboard {
    private final BuggyBedwarsPlugin plugin;
    private final Sidebar sidebar;
    private final ComponentSidebarLayout sidebarLayout;

    public SideBarScoreboard(BuggyBedwarsPlugin plugin) {
        this.plugin = plugin;

        ScoreboardLibrary scoreboardLibrary = null;
        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(plugin);
        } catch (NoPacketAdapterAvailableException e) {
            // Ignore this
        }

        var lines = SidebarComponent.builder()
                .addBlankLine()
                .addComponent(SidebarComponent.staticLine(MiniMessage.miniMessage().deserialize("<gray>\uD83D\uDC6A | Tᴇᴀᴍs")));

        plugin.game().teams().forEach(team -> {
            if (!team.empty()) {
                lines.addBlankLine();
                lines.addDynamicLine(() -> {
                    var teamName = " " + team.getFormattedName();
                    String teamStatus;
                    if (team.empty() || (!team.alive() && team.teamPlayers().isEmpty())) {
                        teamStatus = "  <red>❌";
                    } else if (team.alive()) {
                        teamStatus = "  <green>✔";
                    } else {
                        teamStatus = "  <yellow>⚔";
                    }

                    var teamAlive  = " <gray>(<yellow>" + team.teamPlayers().size() + "<gray>)";

                    return MiniMessage.miniMessage().deserialize(teamStatus + teamName + teamAlive);
                });
            }
        });

        var title = MiniMessage.miniMessage().deserialize("     " + Messenger.PREFIX + "   ");
        sidebarLayout = new ComponentSidebarLayout(SidebarComponent.staticLine(title), lines.build());
        sidebar = scoreboardLibrary.createSidebar();

        Bukkit.getScheduler().runTaskTimer(plugin, this::updateScoreboard, 0L, 5L);
    }

    private void updateScoreboard() {
        sidebarLayout.apply(sidebar);
    }

    public void show(Player player) {
        sidebar.addPlayer(player);
    }

    public void hide(Player player) {
        sidebar.removePlayer(player);
    }
}
