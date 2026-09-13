package com.ledger.atelier.handler;

import com.ledger.atelier.LedgersAtelier;
import com.ledger.atelier.item.CharmItem;
import com.ledger.atelier.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = LedgersAtelier.MODID)
public class CharmEventHandler {

    private static final ResourceLocation WATER_WALK_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(LedgersAtelier.MODID, "water_movement_efficiency");
    private static final ResourceLocation WATER_MINE_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(LedgersAtelier.MODID, "submerged_mining_speed");

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        DamageSource source = event.getSource();

        // Charm of Prickling: Cacti, berry bushes, thorns enchantment, guardian spikes
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_PRICKLING.get())) {
            if (source.is(DamageTypes.CACTUS)
                    || source.is(DamageTypes.SWEET_BERRY_BUSH)
                    || source.is(DamageTypes.THORNS)) {
                event.setCanceled(true);
                return;
            }
        }

        // Charm of Water: Prevents drowning
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_WATER.get())) {
            if (source.is(DamageTypes.DROWN)) {
                event.setCanceled(true);
                return;
            }
        }

        // Charm of Explosion: Prevents all explosion damage
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_EXPLOSION.get())) {
            if (source.is(DamageTypeTags.IS_EXPLOSION)
                    || source.is(DamageTypes.EXPLOSION)
                    || source.is(DamageTypes.PLAYER_EXPLOSION)) {
                event.setCanceled(true);
                return;
            }
        }

        // Charm of Falling: Fall damage, stalagmites, elytra wall collisions
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_FALLING.get())) {
            if (source.is(DamageTypeTags.IS_FALL)
                    || source.is(DamageTypes.FALL)
                    || source.is(DamageTypes.STALAGMITE)
                    || source.is(DamageTypes.FLY_INTO_WALL)) {
                event.setCanceled(true);
                return;
            }
        }

        // Charm of Fire: Lava, magma block, fire, campfires (fireball impact damage kept)
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_FIRE.get())) {
            if (source.is(DamageTypes.LAVA)
                    || source.is(DamageTypes.HOT_FLOOR)
                    || source.is(DamageTypes.IN_FIRE)
                    || source.is(DamageTypes.ON_FIRE)
                    || source.is(DamageTypes.CAMPFIRE)) {
                event.setCanceled(true);
                return;
            }
        }

        // Charm of Freezing: Prevents frost/freeze damage
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_FREEZING.get())) {
            if (source.is(DamageTypes.FREEZE)) {
                event.setCanceled(true);
                return;
            }
        }

        // Charm of Void: Prevents void death and teleports to safety
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_VOID.get())) {
            if (source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                event.setCanceled(true);
                teleportToSafety(player);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        // Charm of Eray: Doubles all damage taken
        if (event.getEntity() instanceof Player player && CharmItem.hasCharm(player, ModItems.CHARM_OF_ERAY.get())) {
            event.setNewDamage(event.getNewDamage() * 2.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // 1. Wings of an Angel: Creative flight
        boolean hasWings = CharmItem.hasCharm(player, ModItems.WINGS_OF_AN_ANGEL.get());
        if (!player.isCreative() && !player.isSpectator()) {
            if (hasWings) {
                if (!player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = true;
                    player.onUpdateAbilities();
                }
            } else {
                if (player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                }
            }
        }

        // 2. Charm of Hunger: Max hunger and saturation
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_HUNGER.get())) {
            FoodData food = player.getFoodData();
            if (food.needsFood() || food.getSaturationLevel() < 20.0F || food.getExhaustionLevel() > 0.0F) {
                food.setFoodLevel(20);
                food.setSaturation(20.0F);
                food.setExhaustion(0.0F);
            }
        }

        // 3. Charm of Water: Max air + water walking & underwater mining speed
        boolean hasWaterCharm = CharmItem.hasCharm(player, ModItems.CHARM_OF_WATER.get());
        if (hasWaterCharm) {
            player.setAirSupply(player.getMaxAirSupply());
        }
        AttributeInstance waterWalk = player.getAttribute(Attributes.WATER_MOVEMENT_EFFICIENCY);
        if (waterWalk != null) {
            boolean hasMod = waterWalk.getModifier(WATER_WALK_MODIFIER) != null;
            if (hasWaterCharm && !hasMod) {
                waterWalk.addTransientModifier(new AttributeModifier(WATER_WALK_MODIFIER, 1.0, AttributeModifier.Operation.ADD_VALUE));
            } else if (!hasWaterCharm && hasMod) {
                waterWalk.removeModifier(WATER_WALK_MODIFIER);
            }
        }
        AttributeInstance waterMine = player.getAttribute(Attributes.SUBMERGED_MINING_SPEED);
        if (waterMine != null) {
            boolean hasMod = waterMine.getModifier(WATER_MINE_MODIFIER) != null;
            if (hasWaterCharm && !hasMod) {
                waterMine.addTransientModifier(new AttributeModifier(WATER_MINE_MODIFIER, 0.8, AttributeModifier.Operation.ADD_VALUE));
            } else if (!hasWaterCharm && hasMod) {
                waterMine.removeModifier(WATER_MINE_MODIFIER);
            }
        }

        // 4. Charm of Fire: Clears fire ticks so player never catches fire
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_FIRE.get())) {
            if (player.getRemainingFireTicks() > 0) {
                player.clearFire();
            }
        }

        // 5. Charm of Prickling: Clears sweet berry bush slowdown
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_PRICKLING.get())) {
            BlockPos pos = player.blockPosition();
            Level level = player.level();
            if (level.getBlockState(pos).is(Blocks.SWEET_BERRY_BUSH)
                    || level.getBlockState(pos.above()).is(Blocks.SWEET_BERRY_BUSH)) {
                player.makeStuckInBlock(Blocks.AIR.defaultBlockState(), Vec3.ZERO);
            }
        }

        // 6. Charm of Freezing: Clears freezing + powder snow walking
        if (CharmItem.hasCharm(player, ModItems.CHARM_OF_FREEZING.get())) {
            if (player.getTicksFrozen() > 0) {
                player.setTicksFrozen(0);
            }
            Level level = player.level();
            BlockPos belowPos = BlockPos.containing(player.getX(), player.getY() - 0.05, player.getZ());
            if (level.getBlockState(belowPos).is(Blocks.POWDER_SNOW)) {
                if (!player.isShiftKeyDown()) {
                    if (player.getDeltaMovement().y < 0) {
                        player.setDeltaMovement(player.getDeltaMovement().x, 0.0, player.getDeltaMovement().z);
                    }
                    player.setOnGround(true);
                    player.resetFallDistance();
                }
            }
            BlockPos feetPos = player.blockPosition();
            if (level.getBlockState(feetPos).is(Blocks.POWDER_SNOW)) {
                player.makeStuckInBlock(Blocks.AIR.defaultBlockState(), Vec3.ZERO);
            }
        }

        // 7. Charm of Void: Teleport to safety before dying
        if (!player.level().isClientSide() && CharmItem.hasCharm(player, ModItems.CHARM_OF_VOID.get())) {
            if (player.getY() < player.level().getMinBuildHeight() - 2) {
                teleportToSafety(player);
            }
        }

        // 8. Charm of Eray: Neutral mobs within 32 blocks become aggressive
        if (!player.level().isClientSide() && player.tickCount % 10 == 0 && CharmItem.hasCharm(player, ModItems.CHARM_OF_ERAY.get())) {
            if (player.level() instanceof ServerLevel serverLevel) {
                List<Mob> nearbyMobs = serverLevel.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(32.0));
                for (Mob mob : nearbyMobs) {
                    if (mob instanceof NeutralMob neutralMob) {
                        neutralMob.setPersistentAngerTarget(player.getUUID());
                        neutralMob.setRemainingPersistentAngerTime(400);
                        mob.setTarget(player);
                    } else if (mob instanceof IronGolem ironGolem) {
                        ironGolem.setTarget(player);
                    } else if (mob instanceof Spider spider) {
                        spider.setTarget(player);
                    } else if (mob instanceof Piglin piglin) {
                        piglin.setTarget(player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        // Charm of Explosion: Creepers ignore the player
        if (event.getEntity() instanceof Creeper && event.getNewAboutToBeSetTarget() instanceof Player player) {
            if (CharmItem.hasCharm(player, ModItems.CHARM_OF_EXPLOSION.get())) {
                event.setNewAboutToBeSetTarget(null);
            }
        }

        // Charm of Eray: Neutral mobs acquire target
        if (event.getEntity() instanceof NeutralMob || event.getEntity() instanceof IronGolem) {
            if (event.getNewAboutToBeSetTarget() == null) {
                LivingEntity entity = event.getEntity();
                Player nearest = entity.level().getNearestPlayer(entity, 32.0);
                if (nearest != null && CharmItem.hasCharm(nearest, ModItems.CHARM_OF_ERAY.get())) {
                    event.setNewAboutToBeSetTarget(nearest);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onVanillaGameEvent(VanillaGameEvent event) {
        // Charm of Sound: Mutes all vibrations caused by the player
        if (event.getCause() instanceof Player player && CharmItem.hasCharm(player, ModItems.CHARM_OF_SOUND.get())) {
            event.setCanceled(true);
        }
    }

    private static void teleportToSafety(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ServerLevel level = serverPlayer.serverLevel();
        BlockPos origin = BlockPos.containing(player.getX(), 0, player.getZ());
        BlockPos safePos = null;

        // Spiral outward up to 48 blocks to find solid ground
        search:
        for (int r = 0; r <= 48; r += 2) {
            for (int dx = -r; dx <= r; dx += (r == 0 ? 1 : Math.max(1, r))) {
                for (int dz = -r; dz <= r; dz += (r == 0 ? 1 : Math.max(1, r))) {
                    int checkX = origin.getX() + dx;
                    int checkZ = origin.getZ() + dz;
                    int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, checkX, checkZ);
                    if (topY > level.getMinBuildHeight()) {
                        BlockPos checkPos = new BlockPos(checkX, topY, checkZ);
                        BlockState belowState = level.getBlockState(checkPos.below());
                        if (belowState.blocksMotion() && !belowState.is(Blocks.LAVA) && !belowState.is(Blocks.FIRE)) {
                            safePos = checkPos;
                            break search;
                        }
                    }
                }
            }
        }

        if (safePos == null) {
            safePos = level.getSharedSpawnPos().above();
        }

        double targetX = safePos.getX() + 0.5;
        double targetY = safePos.getY();
        double targetZ = safePos.getZ() + 0.5;

        // Effects at old position
        level.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1.0, player.getZ(), 40, 0.5, 0.5, 0.5, 0.2);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

        player.teleportTo(targetX, targetY, targetZ);
        player.setDeltaMovement(Vec3.ZERO);
        player.resetFallDistance();

        // Effects at new position
        level.sendParticles(ParticleTypes.PORTAL, targetX, targetY + 1.0, targetZ, 40, 0.5, 0.5, 0.5, 0.2);
        level.playSound(null, targetX, targetY, targetZ, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
