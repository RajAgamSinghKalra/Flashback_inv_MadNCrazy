package com.moulberry.flashback.visuals;

import com.moulberry.flashback.Flashback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Renders the recorded player's inventory as an overlay during replay playback.
 */
public class InventoryOverlay {
    private static int openEntityId = -1;
    @Nullable
    private static InventoryScreen screen = null;

    public static void setOpen(int entityId, boolean open) {
        Minecraft mc = Minecraft.getInstance();
        if (open) {
            Entity e = mc.level == null ? null : mc.level.getEntity(entityId);
            if (e instanceof Player p) {
                openEntityId = entityId;
                screen = new InventoryScreen(p);
                screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
            }
        } else if (openEntityId == entityId) {
            openEntityId = -1;
            screen = null;
        }
    }

    public static void render(GuiGraphics guiGraphics) {
        if (!Flashback.isInReplay()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Entity camera = mc.getCameraEntity();
        if (!(camera instanceof Player player)) {
            return;
        }
        if (player.getId() != openEntityId || screen == null) {
            return;
        }
        screen.render(guiGraphics, -1, -1, 0);
    }
}
