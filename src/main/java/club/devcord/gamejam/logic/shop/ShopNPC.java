package club.devcord.gamejam.logic.shop;

import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.logic.shop.gui.ShopGUI;
import club.devcord.gamejam.utils.RelativeLocation;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcData;
import de.oliver.fancynpcs.api.utils.SkinFetcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShopNPC {
    private final Set<Npc> npcs = new HashSet<>();

    public void create(Game game) {
        FancyNpcsPlugin.get().getNpcManager().getAllNpcs().forEach(npc -> {
            System.out.println(npc.getData().getId() + " " + npc.getData().getName());
        });

        int i = GameSettings.SHOP_LOCATIONS.size();
        for (RelativeLocation position : GameSettings.SHOP_LOCATIONS) {
            var location = position.toBukkitLocation(game.gameMap().bukkitWorld());

            var id = "shop-" + i;
            if (FancyNpcsPlugin.get().getNpcManager().getNpcById(id) == null) {
                return;
            }

            var data = npcData(game, id, location);


            Npc npc = FancyNpcsPlugin.get().getNpcAdapter().apply(data);
            FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
            npc.create();
            i--;
        }
    }

    private static @NotNull NpcData npcData(Game game, String id, Location location) {
        var data = new NpcData(id, UUID.randomUUID(), location);
        var skin = new SkinFetcher("https://cdn.thezexquex.dev/s/q7AstaB2nCeHacd/download/merchant-skin.png");
        data.setType(EntityType.PLAYER);
        data.setSkin(skin);
        data.setDisplayName("<gray>⚖ <dark_gray>| <#eba834>Shop");
        data.setTurnToPlayer(true);
        data.setOnClick(player -> {
            player.sendMessage("Test");
            game.getTeam(player).ifPresent(team -> {
                new ShopGUI().open(player, team.teamColor());
            });
        });
        return data;
    }

    public void clear() {
        FancyNpcsPlugin.get().getNpcManager().getAllNpcs().forEach(npc -> {
            npc.removeForAll();
            FancyNpcsPlugin.get().getNpcManager().removeNpc(npc);
        });
    }
}