package com.ledger.atelier.network;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.item.SavePointRemoteItem;
import com.ledger.atelier.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = LedgersAtelier.MODID)
public class ModNetwork {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                SavePointLeftClickPayload.TYPE,
                SavePointLeftClickPayload.STREAM_CODEC,
                ModNetwork::handleLeftClick
        );
    }

    private static void handleLeftClick(SavePointLeftClickPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                ItemStack stack = serverPlayer.getMainHandItem();
                if (!stack.is(ModItems.SAVE_POINT_REMOTE.get())) {
                    stack = serverPlayer.getOffhandItem();
                }
                if (stack.is(ModItems.SAVE_POINT_REMOTE.get()) && stack.getItem() instanceof SavePointRemoteItem remoteItem) {
                    remoteItem.executeSave(serverPlayer, stack);
                }
            }
        });
    }
}
