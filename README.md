# Ledger's Atelier

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen.svg)](https://minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.x-orange.svg)](https://neoforged.net/)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)

**Ledger's Atelier** is a Minecraft mod for **NeoForge 1.21.1** featuring utility gadgets, functional inventory charms, and custom mechanics.

---

## Features & Items

All items are organized under the custom creative tab: **Ledger's Atelier**.

### 🛠️ Gadgets & Utility

#### **Atomic Shredder**
- **Type:** Consumable Syringe (Max stack: 4)
- **Recipe:** Shaped crafting using Iron Ingots, Iron Nuggets, an Ender Pearl, a Glass Bottle, and Chorus Fruit.
- **Usage:** Hold right-click to channel for 2 seconds (40 ticks).
- **Features:**
  - Custom first-person injection animation smoothly raising the syringe with intensifying vibrations.
  - Glowing silhouette aura, ascending electrical sparks, and pitch-shifting amethyst chimes while channeling.
  - Safely disintegrates and teleports the user across dimensions directly to their respawn point / bed / respawn anchor.
  - Departure and arrival burst FX (flash & campfire smoke).
  - Broadcasts a custom server-wide message: *"[Player] took the easy way out."*
  - Fails cleanly if no valid spawn tether is set.

#### **Save-Point Remote**
- **Type:** Coordinate Tether & Recall Remote (Max stack: 1)
- **Features:**
  - **Left-Click (Air, Blocks, or Entities):** Links the remote to the user and records the exact coordinates and dimension. Repeated left-clicks update the target destination.
  - **Right-Click:** Recalls the tethered player across dimensions directly to the saved coordinates with portal visual effects and sound.
  - Displays dynamic tooltip showing the bound player's name, dimension, and coordinates.
  - Emits an enchantment glint when bound.

#### **Debug Gem**
- **Type:** Testing & Diagnostic Item
- **Usage:** Right-click to trigger atelier energy feedback and log coordinates to the console.

---

### 🔮 Passive Inventory Charms

Charms provide passive benefits simply by being carried anywhere in your inventory or off-hand slot.

| Charm | Icon / ID | Effect |
| :--- | :--- | :--- |
| **Wings of an Angel** | `wings_of_an_angel` | Grants smooth **creative flight** in Survival mode while in your inventory. |
| **Charm of Falling** | `charm_of_falling` | Nullifies all **fall damage**, stalagmite impalement, and kinetic elytra wall collisions. |
| **Charm of Fire** | `charm_of_fire` | Total immunity to **fire, lava, magma blocks, and campfires**. Clears fire ticks instantly and removes the screen fire overlay. |
| **Charm of Water** | `charm_of_water` | Infinite breath (no drowning), normalizes underwater walking speed, and grants submerged mining speed (**Aqua Affinity**). |
| **Charm of Explosion** | `charm_of_explosion` | Complete immunity to all **explosion damage** (TNT, beds, end crystals) and makes creepers ignore the player. |
| **Charm of Hunger** | `charm_of_hunger` | Locks **hunger and saturation** at maximum; clears exhaustion. |
| **Charm of Freezing** | `charm_of_freezing` | Complete immunity to **freeze and powdered snow damage**. Allows walking on top of powdered snow without sinking. |
| **Charm of Prickling** | `charm_of_prickling` | Complete immunity to **cacti, sweet berry bushes, thorns enchantment, and guardian spikes**. Clears berry bush movement slowdown. |
| **Charm of Sound** | `charm_of_sound` | Mutes all vibrations caused by the player, preventing **Sculk Sensors and the Warden** from detecting you. |
| **Charm of Void** | `charm_of_void` | **Emergency Void Rescue:** If you fall below the world's minimum build height, cancels void damage and teleports you to the nearest solid ground. |
| **Charm of Eray** | `charm_of_eray` | *(Troll Charm)* **Doubles all damage taken** and enrages all nearby neutral mobs (Iron Golems, Piglins, Spiders, Zombified Piglins) within 32 blocks. |

---

## Development & Building

### Requirements
- **JDK 21**
- **Gradle 8.10+** (or use the included `./gradlew` wrapper)

### Build Commands
```bash
# Build mod JAR
./gradlew build

# Run Client for testing
./gradlew runClient

# Run Server for testing
./gradlew runServer
```

---

## License

This project is licensed under the terms found in `TEMPLATE_LICENSE.txt`.