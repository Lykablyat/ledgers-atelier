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

    public static final DeferredItem<com.ledger.atelier.item.AtomicShredderItem> ATOMIC_SHREDDER = ITEMS.registerItem(
            "atomic_shredder",
            com.ledger.atelier.item.AtomicShredderItem::new,
            new Item.Properties().stacksTo(4)
    );

    public static final DeferredItem<com.ledger.atelier.item.SavePointRemoteItem> SAVE_POINT_REMOTE = ITEMS.registerItem(
            "save_point_remote",
            com.ledger.atelier.item.SavePointRemoteItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_PRICKLING = ITEMS.registerItem(
            "charm_of_prickling",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_prickling.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_HUNGER = ITEMS.registerItem(
            "charm_of_hunger",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_hunger.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_WATER = ITEMS.registerItem(
            "charm_of_water",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_water.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_EXPLOSION = ITEMS.registerItem(
            "charm_of_explosion",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_explosion.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_FALLING = ITEMS.registerItem(
            "charm_of_falling",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_falling.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_FIRE = ITEMS.registerItem(
            "charm_of_fire",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_fire.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_FREEZING = ITEMS.registerItem(
            "charm_of_freezing",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_freezing.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_SOUND = ITEMS.registerItem(
            "charm_of_sound",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_sound.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_VOID = ITEMS.registerItem(
            "charm_of_void",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_void.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.CharmItem> CHARM_OF_ERAY = ITEMS.registerItem(
            "charm_of_eray",
            props -> new com.ledger.atelier.item.CharmItem(props, "tooltip.ledgers_atelier.charm_of_eray.desc"),
            new Item.Properties()
    );

    public static final DeferredItem<com.ledger.atelier.item.WingsOfAnAngelItem> WINGS_OF_AN_ANGEL = ITEMS.registerItem(
            "wings_of_an_angel",
            com.ledger.atelier.item.WingsOfAnAngelItem::new,
            new Item.Properties()
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
