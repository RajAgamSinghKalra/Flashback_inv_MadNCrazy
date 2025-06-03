package com.moulberry.flashback.keyframe.handler;

import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.keyframe.KeyframeType;
import com.moulberry.flashback.keyframe.change.KeyframeChange;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public interface KeyframeHandler {
    boolean supportsKeyframeChange(Class<? extends KeyframeChange> clazz);

    default boolean alwaysApplyLastKeyframe() {
        return false;
    }

    default void applyCameraPosition(Vector3d position, double yaw, double pitch, double roll) {
    }

    default void applyPlayerSkin(UUID entityid, String skinIdentifier, boolean isUuidSkin) {

    }

    default void applyFov(float fov) {
    }

    default void applyTickrate(float tickrate) {
    }

    default void applyFreeze(boolean frozen, int frozenDelay) {
    }

    default void applyTimeOfDay(int timeOfDay) {
    }

    default void applyCameraShake(float frequencyX, float amplitudeX, float frequencyY, float amplitudeY) {
    }


}
