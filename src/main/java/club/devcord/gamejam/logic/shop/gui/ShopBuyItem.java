package club.devcord.gamejam.logic.shop.gui;

import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.utils.InventoryUtil;
import club.devcord.gamejam.utils.KeyValue;
import club.devcord.gamejam.utils.MiniComponent;
import club.devcord.gamejam.utils.StringUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class ShopBuyItem extends SimpleItem {
    private final ItemStack resultItem;
    private final KeyValue<ItemStack, Integer> price;

    public ShopBuyItem(@NotNull ItemProvider itemProvider, KeyValue<ItemStack, Integer> price) {
        super(itemProvider);
        this.resultItem = itemProvider.get();
        this.price = price;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(resultItem).addLoreLines(MiniComponent.of(""), MiniComponent.of(
                " <gray>Für <yellow>" +
                        price.value() + "x " +
                        StringUtils.capitalizeEnumConstant(price.key().getType().name()) +
                        " <gray>kaufen"
        ));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        super.handleClick(clickType, player, event);

        if (InventoryUtil.hasNoSpaceInInventory(player)) {
            player.sendRichMessage(Messenger.PREFIX + "<red>Du hast keinen Platz im Inventar");
            return;
        }

        if (!InventoryUtil.hasEnoughItems(player, price.key(), price.value())) {
            player.sendRichMessage(Messenger.PREFIX + "<red>Du hast nicht genügend <yellow>" + StringUtils.capitalizeEnumConstant(price.key().getType().name()));
            return;
        }

        InventoryUtil.removeSpecificItemCount(player, price.key(), price.value());
        player.sendRichMessage(Messenger.PREFIX + "<gray>Du hast " +
                MiniMessage.miniMessage().serialize(resultItem.displayName()) +
                " <gray>für <yellow>" +
                price.value() + "x " +
                StringUtils.capitalizeEnumConstant(price.key().getType().name()) +
                " <gray>gekauft"
        );
        player.getInventory().addItem(resultItem);
    }
}
