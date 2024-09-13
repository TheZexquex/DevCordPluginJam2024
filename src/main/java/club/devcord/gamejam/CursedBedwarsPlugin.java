package club.devcord.gamejam;


import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.stage.common.listener.PlayerJoinListener;
import club.devcord.gamejam.stage.lobby.listener.InventoryInteractListener;
import club.devcord.gamejam.stage.lobby.listener.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CursedBedwarsPlugin extends JavaPlugin {
    private Game game;
    private Messenger messenger;

    @Override
    public void onEnable() {
        // api.requestRestart();

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

    }

    public Messenger messenger() {
        return messenger;
    }

    public Game game() {
        return game;
    }
}
