package club.devcord.gamejam;


import club.devcord.gamejam.logic.Game;
import de.chojo.pluginjam.PluginJam;
import de.chojo.pluginjam.serverapi.ServerApi;
import org.bukkit.plugin.java.JavaPlugin;

public class CursedBedwarsPlugin extends JavaPlugin {
    private Game game;
    private Messenger messenger;

    @Override
    public void onEnable() {
        ServerApi api = getPlugin(PluginJam.class).api();
        // api.requestRestart();

        this.messenger = new Messenger(getServer());
        this.game = new Game(this);
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
