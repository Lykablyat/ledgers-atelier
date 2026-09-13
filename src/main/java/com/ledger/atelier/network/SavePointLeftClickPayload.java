package com.ledger.atelier.network;

import com.ledger.atelier.LedgersAtelier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SavePointLeftClickPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SavePointLeftClickPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LedgersAtelier.MODID, "save_point_left_click"));

    public static final StreamCodec<ByteBuf, SavePointLeftClickPayload> STREAM_CODEC =
            StreamCodec.unit(new SavePointLeftClickPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
