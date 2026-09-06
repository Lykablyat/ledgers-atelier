---
trigger: always_on
---

# Ledger's Atelier - Development Guidelines

- Target: Minecraft 1.21.1
- Mod Loader: NeoForge (latest stable 21.1.x)
- Language: Java 21
- Mod ID: ledgers_atelier
- Base Package: com.ledger.atelier

## Architectural Rules:
1. Always use NeoForge's DeferredRegister and DeferredHolder.
2. For custom item properties, use Minecraft 1.21+ Data Components, NOT legacy NBT tags.
3. Keep items modular: separate registries, item logic classes, and client-side rendering.
4. Provide clean JSON data definitions for models, textures, and lang keys under src/main/resources.
5. Always refer to the official NeoForge documentation provided in neoforge-docs folder before implementing features.