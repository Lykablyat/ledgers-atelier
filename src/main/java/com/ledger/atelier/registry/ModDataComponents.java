package com.ledger.atelier.registry;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.component.SavePointData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, LedgersAtelier.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SavePointData>> SAVE_POINT =
            DATA_COMPONENTS.registerComponentType(
                    "save_point",
                    builder -> builder
                            .persistent(SavePointData.CODEC)
                            .networkSynchronized(SavePointData.STREAM_CODEC)
            );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
