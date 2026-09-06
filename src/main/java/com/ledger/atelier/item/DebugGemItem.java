package com.ledger.atelier.item;

import com.ledger.atelier.LedgersAtelier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugGemItem extends Item {
    public DebugGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            player.sendSystemMessage(Component.translatable("message.ledgers_atelier.debug_gem_clicked"));
            LedgersAtelier.LOGGER.info("Debug Gem right-clicked by player {} at {}", player.getName().getString(), player.blockPosition());
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
