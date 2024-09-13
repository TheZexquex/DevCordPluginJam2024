package club.devcord.gamejam;


import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.stage.common.listener.PlayerJoinListener;
import club.devcord.gamejam.stage.lobby.listener.InventoryInteractListener;
import club.devcord.gamejam.stage.lobby.listener.PlayerInteractListener;
import de.chojo.pluginjam.PluginJam;
import de.chojo.pluginjam.serverapi.ServerApi;
import org.bukkit.plugin.java.JavaPlugin;

public class CursedBedwarsPlugin extends JavaPlugin {
    private Game game;
    private Messenger messenger;
    private ServerApi serverApi;

    @Override
    public void onEnable() {
        this.serverApi = getPlugin(PluginJam.class).api();
        this.messenger = new Messenger(getServer());
        this.game = new Game(this);
        var pluginManager = this.getServer().getPluginManager();

        // Listeners
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new InventoryInteractListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
    }

    @Override
    public void onDisable() {
        game.tearDown();
    }

    public Messenger messenger() {
        return messenger;
    }

    public Game game() {
        return game;
    }

    public ServerApi serverApi() {
        return serverApi;
    }
}
