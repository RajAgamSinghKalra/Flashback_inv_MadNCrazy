package com.moulberry.flashback.visuals;

import com.moulberry.flashback.Flashback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Renders the recorded player's inventory as an overlay during replay playback.
 */
public class InventoryOverlay {
    private static int openEntityId = -1;
    @Nullable
    private static InventoryScreen screen = null;
    private static ItemStack cursorStack = ItemStack.EMPTY;
    private static float cursorX = 0.0f;
    private static float cursorY = 0.0f;

    public static void setOpen(int entityId, boolean open) {
        Minecraft mc = Minecraft.getInstance();
        if (open) {
            Entity e = mc.level == null ? null : mc.level.getEntity(entityId);
            if (e instanceof Player p) {
                openEntityId = entityId;
                screen = new InventoryScreen(p);
                screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
                cursorStack = ItemStack.EMPTY;
            }
        } else if (openEntityId == entityId) {
            openEntityId = -1;
            screen = null;
            cursorStack = ItemStack.EMPTY;
        }
    }

    public static void setCursor(int entityId, float x, float y, ItemStack stack) {
        if (entityId != openEntityId) {
            return;
        }
        cursorX = x;
        cursorY = y;
        cursorStack = stack.copy();
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

        if (!cursorStack.isEmpty()) {
            int x = (int)(cursorX * mc.getWindow().getGuiScaledWidth());
            int y = (int)(cursorY * mc.getWindow().getGuiScaledHeight());
            guiGraphics.renderItem(cursorStack, x - 8, y - 8);
            guiGraphics.renderItemDecorations(mc.font, cursorStack, x - 8, y - 8);
        }
    }
}
