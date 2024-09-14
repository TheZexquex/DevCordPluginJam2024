package club.devcord.gamejam;


import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.stage.common.listener.*;
import club.devcord.gamejam.stage.ingame.listener.NaturalHealthRegenerationListener;
import club.devcord.gamejam.stage.lobby.listener.BlockBreakListener;
import club.devcord.gamejam.stage.lobby.listener.InventoryInteractListener;
import club.devcord.gamejam.stage.lobby.listener.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CursedBedwarsPlugin extends JavaPlugin {
    private Game game;
    private Messenger messenger;
    //private ServerApi serverApi;

    @Override
    public void onEnable() {
        //this.serverApi = getPlugin(PluginJam.class).api();
        this.messenger = new Messenger(getServer());
        this.game = new Game(this);
        game.startLobbyPhase();

        registerListeners();
    }

    private void registerListeners() {
        var pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new InventoryInteractListener(game), this);
        pluginManager.registerEvents(new PlayerInteractListener(game), this);
        pluginManager.registerEvents(new EntityDamageListener(game), this);
        pluginManager.registerEvents(new FoodLevelChangeListener(), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(game, messenger), this);
        pluginManager.registerEvents(new NaturalHealthRegenerationListener(), this);
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

    //public ServerApi serverApi() {
    //    return serverApi;
    //}
}
