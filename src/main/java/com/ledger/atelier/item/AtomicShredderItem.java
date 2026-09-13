package com.ledger.atelier.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class AtomicShredderItem extends Item {
    public AtomicShredderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40; // 2 seconds (20 ticks/sec * 2)
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        int elapsedTicks = 40 - remainingUseDuration;

        // Apply brief glowing effect so user's silhouette blooms with light
        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false, false));

        if (level.isClientSide()) {
            // Ascending spiral of electric spark and end rod particles rotating around bounding box
            double angle = elapsedTicks * 0.5;
            double radius = 0.75;
            double height = (elapsedTicks / 40.0) * livingEntity.getBbHeight();
            double px = livingEntity.getX() + Math.cos(angle) * radius;
            double py = livingEntity.getY() + height;
            double pz = livingEntity.getZ() + Math.sin(angle) * radius;

            level.addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0.0, 0.02, 0.0);
            level.addParticle(ParticleTypes.END_ROD, px, py, pz, 0.0, 0.01, 0.0);
        }

        // Ascending pitch spark sound every 8 ticks
        if (elapsedTicks % 8 == 0) {
            float pitch = 0.8F + (elapsedTicks / 40.0F) * 0.8F;
            level.playSound(
                    livingEntity instanceof Player p ? p : null,
                    livingEntity.getX(),
                    livingEntity.getY(),
                    livingEntity.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS,
                    0.8F,
                    pitch
            );
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof ServerPlayer serverPlayer) {
            BlockPos respawnPos = serverPlayer.getRespawnPosition();
            DimensionTransition transition = serverPlayer.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING);
            boolean hasValidSpawn = respawnPos != null && !transition.missingRespawnBlock();

            if (hasValidSpawn) {
                ServerLevel targetLevel = transition.newLevel();
                Vec3 targetPos = transition.pos();
                float yaw = transition.yRot();
                float pitch = transition.xRot();

                ServerLevel originLevel = serverPlayer.serverLevel();
                double oldX = serverPlayer.getX();
                double oldY = serverPlayer.getY();
                double oldZ = serverPlayer.getZ();

                // Departure FX: burst of FLASH and 10x CAMPFIRE_COSY_SMOKE
                originLevel.sendParticles(ParticleTypes.FLASH, oldX, oldY + 1.0, oldZ, 1, 0.0, 0.0, 0.0, 0.0);
                originLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, oldX, oldY + 0.5, oldZ, 10, 0.3, 0.5, 0.3, 0.05);

                // Departure sounds: BEACON_DEACTIVATE and PLAYER_TELEPORT
                originLevel.playSound(null, oldX, oldY, oldZ, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.2F);
                originLevel.playSound(null, oldX, oldY, oldZ, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

                // Safe cross-dimension teleportation
                serverPlayer.teleportTo(targetLevel, targetPos.x, targetPos.y, targetPos.z, yaw, pitch);
                serverPlayer.resetFallDistance();

                // Arrival FX
                targetLevel.sendParticles(ParticleTypes.FLASH, targetPos.x, targetPos.y + 1.0, targetPos.z, 1, 0.0, 0.0, 0.0, 0.0);
                targetLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, targetPos.x, targetPos.y + 0.5, targetPos.z, 10, 0.3, 0.5, 0.3, 0.05);
                targetLevel.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

                // Consume 1 item (respects creative mode)
                stack.consume(1, serverPlayer);

                // Fake Death Message broadcast server-wide (respecting showDeathMessages gamerule)
                if (serverPlayer.serverLevel().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                    Component deathMessage = Component.translatable("message.ledgers_atelier.atomic_shredder.fake_death", serverPlayer.getDisplayName());
                    serverPlayer.server.getPlayerList().broadcastSystemMessage(deathMessage, false);
                }
            } else {
                // Failure: No neural tether detected
                level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.displayClientMessage(Component.translatable("message.ledgers_atelier.atomic_shredder.failure"), true);
            }
        }
        return stack;
    }
}
