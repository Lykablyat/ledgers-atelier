package com.ledger.atelier.client;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.item.CharmItem;
import com.ledger.atelier.registry.ModItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

@EventBusSubscriber(modid = LedgersAtelier.MODID, value = Dist.CLIENT)
public class CharmClientEventHandler {

    @SubscribeEvent
    public static void onRenderBlockScreenEffect(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE) {
            if (CharmItem.hasCharm(event.getPlayer(), ModItems.CHARM_OF_FIRE.get())) {
                event.setCanceled(true);
            }
        }
    }
}
