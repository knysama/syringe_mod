package cn.autoforged.syringe_mod.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class PotionMixingTableScreen extends AbstractContainerScreen<PotionMixingTableMenu> {
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
    private static final ResourceLocation TEX_PLUS = tex("process_plus");
    private static final ResourceLocation TEX_ARROW = tex("brewing_stand_arrow");

    private static final int BORDER = 5;
    private static final int SLOT = 18;
    private static final int GAP = 46;
    private static final int SPLIT = 14;
    private static final int PLAYER_INV_H = 76;

    public PotionMixingTableScreen(PotionMixingTableMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = BORDER * 2 + 162;
        this.imageHeight = BORDER + SLOT + GAP + SLOT + SPLIT + PLAYER_INV_H + BORDER;
        this.inventoryLabelY = this.imageHeight - 94;
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

        for (int i = 0; i < 3; i++) {
            Slot slot = this.menu.slots.get(i);
            g.blit(TEX_SLOT, x + slot.x - 1, y + slot.y - 1, 0, 0, 18, 18, 18, 18);
        }

        int arrowY = y + BORDER + SLOT + (GAP - 26) / 2;
        int progress = menu.getContainerData().get(0);
        int maxProgress = menu.getContainerData().get(1);

        g.setColor(0.4f, 0.4f, 0.4f, 1.0f);
        g.blit(TEX_ARROW, x + 74, arrowY, 0, 0, 7, 26, 7, 26);
        g.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        int arrowH = maxProgress > 0 ? 26 * progress / maxProgress : 0;
        if (arrowH > 0) {
            g.blit(TEX_ARROW, x + 74, arrowY + 26 - arrowH, 0, 26 - arrowH, 7, arrowH, 7, 26);
        }

        int splitY = y + BORDER + SLOT + GAP + SLOT;
        g.blit(TEX_SPLIT, x + 5, splitY, 0, 0, W - 10, 14, 1, 14);

        int playerInvLeft = x + (W - 162) / 2;
        int playerInvTop = splitY + 14;
        g.blit(TEX_PSLOT, playerInvLeft - 1, playerInvTop - 1, 0, 0, 162, 76, 162, 76);
    }
}
