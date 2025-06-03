package com.moulberry.flashback.keyframe.change;

import com.moulberry.flashback.keyframe.handler.KeyframeHandler;
import java.util.UUID;

public record KeyframeChangePlayerSkin(UUID entityId, String skinIdentifier, boolean isUuidSkin) implements KeyframeChange {

    @Override
    public void apply(KeyframeHandler keyframeHandler) {
        keyframeHandler.applyPlayerSkin(this.entityId, this.skinIdentifier, this.isUuidSkin);
    }




    @Override
    public KeyframeChange interpolate(KeyframeChange to, double amount) {

        return this;
    }
}