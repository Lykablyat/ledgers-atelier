package com.ledger.atelier.registry;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.item.DebugGemItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LedgersAtelier.MODID);

    public static final DeferredItem<DebugGemItem> DEBUG_GEM = ITEMS.registerItem(
            "debug_gem",
            DebugGemItem::new,
            new Item.Properties()
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
