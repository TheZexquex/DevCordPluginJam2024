package club.devcord.gamejam.logic.shop;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.logic.shop.gui.ShopGUI;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcData;
import de.oliver.fancynpcs.api.utils.SkinFetcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShopNPC {
    private final Set<Npc> npcs = new HashSet<>();

    public void create(Game game) {
        int i = GameSettings.SHOP_LOCATIONS.size();
        for (GameSettings.RelativeLocation position : GameSettings.SHOP_LOCATIONS) {
            var location = new Location(game.gameMap().bukkitWorld(), position.x(), position.y(), position.z());
            var data = new NpcData("shop-" + i, UUID.randomUUID(), location);
            var skin = new SkinFetcher("https://cdn.thezexquex.dev/s/q7AstaB2nCeHacd/download/merchant-skin.png");
            data.setSkin(skin);
            data.setDisplayName("<gray>⚖ <dark_gray>| <#eba834>Shop");
            data.setTurnToPlayer(true);
            data.setOnClick(player -> {
                player.sendMessage("test");
                game.getTeamColor(player).ifPresent(color -> {
                    player.sendMessage("test2");
                    new ShopGUI().open(player, color);
                });
            });

            Npc npc = FancyNpcsPlugin.get().getNpcAdapter().apply(data);
            npc.create();
            npcs.add(npc);

            FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
            i--;
        }
    }

    public void spawnForAll() {
        npcs.forEach(Npc::spawnForAll);
    }

    public void spawnForPlayer(Player player) {
        npcs.forEach(npc -> {
            npc.spawn(player);
        });
    }

    public void removeAll() {
        var allNpcs = FancyNpcsPlugin.get().getNpcManager().getAllNpcs();
        allNpcs.forEach(Npc::removeForAll);
        allNpcs.forEach(npc -> FancyNpcsPlugin.get().getNpcManager().removeNpc(npc));
    }
}
