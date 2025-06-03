package com.moulberry.flashback.keyframe.types;

import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.keyframe.KeyframeType;
import com.moulberry.flashback.keyframe.change.KeyframeChange;
import com.moulberry.flashback.keyframe.change.KeyframeChangePlayerSkin; // Correct import
import com.moulberry.flashback.keyframe.handler.KeyframeHandler;
import com.moulberry.flashback.keyframe.impl.SkinKeyframe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SkinKeyframeType implements KeyframeType<SkinKeyframe> {

    public static SkinKeyframeType INSTANCE = new SkinKeyframeType();

    private SkinKeyframeType() {
    }

    @Override
    public Class<? extends KeyframeChange> keyframeChangeType() {
        return KeyframeChangePlayerSkin.class; // Now returns your custom skin change
    }

    @Override
    public @Nullable String icon() {
        return "\ue84e";
    }

    @Override
    public String name() {
        return "Skin";
    }

    @Override
    public String id() {
        return "SKIN";
    }

    @Override
    public @Nullable SkinKeyframe createDirect() {

        return new SkinKeyframe(Flashback.lastselectede, "", false);
    }

    @Override
    public KeyframeCreatePopup<SkinKeyframe> createPopup() {
        return null;
    }
}