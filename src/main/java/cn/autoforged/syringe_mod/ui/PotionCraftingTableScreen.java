package cn.autoforged.syringe_mod.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;

public class PotionCraftingTableScreen extends AbstractContainerScreen<PotionCraftingTableMenu> {
    private static final String NS = "autoforge_bricks";
    private static ResourceLocation tex(String name) {
        return ResourceLocation.fromNamespaceAndPath(NS, "textures/gui/" + name + ".png");
    }
    private static final ResourceLocation TEX_SLOT  = tex("slot_default");
    private static final ResourceLocation TEX_PSLOT = tex("player_slots_9x4");
    private static final ResourceLocation TEX_FILL  = tex("fill_white");
    private static final ResourceLocation TEX_SPLIT = tex("split_bar");
    private static final ResourceLocation TEX_TL = tex("border_corner_tl");
    private static final ResourceLocation TEX_TR = tex("border_corner_tr");
    private static final ResourceLocation TEX_BL = tex("border_corner_bl");
    private static final ResourceLocation TEX_BR = tex("border_corner_br");
    private static final ResourceLocation TEX_ET = tex("border_edge_top");
    private static final ResourceLocation TEX_EB = tex("border_edge_bottom");
    private static final ResourceLocation TEX_EL = tex("border_edge_left");
    private static final ResourceLocation TEX_ER = tex("border_edge_right");
    private static final int WIDTH = 176;
    private static final int HEIGHT = 190;
    private static final int WORK_TOP = 21;
    private static final int WORK_BOTTOM = 87;
    private static final int SPLIT_Y = 94;
    private static final int PLAYER_INV_TOP = 109;

    private static final int ENAMEL_LIGHT = 0xFFD9D7D0;
    private static final int ENAMEL_MID = 0xFFB8B7B2;
    private static final int ENAMEL_SHADOW = 0xFF85898A;
    private static final int PANEL_DARK = 0xFF252B30;
    private static final int PANEL_INSET = 0xFF171C20;
    private static final int CYAN = 0xFF20B8C7;
    private static final int CYAN_DARK = 0xFF126A73;
    private static final int HEAT = 0xFFE47728;
    private static final int BLOCKED = 0xFFB74742;

