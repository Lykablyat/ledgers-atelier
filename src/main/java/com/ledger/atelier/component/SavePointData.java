package com.ledger.atelier.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.UUID;

public record SavePointData(
        UUID ownerUUID,
        String ownerName,
        ResourceKey<Level> dimension,
        double x,
        double y,
        double z,
        float yaw,
        float pitch
) {
    public static final Codec<SavePointData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("owner_uuid").forGetter(SavePointData::ownerUUID),
                    Codec.STRING.fieldOf("owner_name").forGetter(SavePointData::ownerName),
                    Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(SavePointData::dimension),
                    Codec.DOUBLE.fieldOf("x").forGetter(SavePointData::x),
                    Codec.DOUBLE.fieldOf("y").forGetter(SavePointData::y),
                    Codec.DOUBLE.fieldOf("z").forGetter(SavePointData::z),
                    Codec.FLOAT.fieldOf("yaw").forGetter(SavePointData::yaw),
                    Codec.FLOAT.fieldOf("pitch").forGetter(SavePointData::pitch)
            ).apply(instance, SavePointData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SavePointData> STREAM_CODEC = StreamCodec.of(
            (buf, val) -> {
                UUIDUtil.STREAM_CODEC.encode(buf, val.ownerUUID());
                ByteBufCodecs.STRING_UTF8.encode(buf, val.ownerName());
                ResourceKey.streamCodec(Registries.DIMENSION).encode(buf, val.dimension());
                buf.writeDouble(val.x());
                buf.writeDouble(val.y());
                buf.writeDouble(val.z());
                buf.writeFloat(val.yaw());
                buf.writeFloat(val.pitch());
            },
            buf -> new SavePointData(
                    UUIDUtil.STREAM_CODEC.decode(buf),
                    ByteBufCodecs.STRING_UTF8.decode(buf),
                    ResourceKey.streamCodec(Registries.DIMENSION).decode(buf),
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readFloat(),
                    buf.readFloat()
            )
    );

    public SavePointData withLocation(ResourceKey<Level> newDimension, double newX, double newY, double newZ, float newYaw, float newPitch) {
        return new SavePointData(this.ownerUUID, this.ownerName, newDimension, newX, newY, newZ, newYaw, newPitch);
    }
}
