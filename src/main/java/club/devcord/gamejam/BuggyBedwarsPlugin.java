package club.devcord.gamejam;


import club.devcord.gamejam.command.ShopCommand;
import club.devcord.gamejam.command.ShoutCommand;
import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.command.DebugCommand;
import club.devcord.gamejam.command.ForceStartCommand;
import club.devcord.gamejam.stage.common.listener.*;
import club.devcord.gamejam.stage.ingame.listener.*;
import club.devcord.gamejam.stage.common.listener.BlockBreakListener;
import club.devcord.gamejam.stage.lobby.listener.InventoryInteractListener;
import club.devcord.gamejam.stage.lobby.listener.LobbyQuitListener;
import club.devcord.gamejam.stage.lobby.listener.PlayerInteractListener;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;

import java.util.logging.Level;

public class BuggyBedwarsPlugin extends JavaPlugin {
    private Game game;
    private Messenger messenger;
    private LegacyPaperCommandManager<CommandSender> commandManager;
    //private ServerApi serverApi;

    @Override
    public void onEnable() {
        //this.serverApi = getPlugin(PluginJam.class).api();
        this.messenger = new Messenger(getServer());
        this.game = new Game(this);
        game.startLobbyPhase();

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        var pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new InventoryInteractListener(game), this);
        pluginManager.registerEvents(new PlayerInteractListener(game), this);
        pluginManager.registerEvents(new EntityDamageListener(game, messenger), this);
        pluginManager.registerEvents(new FoodLevelChangeListener(), this);
        pluginManager.registerEvents(new BlockBreakListener(game.blockRegistry(), game, messenger), this);
        pluginManager.registerEvents(new PlayerQuitListener(game, messenger), this);
        pluginManager.registerEvents(new NaturalHealthRegenerationListener(), this);
        pluginManager.registerEvents(new BlockPlaceListener(game), this);
        pluginManager.registerEvents(new ProjectileHitListener(), this);
        pluginManager.registerEvents(new LobbyQuitListener(game, messenger), this);
        pluginManager.registerEvents(new ExplodeListener(game.blockRegistry()), this);
        pluginManager.registerEvents(new PlayerMoveListener(game, messenger), this);
        pluginManager.registerEvents(new TeamBedStateChangeListener(this), this);
        pluginManager.registerEvents(new AsyncChatListener(game, messenger), this);
        pluginManager.registerEvents(new PlayerDropItemListener(), this);
        pluginManager.registerEvents(new ProjectileLaunchListener(this, game), this);
        pluginManager.registerEvents(game.blockRegistry(), this);
    }

    private void registerCommands() {
        try {
            this.commandManager = new LegacyPaperCommandManager<>(
                    this,
                    ExecutionCoordinator.simpleCoordinator(),
                    SenderMapper.identity()
            );

            if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
                commandManager.registerBrigadier();
            }
        } catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Failed to initialize command manager", exception);
            this.getServer().getPluginManager().disablePlugin(this);
        }

        new ForceStartCommand(this).register(commandManager);
        new DebugCommand(this).register(commandManager);
        new ShoutCommand(this).register(commandManager);
        new ShopCommand(this).register(commandManager);
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