    public PotionCraftingTableScreen(PotionCraftingTableMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = WIDTH;
        this.imageHeight = HEIGHT;
        this.titleLabelX = 9;
        this.titleLabelY = 7;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 98;
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mx, int my) {
        int x = this.leftPos, y = this.topPos;
        int W = this.imageWidth, H = this.imageHeight;

        g.blit(TEX_FILL, x + 5, y + 5, 0, 0, W - 10, H - 10, 1, 1);

        g.blit(TEX_TL, x, y, 0, 0, 5, 5, 5, 5);
        g.blit(TEX_TR, x + W - 5, y, 0, 0, 5, 5, 5, 5);
        g.blit(TEX_BL, x, y + H - 5, 0, 0, 5, 5, 5, 5);
        g.blit(TEX_BR, x + W - 5, y + H - 5, 0, 0, 5, 5, 5, 5);

        g.blit(TEX_ET, x + 5, y, 0, 0, W - 10, 5, 1, 5);
        g.blit(TEX_EB, x + 5, y + H - 5, 0, 0, W - 10, 5, 1, 5);
        g.blit(TEX_EL, x, y + 5, 0, 0, 5, H - 10, 5, 1);
        g.blit(TEX_ER, x + W - 5, y + 5, 0, 0, 5, H - 10, 5, 1);

        // 工作区外壳：暖白搪瓷边框包围深色机械内衬。
        panel(g, x + 9, y + WORK_TOP, x + W - 9, y + WORK_BOTTOM);
        g.fill(x + 13, y + WORK_TOP + 4, x + 94, y + WORK_BOTTOM - 4, PANEL_DARK);
        g.fill(x + 96, y + WORK_TOP + 4, x + 136, y + WORK_BOTTOM - 4, PANEL_DARK);
        g.fill(x + 138, y + WORK_TOP + 4, x + W - 13, y + WORK_BOTTOM - 4, PANEL_DARK);

        // 三联原料架：编号色块比文字更适合低分辨率 GUI。
        int[] markerColors = {0xFFCA5A32, 0xFFD2AE38, 0xFF5AA447};
        for (int i = 0; i < 3; i++) {
            Slot slot = this.menu.slots.get(i);
            int slotX = x + slot.x;
            int slotY = y + slot.y;
            g.fill(slotX - 3, y + 30, slotX + 19, y + 66, PANEL_INSET);
            g.fill(slotX - 1, y + 31, slotX + 17, y + 34, markerColors[i]);
            g.blit(TEX_SLOT, slotX - 1, slotY - 1, 0, 0, 18, 18, 18, 18);
            g.fill(slotX + 7, y + 64, slotX + 9, y + 72, ENAMEL_SHADOW);
        }

        // The center rack is a dedicated container slot. A small bottle-shaped
        // stencil remains visible while empty so its purpose is readable without JEI.
        Slot bottleSlot = this.menu.slots.get(1);
        if (!bottleSlot.hasItem()) {
            int bx = x + bottleSlot.x;
            int by = y + bottleSlot.y;
            int glass = 0xFFB9D7DD;
            int glassShadow = 0xFF63848C;
            g.fill(bx + 7, by + 3, bx + 10, by + 5, glass);
            g.fill(bx + 6, by + 5, bx + 11, by + 7, glassShadow);
            g.fill(bx + 4, by + 7, bx + 13, by + 14, glassShadow);
            g.fill(bx + 6, by + 8, bx + 11, by + 12, PANEL_INSET);
            g.fill(bx + 5, by + 13, bx + 12, by + 15, glass);
        }

        // 原料汇流到水浴锅。
        g.fill(x + 28, y + 72, x + 82, y + 75, ENAMEL_SHADOW);
        g.fill(x + 80, y + 69, x + 99, y + 72, ENAMEL_SHADOW);
        g.fill(x + 94, y + 67, x + 99, y + 74, ENAMEL_SHADOW);

        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();
        boolean blocked = menu.isOutputBlocked();
        int potX = x + 101;
        int potY = y + 34;
        int potW = 30;
        int liquidH = maxProgress > 0 ? 17 * progress / maxProgress : 0;
        int rimColor = blocked ? BLOCKED : ENAMEL_SHADOW;
        g.fill(potX - 3, potY + 3, potX, potY + 7, rimColor);
        g.fill(potX + potW, potY + 3, potX + potW + 3, potY + 7, rimColor);
        g.fill(potX, potY, potX + potW, potY + 4, rimColor);
        g.fill(potX + 3, potY + 4, potX + potW - 3, potY + 23, PANEL_INSET);
        if (liquidH > 0) {
            g.fill(potX + 5, potY + 21 - liquidH, potX + potW - 5, potY + 21, CYAN);
            g.fill(potX + 5, potY + 21 - liquidH, potX + potW - 5, potY + 23 - liquidH, 0xFF75D1D8);
        }
        g.fill(potX + 5, potY + 25, potX + potW - 5, potY + 29, 0xFF442A21);
        int heatW = maxProgress > 0 ? 20 * progress / maxProgress : 0;
        if (heatW > 0) {
            g.fill(potX + 5, potY + 25, potX + 5 + heatW, potY + 29, HEAT);
        }

        // 加工方向箭头和独立产物仓。
        int flowColor = blocked ? BLOCKED : CYAN_DARK;
        g.fill(x + 132, y + 48, x + 141, y + 51, flowColor);
        g.fill(x + 138, y + 45, x + 141, y + 54, flowColor);
        Slot output = this.menu.slots.get(3);
        g.fill(x + output.x - 4, y + output.y - 4, x + output.x + 20, y + output.y + 20,
                blocked ? BLOCKED : CYAN_DARK);
        g.blit(TEX_SLOT, x + output.x - 1, y + output.y - 1, 0, 0, 18, 18, 18, 18);
        g.fill(x + 145, y + 70, x + 161, y + 74, PANEL_INSET);
        g.fill(x + 147, y + 71, x + 159, y + 73, blocked ? BLOCKED : CYAN);

        g.blit(TEX_SPLIT, x + 5, y + SPLIT_Y, 0, 0, W - 10, 14, 1, 14);

        int playerInvLeft = x + (W - 162) / 2;
        g.blit(TEX_PSLOT, playerInvLeft - 1, y + PLAYER_INV_TOP - 1, 0, 0, 162, 76, 162, 76);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        Slot bottleSlot = this.menu.slots.get(1);
        if (!bottleSlot.hasItem() && isHovering(bottleSlot.x, bottleSlot.y, 16, 16, mouseX, mouseY)) {
            g.renderTooltip(this.font, Items.GLASS_BOTTLE.getDescription(), mouseX, mouseY);
        }
    }

    private static void panel(GuiGraphics g, int left, int top, int right, int bottom) {
        g.fill(left, top, right, bottom, ENAMEL_SHADOW);
        g.fill(left + 2, top + 2, right - 2, bottom - 2, ENAMEL_LIGHT);
        g.fill(left + 4, top + 4, right - 4, bottom - 4, ENAMEL_MID);
    }
}
