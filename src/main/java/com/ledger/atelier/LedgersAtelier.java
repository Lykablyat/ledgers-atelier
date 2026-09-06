package com.ledger.atelier;

import com.ledger.atelier.registry.ModCreativeModeTabs;
import com.ledger.atelier.registry.ModItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(LedgersAtelier.MODID)
public class LedgersAtelier {
    public static final String MODID = "ledgers_atelier";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LedgersAtelier(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        LOGGER.info("Ledger's Atelier initialized.");
    }
}
