# Ledger's Atelier

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen.svg)](https://minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.x-orange.svg)](https://neoforged.net/)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)

**Ledger's Atelier** is a Minecraft mod for **NeoForge 1.21.1** featuring utility gadgets, functional inventory charms, and custom mechanics.

---

## Items

### 🛠️ Gadgets & Utility

- [Icon] **Atomic Shredder**: A painless death, or so it would seem.
- [Icon] **Save-Point Remote**: Save your coordinates and return to them at will. Make sure not to let anyone else get a hold of your remote.
- [Icon] **Debug Gem**: Diagnostic tool used for testing and verifying atelier energy.

---

### 🔮 Charms

Charms provide passive benefits simply by being carried in your inventory or off-hand.

| Charm | Icon / ID | Effect |
| :--- | :--- | :--- |
| **Wings of an Angel** | `wings_of_an_angel` | Grants creative flight while in your inventory. |
| **Charm of Falling** | `charm_of_falling` | Nullifies all fall damage, stalagmite impacts, and elytra collisions. |
| **Charm of Fire** | `charm_of_fire` | Immunity to fire, lava, campfires, and magma blocks. |
| **Charm of Water** | `charm_of_water` | Prevents drowning and normalizes underwater movement and mining. |
| **Charm of Explosion** | `charm_of_explosion` | Immunity to explosions and causes creepers to ignore you. |
| **Charm of Hunger** | `charm_of_hunger` | Permanently satisfies hunger and saturation. |
| **Charm of Freezing** | `charm_of_freezing` | Immunity to freezing and allows walking on powdered snow. |
| **Charm of Prickling** | `charm_of_prickling` | Immunity to cacti, berry bushes, thorns, and guardian spikes. |
| **Charm of Sound** | `charm_of_sound` | Mutes vibrations so sculk sensors and Wardens never detect you. |
| **Charm of Void** | `charm_of_void` | Rescues you from falling into the void by teleporting you to safe ground. |
| **Charm of Eray** | `charm_of_eray` | Enrages neutral mobs within 32 blocks and doubles all damage taken. |

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