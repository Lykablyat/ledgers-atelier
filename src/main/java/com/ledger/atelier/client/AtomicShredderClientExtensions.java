package com.ledger.atelier.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class AtomicShredderClientExtensions implements IClientItemExtensions {
    @Override
    public boolean applyForgeHandTransform(
            PoseStack poseStack,
            LocalPlayer player,
            HumanoidArm arm,
            ItemStack itemInHand,
            float partialTick,
            float equipProgress,
            float swingProgress
    ) {
        InteractionHand hand = player.getUsedItemHand();
        HumanoidArm usingArm = (hand == InteractionHand.MAIN_HAND)
                ? player.getMainArm()
                : player.getMainArm().getOpposite();

        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && usingArm == arm) {
            int k = arm == HumanoidArm.RIGHT ? 1 : -1;

            float remainingTicks = (float) player.getUseItemRemainingTicks() - partialTick + 1.0F;
            float totalDuration = (float) itemInHand.getUseDuration(player);
            float progress = 1.0F - (remainingTicks / totalDuration);
            progress = Mth.clamp(progress, 0.0F, 1.0F);

            // Smooth ease-out curve for raising the syringe
            float raiseProgress = 1.0F - (float) Math.pow(1.0F - progress, 3.0);

            // 1. Base first-person arm position (matches vanilla ItemInHandRenderer.applyItemArmTransform)
            poseStack.translate((float) k * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);

            // 2. Channeling injection posture:
            // Move syringe towards the center of view and raise it
            poseStack.translate((float) k * -0.22F * raiseProgress, 0.18F * raiseProgress, 0.12F * raiseProgress);

            // Angle syringe so the barrel and needle face prominently in front of the player
            poseStack.mulPose(Axis.XP.rotationDegrees(-15.0F * raiseProgress));
            poseStack.mulPose(Axis.YP.rotationDegrees((float) k * 30.0F * raiseProgress));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) k * -12.0F * raiseProgress));

            // 3. Energetic vibration as atomic energy charges up
            if (progress > 0.05F) {
                float jitterIntensity = progress * 0.003F;
                float jitterX = (float) Math.sin((player.tickCount + partialTick) * 3.0F) * jitterIntensity;
                float jitterY = (float) Math.cos((player.tickCount + partialTick) * 2.5F) * jitterIntensity;
                poseStack.translate(jitterX, jitterY, 0.0F);
            }

            return true;
        }

        return false;
    }
}
