package com.moulberry.flashback.keyframe.handler;

import com.moulberry.flashback.FilePlayerSkin;
import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.keyframe.change.*;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record MinecraftKeyframeHandler(Minecraft minecraft) implements KeyframeHandler {

    private static final Set<Class<? extends KeyframeChange>> supportedChanges = Set.of(
            KeyframeChangeCameraPosition.class, KeyframeChangeFov.class, KeyframeChangeTimeOfDay.class, KeyframeChangeCameraShake.class,KeyframeChangePlayerSkin.class
    );

    @Override
    public boolean supportsKeyframeChange(Class<? extends KeyframeChange> clazz) {
        return supportedChanges.contains(clazz);
    }

    @Override
    public void applyPlayerSkin(UUID entityid, String skinIdentifier, boolean isUuidSkin) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState == null) {

            return;
        }

        if(entityid != null){



        // Reset any existing skin override for this entity first
        editorState.skinOverride.remove(entityid);
        editorState.skinOverrideFromFile.remove(entityid);


        if (isUuidSkin) {

            try {
                UUID skinUuid = UUID.fromString(skinIdentifier);
                CompletableFuture.supplyAsync(() -> Minecraft.getInstance().getMinecraftSessionService().fetchProfile(skinUuid, true))
                        .thenAccept(profileResult -> {
                            if (profileResult != null) {
                                editorState.skinOverride.put(entityid, profileResult.profile());
                            }
                        });
            } catch (IllegalArgumentException e) {
                System.err.println("Flashback: Invalid UUID for skin keyframe: " + skinIdentifier);
            }
        } else {
            // Assume skinIdentifier is a file path

            editorState.skinOverrideFromFile.put(entityid, new FilePlayerSkin(skinIdentifier));
        }}
    }

    @Override
    public void applyCameraPosition(Vector3d position, double yaw, double pitch, double roll) {
        LocalPlayer player = this.minecraft.player;
        if (player != null) {
            if (this.minecraft.cameraEntity != this.minecraft.player) {
                Minecraft.getInstance().getConnection().sendUnsignedCommand("spectate");
            }

            player.moveTo(position.x, position.y, position.z, (float) yaw, (float) pitch);

            EditorState editorState = EditorStateManager.getCurrent();
            if (editorState != null) {
                if (roll > -0.01 && roll < 0.01) {
                    editorState.replayVisuals.overrideRoll = false;
                    editorState.replayVisuals.overrideRollAmount = 0.0f;
                } else {
                    editorState.replayVisuals.overrideRoll = true;
                    editorState.replayVisuals.overrideRollAmount = (float) roll;
                }
            }

            player.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    public void applyFov(float fov) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            editorState.replayVisuals.setFov(fov);
        }
    }

    @Override
    public void applyTimeOfDay(int timeOfDay) {
        timeOfDay = timeOfDay % 24000;
        if (timeOfDay < 0) {
            timeOfDay += 24000;
        }

        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            editorState.replayVisuals.overrideTimeOfDay = timeOfDay;
        }
    }

    @Override
    public void applyCameraShake(float frequencyX, float amplitudeX, float frequencyY, float amplitudeY) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState != null) {
            editorState.replayVisuals.setCameraShake(frequencyX, amplitudeX, frequencyY, amplitudeY);
        }
    }
}
