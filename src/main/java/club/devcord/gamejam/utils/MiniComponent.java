package club.devcord.gamejam.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;

public class MiniComponent {
    public static AdventureComponentWrapper of(String miniMessage) {
        return new AdventureComponentWrapper(MiniMessage.miniMessage().deserialize(miniMessage));
    }
}
