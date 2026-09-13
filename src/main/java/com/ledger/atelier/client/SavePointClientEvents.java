package com.ledger.atelier.client;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.network.SavePointLeftClickPayload;
import com.ledger.atelier.registry.ModItems;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = LedgersAtelier.MODID, value = Dist.CLIENT)
public class SavePointClientEvents {
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().is(ModItems.SAVE_POINT_REMOTE.get()) ||
                player.getOffhandItem().is(ModItems.SAVE_POINT_REMOTE.get())) {
            PacketDistributor.sendToServer(new SavePointLeftClickPayload());
        }
    }
}
