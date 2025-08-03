package com.moulberry.flashback.packet;

import com.moulberry.flashback.Flashback;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

/**
 * Packet synchronising the item carried on the player's cursor when the inventory is open.
 * The cursor coordinates are normalised (0-1) relative to the GUI size so they can be
 * scaled to any resolution during playback.
 */
public record FlashbackInventoryCursor(int entityId, float cursorX, float cursorY, ItemStack itemStack)
        implements CustomPacketPayload {

    public static final Type<FlashbackInventoryCursor> TYPE =
            new Type<>(Flashback.createResourceLocation("inventory_cursor"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlashbackInventoryCursor> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public FlashbackInventoryCursor decode(RegistryFriendlyByteBuf buf) {
                    int entity = buf.readVarInt();
                    float x = buf.readFloat();
                    float y = buf.readFloat();
                    ItemStack stack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                    return new FlashbackInventoryCursor(entity, x, y, stack);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, FlashbackInventoryCursor value) {
                    buf.writeVarInt(value.entityId());
                    buf.writeFloat(value.cursorX());
                    buf.writeFloat(value.cursorY());
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, value.itemStack());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
