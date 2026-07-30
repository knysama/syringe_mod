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

    private static final int WIDTH = 248;
    private static final int HEIGHT = 269;
    private static final int WORK_TOP = 25;
    private static final int WORK_BOTTOM = 153;
    private static final int SPLIT_Y = 162;
    private static final int PLAYER_INV_TOP = 184;

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
        this.inventoryLabelX = 43;
        this.inventoryLabelY = 172;
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
        graphics.fill(x + 10, y + 19, x + 93, y + 22, ENAMEL_SHADOW);
        graphics.fill(x + 12, y + 19, x + 91, y + 20, ENAMEL_LIGHT);
    }

    private void drawWorkArea(GuiGraphics graphics, int x, int y) {
        panel(graphics, x + 8, y + WORK_TOP, x + WIDTH - 8, y + WORK_BOTTOM);

        drawInputBay(graphics, x, y, 0, PURPLE);
        drawInputBay(graphics, x, y, 1, CYAN);

        int flowColor = this.menu.isOutputBlocked() ? BLOCKED : CYAN_DARK;
        // Twin reagent channels into the centrifuge.
        channel(graphics, x + 49, y + 55, x + 75, y + 59, flowColor);
        channel(graphics, x + 49, y + 103, x + 75, y + 107, flowColor);

        drawCentrifuge(graphics, x, y);

        // Output channel and isolated product bay.
        channel(graphics, x + 177, y + 76, x + 198, y + 80, flowColor);
        graphics.fill(x + 195, y + 73, x + 199, y + 83, flowColor);

        bevel(graphics, x + 196, y + 42, x + 236, y + 105);
        graphics.fill(x + 200, y + 46, x + 232, y + 101, PANEL);
        graphics.fill(x + 203, y + 49, x + 229, y + 92, PANEL_DARK);
        Slot output = this.menu.slots.get(2);
        graphics.blit(
                SLOT_TEXTURE,
                x + output.x - 1, y + output.y - 1,
                0, 0,
                18, 18,
                18, 18);
        graphics.fill(x + 203, y + 95, x + 229, y + 100, PANEL_DARK);
        graphics.fill(
                x + 206, y + 96, x + 226, y + 99,
                this.menu.isOutputBlocked() ? BLOCKED : CYAN);

        drawProgressRail(graphics, x, y);
    }

    private void drawInputBay(
            GuiGraphics graphics, int x, int y, int slotIndex, int medicineColor) {
        Slot slot = this.menu.slots.get(slotIndex);
        int bayTop = slotIndex == 0 ? 38 : 86;

        bevel(graphics, x + 13, y + bayTop, x + 58, y + bayTop + 39);
        graphics.fill(x + 17, y + bayTop + 4, x + 54, y + bayTop + 35, PANEL);
        graphics.fill(x + 20, y + bayTop + 7, x + 49, y + bayTop + 32, PANEL_DARK);
        graphics.fill(x + 19, y + bayTop + 8, x + 22, y + bayTop + 31, medicineColor);

        graphics.blit(
                SLOT_TEXTURE,
                x + slot.x - 1, y + slot.y - 1,
                0, 0,
                18, 18,
                18, 18);

        // Small connector block prevents the inputs from reading as loose slots.
        graphics.fill(x + 49, y + bayTop + 15, x + 62, y + bayTop + 24, PANEL);
        graphics.fill(x + 52, y + bayTop + 18, x + 62, y + bayTop + 21, CYAN_DARK);
    }

    private void drawCentrifuge(GuiGraphics graphics, int x, int y) {
        int progress = this.menu.getProgress();
        int maxProgress = this.menu.getMaxProgress();
        boolean running = progress > 0 && maxProgress > 0;

        bevel(graphics, x + 72, y + 31, x + 179, y + 132);
        graphics.fill(x + 77, y + 36, x + 174, y + 127, PANEL);
        graphics.fill(x + 82, y + 41, x + 169, y + 122, PANEL_DARK);
        graphics.fill(x + 87, y + 46, x + 164, y + 117, PANEL_INSET);

        int centerX = x + 125;
        int centerY = y + 81;
        int phase = running ? (progress * 8 / maxProgress) & 3 : 0;
        int rotorColor = running ? CYAN_DARK : ROTOR;

        if ((phase & 1) == 0) {
            graphics.fill(centerX - 28, centerY - 4, centerX + 28, centerY + 4, rotorColor);
            graphics.fill(centerX - 4, centerY - 25, centerX + 4, centerY + 25, rotorColor);
        } else {
            graphics.fill(centerX - 23, centerY - 19, centerX - 11, centerY - 12, rotorColor);
            graphics.fill(centerX + 11, centerY - 19, centerX + 23, centerY - 12, rotorColor);
            graphics.fill(centerX - 23, centerY + 12, centerX - 11, centerY + 19, rotorColor);
            graphics.fill(centerX + 11, centerY + 12, centerX + 23, centerY + 19, rotorColor);
        }

        graphics.fill(centerX - 8, centerY - 8, centerX + 8, centerY + 8, ROTOR);
        graphics.fill(centerX - 4, centerY - 4, centerX + 4, centerY + 4, ENAMEL_MID);

        int brightCyan = running ? CYAN_LIGHT : CYAN;
        sample(graphics, centerX, centerY - 27, brightCyan);
        sample(graphics, centerX + 30, centerY, CYAN);
        sample(graphics, centerX, centerY + 27, PURPLE);
        sample(graphics, centerX - 30, centerY, PURPLE);

        // Lid status bar above the chamber.
        graphics.fill(x + 96, y + 28, x + 154, y + 35, PANEL);
        for (int index = 0; index < 5; index++) {
            int lampX = x + 101 + index * 10;
            graphics.fill(lampX, y + 30, lampX + 6, y + 33, running ? CYAN : CYAN_DARK);
        }
    }

    private void drawProgressRail(GuiGraphics graphics, int x, int y) {
        int progress = this.menu.getProgress();
        int maxProgress = this.menu.getMaxProgress();
        boolean blocked = this.menu.isOutputBlocked();

        // Fast-process control motif at the left and ventilation motif at right.
        bevel(graphics, x + 19, y + 136, x + 45, y + 151);
        graphics.fill(x + 25, y + 140, x + 29, y + 147, CYAN);
        graphics.fill(x + 30, y + 140, x + 34, y + 147, CYAN);
        graphics.fill(x + 35, y + 140, x + 39, y + 147, CYAN);

        graphics.fill(x + 49, y + 136, x + 199, y + 151, ENAMEL_SHADOW);
        graphics.fill(x + 52, y + 139, x + 196, y + 148, PANEL);
        graphics.fill(x + 55, y + 141, x + 193, y + 146, PANEL_DARK);
        int filled = maxProgress > 0 ? 136 * progress / maxProgress : 0;
        if (filled > 0) {
            graphics.fill(
                    x + 56, y + 142, x + 56 + filled, y + 145,
                    blocked ? BLOCKED : CYAN);
        }
        for (int divider = 1; divider < 10; divider++) {
            int dividerX = x + 56 + divider * 14;
            graphics.fill(dividerX, y + 140, dividerX + 1, y + 147, PANEL);
        }

        bevel(graphics, x + 203, y + 136, x + 229, y + 151);
        graphics.fill(x + 210, y + 139, x + 222, y + 148, PANEL_DARK);
        graphics.fill(x + 215, y + 140, x + 217, y + 147, ROTOR);
        graphics.fill(x + 211, y + 143, x + 221, y + 145, ROTOR);
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
