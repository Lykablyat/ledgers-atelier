package com.ledger.atelier.handler;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.item.SavePointRemoteItem;
import com.ledger.atelier.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = LedgersAtelier.MODID)
public class SavePointEventHandler {
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (stack.is(ModItems.SAVE_POINT_REMOTE.get())) {
            event.setCanceled(true);
            if (player instanceof ServerPlayer serverPlayer && stack.getItem() instanceof SavePointRemoteItem remoteItem) {
                remoteItem.executeSave(serverPlayer, stack);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof ItemEntity itemEntity && !itemEntity.level().isClientSide()) {
            ItemStack itemStack = itemEntity.getItem();
            if (itemStack.is(Items.NETHER_STAR)) {
                Level level = itemEntity.level();
                boolean inPortal = level.getBlockStates(itemEntity.getBoundingBox())
                        .anyMatch(state -> state.is(Blocks.NETHER_PORTAL));

                if (inPortal) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    double x = itemEntity.getX();
                    double y = itemEntity.getY();
                    double z = itemEntity.getZ();

                    int count = itemStack.getCount();
                    if (count > 1) {
                        itemStack.shrink(1);
                        ItemEntity remoteEntity = new ItemEntity(
                                serverLevel, x, y, z,
                                new ItemStack(ModItems.SAVE_POINT_REMOTE.get())
                        );
                        remoteEntity.setDeltaMovement(0.0, 0.2, 0.0);
                        remoteEntity.setPortalCooldown();
                        serverLevel.addFreshEntity(remoteEntity);
                    } else {
                        itemEntity.setItem(new ItemStack(ModItems.SAVE_POINT_REMOTE.get()));
                        itemEntity.setDeltaMovement(0.0, 0.2, 0.0);
                        itemEntity.setPortalCooldown();
                    }

                    // Transmutation FX
                    serverLevel.sendParticles(ParticleTypes.FLASH, x, y + 0.3, z, 1, 0.0, 0.0, 0.0, 0.0);
                    serverLevel.sendParticles(ParticleTypes.PORTAL, x, y + 0.3, z, 40, 0.4, 0.4, 0.4, 0.2);
                    serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, x, y + 0.3, z, 20, 0.3, 0.3, 0.3, 0.1);
                    serverLevel.sendParticles(ParticleTypes.END_ROD, x, y + 0.3, z, 10, 0.2, 0.3, 0.2, 0.05);

                    serverLevel.playSound(null, x, y, z, SoundEvents.TOTEM_USE, SoundSource.BLOCKS, 0.8F, 1.6F);
                    serverLevel.playSound(null, x, y, z, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 1.0F, 1.4F);
                }
            }
        }
    }
}
