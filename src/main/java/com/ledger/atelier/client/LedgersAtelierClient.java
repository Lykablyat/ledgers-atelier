package com.ledger.atelier.client;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.registry.ModItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = LedgersAtelier.MODID, value = Dist.CLIENT)
public class LedgersAtelierClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LedgersAtelier.LOGGER.info("Ledger's Atelier client setup complete.");
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new AtomicShredderClientExtensions(), ModItems.ATOMIC_SHREDDER.get());
    }
}
