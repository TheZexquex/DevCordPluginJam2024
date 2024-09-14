package club.devcord.gamejam.logic.shop.gui;

import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.utils.InventoryUtil;
import club.devcord.gamejam.utils.KeyValue;
import club.devcord.gamejam.utils.MiniComponent;
import club.devcord.gamejam.utils.StringUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public abstract class ShopPurchasableItem extends SimpleItem {
    private final ItemStack resultItem;
    private final KeyValue<ItemStack, Integer> price;
    private @Nullable String itemName;

    public ShopPurchasableItem(@NotNull ItemProvider itemProvider, KeyValue<ItemStack, Integer> price) {
        this(itemProvider, null, price);
    }

    public ShopPurchasableItem(@NotNull ItemProvider itemProvider, @Nullable String itemName, KeyValue<ItemStack, Integer> price) {
        super(itemProvider);
        this.resultItem = itemProvider.get();
        this.itemName = itemName;
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
            player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1.0F, 1.0F));
            player.sendRichMessage(Messenger.PREFIX + "<red>Du hast keinen Platz im Inventar");
            return;
        }

        if (!InventoryUtil.hasEnoughItems(player, price.key(), price.value())) {
            player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1.0F, 1.0F));
            player.sendRichMessage(Messenger.PREFIX + "<red>Du hast nicht genügend <yellow>" + StringUtils.capitalizeEnumConstant(price.key().getType().name()));
            return;
        }

        if (itemName == null) {
            itemName = MiniMessage.miniMessage().serialize(resultItem.displayName());
        }

        InventoryUtil.removeSpecificItemCount(player, price.key(), price.value());
        player.sendRichMessage(Messenger.PREFIX + "<gray>Du hast " +
                itemName +
                " <gray>für <yellow>" +
                price.value() + "x " +
                StringUtils.capitalizeEnumConstant(price.key().getType().name()) +
                " <gray>gekauft"
        );

        giveItem(player);

        player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0F, 1.0F));
    }

    public abstract void giveItem(Player player);
}
