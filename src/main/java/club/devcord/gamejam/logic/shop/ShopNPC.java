package club.devcord.gamejam.logic.shop;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.shop.gui.ShopGUI;
import com.github.juliarn.npclib.api.Npc;
import com.github.juliarn.npclib.api.Platform;
import com.github.juliarn.npclib.api.event.InteractNpcEvent;
import com.github.juliarn.npclib.api.profile.Profile;
import com.github.juliarn.npclib.bukkit.BukkitPlatform;
import com.github.juliarn.npclib.bukkit.util.BukkitPlatformUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ShopNPC {
    private Platform<World, Player, ItemStack, Plugin> platform;

    public void configure(CursedBedwarsPlugin plugin) {
        platform = BukkitPlatform.bukkitNpcPlatformBuilder()
                .actionController(builder -> {})
                .build();
    }

    public void registerNpcListeners(Npc<World, Player, ItemStack, Plugin> toCheck) {
        var eventManager = this.platform.eventManager();
        eventManager.registerEventHandler(InteractNpcEvent.class, interactEvent -> {
            var npc = interactEvent.npc();
            var player = (Player) interactEvent.player();

            new ShopGUI().open(player);
        });

    }

    public void spawn(Location location) {

        this.platform.newNpcBuilder()
                .position(BukkitPlatformUtil.positionFromBukkitLegacy(location))
                .profile(Profile.unresolved("TheZexquex"))
                .thenAccept(builder -> {
                    var npc = builder.buildAndTrack();
                    registerNpcListeners(npc);
                });
    }
}
