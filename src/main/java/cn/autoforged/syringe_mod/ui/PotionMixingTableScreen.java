package cn.autoforged.syringe_mod.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class PotionMixingTableScreen extends AbstractContainerScreen<PotionMixingTableMenu> {
    private static final String GUI_NAMESPACE = "autoforge_bricks";

    private static ResourceLocation guiTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath(
                GUI_NAMESPACE, "textures/gui/" + name + ".png");
    }

    private static final ResourceLocation SLOT_TEXTURE = guiTexture("slot_default");
    private static final ResourceLocation PLAYER_SLOTS_TEXTURE = guiTexture("player_slots_9x4");
    private static final ResourceLocation FILL_TEXTURE = guiTexture("fill_white");
    private static final ResourceLocation SPLIT_TEXTURE = guiTexture("split_bar");
    private static final ResourceLocation BORDER_TOP_LEFT = guiTexture("border_corner_tl");
    private static final ResourceLocation BORDER_TOP_RIGHT = guiTexture("border_corner_tr");
    private static final ResourceLocation BORDER_BOTTOM_LEFT = guiTexture("border_corner_bl");
    private static final ResourceLocation BORDER_BOTTOM_RIGHT = guiTexture("border_corner_br");
    private static final ResourceLocation BORDER_TOP = guiTexture("border_edge_top");
    private static final ResourceLocation BORDER_BOTTOM = guiTexture("border_edge_bottom");
    private static final ResourceLocation BORDER_LEFT = guiTexture("border_edge_left");
    private static final ResourceLocation BORDER_RIGHT = guiTexture("border_edge_right");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 190;
    private static final int WORK_TOP = 20;
    private static final int WORK_BOTTOM = 91;
    private static final int SPLIT_Y = 94;
    private static final int PLAYER_INV_TOP = 109;

    private static final int ENAMEL_LIGHT = 0xFFE2E3DF;
    private static final int ENAMEL_MID = 0xFFBEC2C0;
    private static final int ENAMEL_SHADOW = 0xFF858D90;
    private static final int PANEL = 0xFF30373B;
    private static final int PANEL_DARK = 0xFF171C20;
    private static final int ROTOR = 0xFF4E595E;
    private static final int CYAN = 0xFF21B7C1;
    private static final int CYAN_LIGHT = 0xFF71D5D4;
    private static final int CYAN_DARK = 0xFF126B73;
    private static final int PURPLE = 0xFF8241A7;
    private static final int BLOCKED = 0xFFB74742;

    public PotionMixingTableScreen(
            PotionMixingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = WIDTH;
        this.imageHeight = HEIGHT;
        this.titleLabelX = 9;
        this.titleLabelY = 7;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 98;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        drawFrame(graphics, x, y);
        drawWorkArea(graphics, x, y);

        graphics.blit(
                SPLIT_TEXTURE,
                x + 5, y + SPLIT_Y,
                0, 0,
                WIDTH - 10, 14,
                1, 14);

        int playerInventoryLeft = x + (WIDTH - 162) / 2;
        graphics.blit(
                PLAYER_SLOTS_TEXTURE,
                playerInventoryLeft - 1, y + PLAYER_INV_TOP - 1,
                0, 0,
                162, 76,
                162, 76);
    }

    private void drawFrame(GuiGraphics graphics, int x, int y) {
        graphics.blit(FILL_TEXTURE, x + 5, y + 5, 0, 0, WIDTH - 10, HEIGHT - 10, 1, 1);

        graphics.blit(BORDER_TOP_LEFT, x, y, 0, 0, 5, 5, 5, 5);
        graphics.blit(BORDER_TOP_RIGHT, x + WIDTH - 5, y, 0, 0, 5, 5, 5, 5);
        graphics.blit(BORDER_BOTTOM_LEFT, x, y + HEIGHT - 5, 0, 0, 5, 5, 5, 5);
        graphics.blit(BORDER_BOTTOM_RIGHT,
                x + WIDTH - 5, y + HEIGHT - 5, 0, 0, 5, 5, 5, 5);

        graphics.blit(BORDER_TOP, x + 5, y, 0, 0, WIDTH - 10, 5, 1, 5);
        graphics.blit(BORDER_BOTTOM,
                x + 5, y + HEIGHT - 5, 0, 0, WIDTH - 10, 5, 1, 5);
        graphics.blit(BORDER_LEFT, x, y + 5, 0, 0, 5, HEIGHT - 10, 5, 1);
        graphics.blit(BORDER_RIGHT,
                x + WIDTH - 5, y + 5, 0, 0, 5, HEIGHT - 10, 5, 1);
    }

    private void drawWorkArea(GuiGraphics graphics, int x, int y) {
        panel(graphics, x + 9, y + WORK_TOP, x + WIDTH - 9, y + WORK_BOTTOM);

        // Two-input reagent bay.
        graphics.fill(x + 13, y + 25, x + 55, y + 85, PANEL);
        graphics.fill(x + 16, y + 28, x + 52, y + 82, PANEL_DARK);
        graphics.fill(x + 19, y + 30, x + 22, y + 52, PURPLE);
        graphics.fill(x + 19, y + 58, x + 22, y + 80, CYAN);

        for (int i = 0; i < 2; i++) {
            Slot slot = this.menu.slots.get(i);
            graphics.blit(
                    SLOT_TEXTURE,
                    x + slot.x - 1, y + slot.y - 1,
                    0, 0,
                    18, 18,
                    18, 18);
        }

        // Pixel plus sign between the two input slots.
        graphics.fill(x + 31, y + 53, x + 35, y + 61, ENAMEL_MID);
        graphics.fill(x + 29, y + 55, x + 37, y + 59, ENAMEL_MID);

        // Input-to-centrifuge flow arrow.
        int flowColor = this.menu.isOutputBlocked() ? BLOCKED : CYAN_DARK;
        graphics.fill(x + 55, y + 53, x + 65, y + 57, flowColor);
        graphics.fill(x + 62, y + 50, x + 65, y + 60, flowColor);

        drawCentrifuge(graphics, x, y);

        // Centrifuge-to-output flow arrow.
        graphics.fill(x + 128, y + 53, x + 138, y + 57, flowColor);
        graphics.fill(x + 135, y + 50, x + 138, y + 60, flowColor);

        // Isolated output bay and status lamp.
        graphics.fill(x + 137, y + 35, x + 163, y + 77, PANEL);
        graphics.fill(x + 140, y + 39, x + 160, y + 68, PANEL_DARK);
        Slot output = this.menu.slots.get(2);
        graphics.blit(
                SLOT_TEXTURE,
                x + output.x - 1, y + output.y - 1,
                0, 0,
                18, 18,
                18, 18);
        graphics.fill(x + 142, y + 71, x + 158, y + 75, PANEL_DARK);
        graphics.fill(
                x + 144, y + 72, x + 156, y + 74,
                this.menu.isOutputBlocked() ? BLOCKED : CYAN);
    }

    private void drawCentrifuge(GuiGraphics graphics, int x, int y) {
        int progress = this.menu.getProgress();
        int maxProgress = this.menu.getMaxProgress();
        boolean running = progress > 0 && maxProgress > 0;
        boolean blocked = this.menu.isOutputBlocked();

        graphics.fill(x + 64, y + 24, x + 128, y + 77, ENAMEL_SHADOW);
        graphics.fill(x + 67, y + 27, x + 125, y + 74, PANEL);
        graphics.fill(x + 71, y + 31, x + 121, y + 70, PANEL_DARK);
        graphics.fill(x + 74, y + 34, x + 118, y + 67, 0xFF20272B);

        int centerX = x + 96;
        int centerY = y + 50;
        int phase = running ? (progress * 8 / maxProgress) & 3 : 0;
        int rotorColor = running ? CYAN_DARK : ROTOR;

        if ((phase & 1) == 0) {
            graphics.fill(centerX - 17, centerY - 2, centerX + 17, centerY + 2, rotorColor);
            graphics.fill(centerX - 2, centerY - 14, centerX + 2, centerY + 14, rotorColor);
        } else {
            graphics.fill(centerX - 13, centerY - 10, centerX - 7, centerY - 6, rotorColor);
            graphics.fill(centerX + 7, centerY - 10, centerX + 13, centerY - 6, rotorColor);
            graphics.fill(centerX - 13, centerY + 6, centerX - 7, centerY + 10, rotorColor);
            graphics.fill(centerX + 7, centerY + 6, centerX + 13, centerY + 10, rotorColor);
        }

        graphics.fill(centerX - 5, centerY - 5, centerX + 5, centerY + 5, ROTOR);
        graphics.fill(centerX - 2, centerY - 2, centerX + 2, centerY + 2, PANEL_DARK);

        int sampleCyan = running ? CYAN_LIGHT : CYAN;
        sample(graphics, centerX - 2, centerY - 13, sampleCyan);
        sample(graphics, centerX + 11, centerY - 2, CYAN);
        sample(graphics, centerX - 2, centerY + 9, PURPLE);
        sample(graphics, centerX - 15, centerY - 2, PURPLE);

        // Segmented progress rail under the chamber.
        graphics.fill(x + 64, y + 80, x + 128, y + 88, PANEL);
        graphics.fill(x + 67, y + 82, x + 125, y + 86, PANEL_DARK);
        int filled = maxProgress > 0 ? 56 * progress / maxProgress : 0;
        if (filled > 0) {
            graphics.fill(
                    x + 68, y + 83, x + 68 + filled, y + 85,
                    blocked ? BLOCKED : CYAN);
        }
        for (int divider = 1; divider < 4; divider++) {
            int dividerX = x + 68 + divider * 14;
            graphics.fill(dividerX, y + 82, dividerX + 1, y + 86, PANEL);
        }
    }

    private static void sample(GuiGraphics graphics, int centerX, int centerY, int color) {
        graphics.fill(centerX - 3, centerY - 3, centerX + 3, centerY + 3, ROTOR);
        graphics.fill(centerX - 2, centerY - 2, centerX + 2, centerY + 2, color);
    }

    private static void panel(
            GuiGraphics graphics, int left, int top, int right, int bottom) {
        graphics.fill(left, top, right, bottom, ENAMEL_SHADOW);
        graphics.fill(left + 2, top + 2, right - 2, bottom - 2, ENAMEL_LIGHT);
        graphics.fill(left + 4, top + 4, right - 4, bottom - 4, ENAMEL_MID);
    }
}
