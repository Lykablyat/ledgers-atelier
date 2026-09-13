package com.ledger.atelier.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CharmItem extends Item {
    private final String tooltipKey;

    public CharmItem(Properties properties, String tooltipKey) {
        super(properties.stacksTo(1));
        this.tooltipKey = tooltipKey;
    }

    public CharmItem(String tooltipKey) {
        this(new Properties(), tooltipKey);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (this.tooltipKey != null && !this.tooltipKey.isEmpty()) {
            tooltipComponents.add(Component.translatable(this.tooltipKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public static boolean hasCharm(LivingEntity entity, Item item) {
        if (entity instanceof Player player) {
            if (player.getOffhandItem().is(item)) {
                return true;
            }
            for (ItemStack stack : player.getInventory().items) {
                if (stack.is(item)) {
                    return true;
                }
            }
        }
        return false;
    }
}
