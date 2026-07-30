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

    private static final int WIDTH = 198;
    private static final int HEIGHT = 220;
    private static final int WORK_TOP = 23;
    private static final int WORK_BOTTOM = 116;
    private static final int SPLIT_Y = 120;
    private static final int PLAYER_INV_TOP = 141;

    private static final int ENAMEL_LIGHT = 0xFFE4E5E1;
    private static final int ENAMEL_MID = 0xFFBEC3C2;
    private static final int ENAMEL_SHADOW = 0xFF818A8E;
    private static final int PANEL = 0xFF343C41;
    private static final int PANEL_DARK = 0xFF171D21;
    private static final int PANEL_INSET = 0xFF242B2F;
    private static final int ROTOR = 0xFF515D62;
    private static final int CYAN = 0xFF20BBC5;
    private static final int CYAN_LIGHT = 0xFF77D8D7;
    private static final int CYAN_DARK = 0xFF126C74;
    private static final int PURPLE = 0xFF8242A7;
    private static final int BLOCKED = 0xFFB74742;

    public PotionMixingTableScreen(
            PotionMixingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = WIDTH;
        this.imageHeight = HEIGHT;
        this.titleLabelX = 11;
        this.titleLabelY = 8;
        this.inventoryLabelX = 18;
        this.inventoryLabelY = 129;
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

        // Recessed header strip from the approved concept, kept clear for the title.
        graphics.fill(x + 10, y + 18, x + 82, y + 21, ENAMEL_SHADOW);
        graphics.fill(x + 12, y + 18, x + 80, y + 19, ENAMEL_LIGHT);
    }

    private void drawWorkArea(GuiGraphics graphics, int x, int y) {
        panel(graphics, x + 8, y + WORK_TOP, x + WIDTH - 8, y + WORK_BOTTOM);

        drawInputBay(graphics, x, y, 0, PURPLE);
        drawInputBay(graphics, x, y, 1, CYAN);

        int flowColor = this.menu.isOutputBlocked() ? BLOCKED : CYAN_DARK;
        // Twin reagent channels into the centrifuge.
        channel(graphics, x + 43, y + 47, x + 51, y + 51, flowColor);
        channel(graphics, x + 43, y + 82, x + 51, y + 86, flowColor);

        drawCentrifuge(graphics, x, y);

        // Output channel and isolated product bay.
        channel(graphics, x + 142, y + 63, x + 151, y + 67, flowColor);
        graphics.fill(x + 148, y + 60, x + 152, y + 70, flowColor);

        bevel(graphics, x + 148, y + 35, x + 190, y + 99);
        graphics.fill(x + 152, y + 39, x + 186, y + 95, PANEL);
        graphics.fill(x + 155, y + 42, x + 183, y + 88, PANEL_DARK);
        Slot output = this.menu.slots.get(2);
        graphics.blit(
                SLOT_TEXTURE,
                x + output.x - 1, y + output.y - 1,
                0, 0,
                18, 18,
                18, 18);
        graphics.fill(x + 155, y + 90, x + 183, y + 94, PANEL_DARK);
        graphics.fill(
                x + 158, y + 91, x + 180, y + 93,
                this.menu.isOutputBlocked() ? BLOCKED : CYAN);

        drawProgressRail(graphics, x, y);
    }

    private void drawInputBay(
            GuiGraphics graphics, int x, int y, int slotIndex, int medicineColor) {
        Slot slot = this.menu.slots.get(slotIndex);
        int bayTop = slotIndex == 0 ? 30 : 65;

        bevel(graphics, x + 8, y + bayTop, x + 47, y + bayTop + 32);
        graphics.fill(x + 12, y + bayTop + 4, x + 43, y + bayTop + 28, PANEL);
        graphics.fill(x + 15, y + bayTop + 7, x + 40, y + bayTop + 25, PANEL_DARK);
        graphics.fill(x + 14, y + bayTop + 8, x + 17, y + bayTop + 24, medicineColor);

        graphics.blit(
                SLOT_TEXTURE,
                x + slot.x - 1, y + slot.y - 1,
                0, 0,
                18, 18,
                18, 18);

        // Small connector block prevents the inputs from reading as loose slots.
        graphics.fill(x + 43, y + bayTop + 12, x + 52, y + bayTop + 21, PANEL);
        graphics.fill(x + 45, y + bayTop + 15, x + 52, y + bayTop + 18, CYAN_DARK);
    }

    private void drawCentrifuge(GuiGraphics graphics, int x, int y) {
        int progress = this.menu.getProgress();
        int maxProgress = this.menu.getMaxProgress();
        boolean running = progress > 0 && maxProgress > 0;

        bevel(graphics, x + 48, y + 25, x + 144, y + 102);
        graphics.fill(x + 53, y + 30, x + 139, y + 97, PANEL);
        graphics.fill(x + 58, y + 35, x + 134, y + 92, PANEL_DARK);
        graphics.fill(x + 63, y + 40, x + 129, y + 87, PANEL_INSET);

        int centerX = x + 96;
        int centerY = y + 64;
        int phase = running ? (progress * 8 / maxProgress) & 3 : 0;
        int rotorColor = running ? CYAN_DARK : ROTOR;

        if ((phase & 1) == 0) {
            graphics.fill(centerX - 23, centerY - 3, centerX + 23, centerY + 3, rotorColor);
            graphics.fill(centerX - 3, centerY - 20, centerX + 3, centerY + 20, rotorColor);
        } else {
            graphics.fill(centerX - 19, centerY - 16, centerX - 9, centerY - 10, rotorColor);
            graphics.fill(centerX + 9, centerY - 16, centerX + 19, centerY - 10, rotorColor);
            graphics.fill(centerX - 19, centerY + 10, centerX - 9, centerY + 16, rotorColor);
            graphics.fill(centerX + 9, centerY + 10, centerX + 19, centerY + 16, rotorColor);
        }

        graphics.fill(centerX - 8, centerY - 8, centerX + 8, centerY + 8, ROTOR);
        graphics.fill(centerX - 4, centerY - 4, centerX + 4, centerY + 4, ENAMEL_MID);

        int brightCyan = running ? CYAN_LIGHT : CYAN;
        sample(graphics, centerX, centerY - 22, brightCyan);
        sample(graphics, centerX + 25, centerY, CYAN);
        sample(graphics, centerX, centerY + 22, PURPLE);
        sample(graphics, centerX - 25, centerY, PURPLE);

        // Lid status bar above the chamber.
        graphics.fill(x + 71, y + 22, x + 121, y + 29, PANEL);
        for (int index = 0; index < 5; index++) {
            int lampX = x + 75 + index * 9;
            graphics.fill(lampX, y + 24, lampX + 6, y + 27, running ? CYAN : CYAN_DARK);
        }
    }

    private void drawProgressRail(GuiGraphics graphics, int x, int y) {
        int progress = this.menu.getProgress();
        int maxProgress = this.menu.getMaxProgress();
        boolean blocked = this.menu.isOutputBlocked();

        // Fast-process control motif at the left and ventilation motif at right.
        bevel(graphics, x + 10, y + 103, x + 40, y + 115);
        graphics.fill(x + 16, y + 106, x + 20, y + 112, CYAN);
        graphics.fill(x + 21, y + 106, x + 25, y + 112, CYAN);
        graphics.fill(x + 26, y + 106, x + 30, y + 112, CYAN);

        graphics.fill(x + 43, y + 103, x + 157, y + 115, ENAMEL_SHADOW);
        graphics.fill(x + 46, y + 106, x + 154, y + 112, PANEL);
        graphics.fill(x + 49, y + 108, x + 151, y + 111, PANEL_DARK);
        int filled = maxProgress > 0 ? 100 * progress / maxProgress : 0;
        if (filled > 0) {
            graphics.fill(
                    x + 50, y + 109, x + 50 + filled, y + 111,
                    blocked ? BLOCKED : CYAN);
        }
        for (int divider = 1; divider < 10; divider++) {
            int dividerX = x + 50 + divider * 10;
            graphics.fill(dividerX, y + 107, dividerX + 1, y + 112, PANEL);
        }

        bevel(graphics, x + 160, y + 103, x + 188, y + 115);
        graphics.fill(x + 166, y + 106, x + 182, y + 112, PANEL_DARK);
        graphics.fill(x + 173, y + 106, x + 175, y + 112, ROTOR);
        graphics.fill(x + 168, y + 108, x + 180, y + 110, ROTOR);
    }

    private static void sample(GuiGraphics graphics, int centerX, int centerY, int color) {
        graphics.fill(centerX - 6, centerY - 6, centerX + 6, centerY + 6, ROTOR);
        graphics.fill(centerX - 4, centerY - 4, centerX + 4, centerY + 4, color);
        graphics.fill(centerX - 2, centerY - 3, centerX + 1, centerY, CYAN_LIGHT);
    }

    private static void channel(
            GuiGraphics graphics, int left, int top, int right, int bottom, int color) {
        graphics.fill(left, top, right, bottom, PANEL);
        graphics.fill(left + 2, top + 1, right, bottom - 1, color);
    }

    private static void bevel(
            GuiGraphics graphics, int left, int top, int right, int bottom) {
        graphics.fill(left, top, right, bottom, ENAMEL_SHADOW);
        graphics.fill(left + 2, top + 2, right - 2, bottom - 2, ENAMEL_LIGHT);
        graphics.fill(left + 4, top + 4, right - 4, bottom - 4, ENAMEL_MID);
    }

    private static void panel(
            GuiGraphics graphics, int left, int top, int right, int bottom) {
        graphics.fill(left, top, right, bottom, ENAMEL_SHADOW);
        graphics.fill(left + 2, top + 2, right - 2, bottom - 2, ENAMEL_LIGHT);
        graphics.fill(left + 4, top + 4, right - 4, bottom - 4, ENAMEL_MID);
    }
}
