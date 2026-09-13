package com.ledger.atelier.item;

import com.ledger.atelier.component.SavePointData;
import com.ledger.atelier.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SavePointRemoteItem extends Item {
    public SavePointRemoteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            SavePointData data = stack.get(ModDataComponents.SAVE_POINT.get());

            if (data == null) {
                // Remote is unbound
                level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                        SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.displayClientMessage(
                        Component.translatable("message.ledgers_atelier.save_point_remote.unbound"), true);
                return InteractionResultHolder.fail(stack);
            }

            MinecraftServer server = serverPlayer.getServer();
            if (server == null) {
                return InteractionResultHolder.fail(stack);
            }

            // Look up the registered target player by UUID
            ServerPlayer targetPlayer = server.getPlayerList().getPlayer(data.ownerUUID());

            if (targetPlayer == null) {
                // Target player is offline
                level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                        SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.displayClientMessage(
                        Component.translatable("message.ledgers_atelier.save_point_remote.target_offline", data.ownerName()), true);
                return InteractionResultHolder.fail(stack);
            }

            // Resolve target dimension level
            ServerLevel targetLevel = server.getLevel(data.dimension());
            if (targetLevel == null) {
                level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                        SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.displayClientMessage(
                        Component.translatable("message.ledgers_atelier.save_point_remote.dimension_invalid"), true);
                return InteractionResultHolder.fail(stack);
            }

            // Teleport the registered player
            ServerLevel originLevel = targetPlayer.serverLevel();
            double oldX = targetPlayer.getX();
            double oldY = targetPlayer.getY();
            double oldZ = targetPlayer.getZ();

            // Origin FX
            originLevel.sendParticles(ParticleTypes.PORTAL, oldX, oldY + 1.0, oldZ, 40, 0.4, 0.6, 0.4, 0.1);
            originLevel.playSound(null, oldX, oldY, oldZ, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Execute safe teleport
            targetPlayer.teleportTo(targetLevel, data.x(), data.y(), data.z(), data.yaw(), data.pitch());
            targetPlayer.resetFallDistance();

            // Destination FX
            targetLevel.sendParticles(ParticleTypes.PORTAL, data.x(), data.y() + 1.0, data.z(), 40, 0.4, 0.6, 0.4, 0.1);
            targetLevel.playSound(null, data.x(), data.y(), data.z(), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Audio & notification feedback
            if (serverPlayer.getUUID().equals(targetPlayer.getUUID())) {
                serverPlayer.displayClientMessage(
                        Component.translatable("message.ledgers_atelier.save_point_remote.teleported_self"), true);
            } else {
                serverPlayer.displayClientMessage(
                        Component.translatable("message.ledgers_atelier.save_point_remote.recalled_target", targetPlayer.getDisplayName()), true);
                targetPlayer.displayClientMessage(
                        Component.translatable("message.ledgers_atelier.save_point_remote.recalled_by_other", serverPlayer.getDisplayName()), true);
            }

            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        return InteractionResultHolder.pass(stack);
    }

    /**
     * Executes the left-click save functionality on the server.
     */
    public void executeSave(ServerPlayer caller, ItemStack stack) {
        SavePointData existing = stack.get(ModDataComponents.SAVE_POINT.get());
        ServerLevel level = caller.serverLevel();

        double x = caller.getX();
        double y = caller.getY();
        double z = caller.getZ();
        float yaw = caller.getYRot();
        float pitch = caller.getXRot();

        if (existing == null) {
            // First time binding: bind permanently to the caller
            SavePointData newData = new SavePointData(
                    caller.getUUID(),
                    caller.getScoreboardName(),
                    level.dimension(),
                    x, y, z, yaw, pitch
            );
            stack.set(ModDataComponents.SAVE_POINT.get(), newData);

            // FX
            level.sendParticles(ParticleTypes.PORTAL, x, y + 1.0, z, 30, 0.3, 0.5, 0.3, 0.05);
            level.playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.5F);

            caller.displayClientMessage(
                    Component.translatable(
                            "message.ledgers_atelier.save_point_remote.bound",
                            caller.getScoreboardName(),
                            (int) x, (int) y, (int) z
                    ),
                    true
            );
        } else {
            // Already bound: Option A (Beacon Mode) - updates target coordinates to caller's position while preserving owner
            SavePointData updated = existing.withLocation(level.dimension(), x, y, z, yaw, pitch);
            stack.set(ModDataComponents.SAVE_POINT.get(), updated);

            // FX
            level.sendParticles(ParticleTypes.PORTAL, x, y + 1.0, z, 20, 0.3, 0.5, 0.3, 0.05);
            level.playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.8F);

            if (caller.getUUID().equals(existing.ownerUUID())) {
                caller.displayClientMessage(
                        Component.translatable(
                                "message.ledgers_atelier.save_point_remote.updated_self",
                                (int) x, (int) y, (int) z
                        ),
                        true
                );
            } else {
                caller.displayClientMessage(
                        Component.translatable(
                                "message.ledgers_atelier.save_point_remote.updated_for_target",
                                existing.ownerName()
                        ),
                        true
                );
            }
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (player instanceof ServerPlayer serverPlayer) {
            executeSave(serverPlayer, stack);
        }
        return true; // Cancel attack damage
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Enchantment glint when bound
        return stack.has(ModDataComponents.SAVE_POINT.get()) || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        SavePointData data = stack.get(ModDataComponents.SAVE_POINT.get());
        if (data != null) {
            tooltipComponents.add(Component.translatable("tooltip.ledgers_atelier.save_point_remote.bound_to", data.ownerName())
                    .withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.ledgers_atelier.save_point_remote.dimension", data.dimension().location().toString())
                    .withStyle(ChatFormatting.DARK_AQUA));
            tooltipComponents.add(Component.translatable(
                    "tooltip.ledgers_atelier.save_point_remote.coords",
                    String.format("%.1f", data.x()),
                    String.format("%.1f", data.y()),
                    String.format("%.1f", data.z())
            ).withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.ledgers_atelier.save_point_remote.unbound")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            tooltipComponents.add(Component.translatable("tooltip.ledgers_atelier.save_point_remote.hint_left")
                    .withStyle(ChatFormatting.DARK_GRAY));
            tooltipComponents.add(Component.translatable("tooltip.ledgers_atelier.save_point_remote.hint_right")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
