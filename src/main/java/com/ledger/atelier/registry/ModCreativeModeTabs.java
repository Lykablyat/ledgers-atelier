package com.ledger.atelier.registry;

import com.ledger.atelier.LedgersAtelier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LedgersAtelier.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(
            "ledgers_atelier_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ledgers_atelier"))
                    .icon(() -> new ItemStack(ModItems.DEBUG_GEM.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.DEBUG_GEM.get());
                        output.accept(ModItems.ATOMIC_SHREDDER.get());
                        output.accept(ModItems.SAVE_POINT_REMOTE.get());
                        output.accept(ModItems.CHARM_OF_PRICKLING.get());
                        output.accept(ModItems.CHARM_OF_HUNGER.get());
                        output.accept(ModItems.CHARM_OF_WATER.get());
                        output.accept(ModItems.CHARM_OF_EXPLOSION.get());
                        output.accept(ModItems.CHARM_OF_FALLING.get());
                        output.accept(ModItems.CHARM_OF_FIRE.get());
                        output.accept(ModItems.CHARM_OF_FREEZING.get());
                        output.accept(ModItems.CHARM_OF_SOUND.get());
                        output.accept(ModItems.CHARM_OF_VOID.get());
                        output.accept(ModItems.CHARM_OF_ERAY.get());
                        output.accept(ModItems.WINGS_OF_AN_ANGEL.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
