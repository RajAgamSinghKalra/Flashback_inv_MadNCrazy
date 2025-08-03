package com.moulberry.flashback.packet;

import com.moulberry.flashback.Flashback;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Packet sent during replay playback to toggle the inventory overlay for a player.
 */
public record FlashbackInventoryOpen(int entityId, boolean open) implements CustomPacketPayload {
    public static final Type<FlashbackInventoryOpen> TYPE = new Type<>(Flashback.createResourceLocation("inventory_open"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlashbackInventoryOpen> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FlashbackInventoryOpen decode(RegistryFriendlyByteBuf buf) {
            int entity = buf.readVarInt();
            boolean open = buf.readBoolean();
            return new FlashbackInventoryOpen(entity, open);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FlashbackInventoryOpen value) {
            buf.writeVarInt(value.entityId());
            buf.writeBoolean(value.open());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
