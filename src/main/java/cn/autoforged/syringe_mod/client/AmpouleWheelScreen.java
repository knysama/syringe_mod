package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.integration.OptionalAccessoryBag;
import cn.autoforged.syringe_mod.item.AmpouleItem;
import cn.autoforged.syringe_mod.item.InjectionGunItem;
import cn.autoforged.syringe_mod.item.MedicineBagItemHandler;
import cn.autoforged.syringe_mod.item.SyringeBagItem;
import cn.autoforged.syringe_mod.network.payload.ServerboundSelectAmpoulePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public final class AmpouleWheelScreen extends Screen {
    private static final int PAGE_SIZE = 8;
    private static final int RADIUS = 76;
    private static final int SLOT_RADIUS = 14;
    private final List<WheelEntry> entries;
    private final ItemStack loadedAmpoule;
    private int page;
    private int hovered = -1;

    public AmpouleWheelScreen(Player player) {
        super(Component.translatable("screen.syringe_mod.ampoule_wheel"));
        this.entries = collectAmpoules(player);
        this.loadedAmpoule = InjectionGunItem.getLoadedAmpoule(player.getMainHandItem());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        int centerX = width / 2;
        int centerY = height / 2;
        hovered = findHovered(mouseX, mouseY, centerX, centerY);

        graphics.fill(centerX - 25, centerY - 10, centerX + 25, centerY + 10, 0xA0000000);
        graphics.drawCenteredString(font,
                Component.translatable("screen.syringe_mod.ampoule_wheel.page",
                        page + 1, Math.max(1, pageCount())),
                centerX, centerY - 4, 0xFFFFFF);

        int start = page * PAGE_SIZE;
        int count = Math.min(PAGE_SIZE, entries.size() - start);
        for (int i = 0; i < count; i++) {
            double angle = -Math.PI / 2.0D + Math.PI * 2.0D * i / count;
            int x = centerX + (int) Math.round(Math.cos(angle) * RADIUS);
            int y = centerY + (int) Math.round(Math.sin(angle) * RADIUS);
            WheelEntry entry = entries.get(start + i);
            boolean loaded = !loadedAmpoule.isEmpty()
                    && ItemStack.isSameItemSameComponents(loadedAmpoule, entry.prototype());
            int color = i == hovered ? 0xD060BFEF : loaded ? 0xD040A060 : 0xB0202020;
            graphics.fill(x - SLOT_RADIUS, y - SLOT_RADIUS, x + SLOT_RADIUS, y + SLOT_RADIUS, color);

            graphics.renderItem(entry.prototype(), x - 8, y - 8);
            graphics.renderItemDecorations(font, entry.prototype(), x - 8, y - 8,
                    entry.count() > 1 ? Integer.toString(entry.count()) : null);
        }

        if (hovered >= 0) {
            WheelEntry entry = entries.get(start + hovered);
            graphics.renderTooltip(font, entry.prototype(), mouseX, mouseY);
        } else if (entries.isEmpty()) {
            graphics.drawCenteredString(font,
                    Component.translatable("screen.syringe_mod.ampoule_wheel.empty"),
                    centerX, centerY + 18, 0xAAAAAA);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && hovered >= 0) {
            int index = page * PAGE_SIZE + hovered;
            if (index < entries.size()) {
                PacketDistributor.sendToServer(
                        new ServerboundSelectAmpoulePayload(entries.get(index).prototype()));
                onClose();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int pages = pageCount();
        if (pages <= 1 || scrollY == 0.0D) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        page = Math.floorMod(page + (scrollY < 0.0D ? 1 : -1), pages);
        return true;
    }

    private int findHovered(double mouseX, double mouseY, int centerX, int centerY) {
        int count = Math.min(PAGE_SIZE, entries.size() - page * PAGE_SIZE);
        for (int i = 0; i < count; i++) {
            double angle = -Math.PI / 2.0D + Math.PI * 2.0D * i / count;
            int x = centerX + (int) Math.round(Math.cos(angle) * RADIUS);
            int y = centerY + (int) Math.round(Math.sin(angle) * RADIUS);
            double dx = mouseX - x;
            double dy = mouseY - y;
            if (dx * dx + dy * dy <= SLOT_RADIUS * SLOT_RADIUS) {
                return i;
            }
        }
        return -1;
    }

    private int pageCount() {
        return Math.max(1, (entries.size() + PAGE_SIZE - 1) / PAGE_SIZE);
    }

    private static List<WheelEntry> collectAmpoules(Player player) {
        List<WheelEntry> result = new ArrayList<>();

        OptionalAccessoryBag.findBeltBag(player).ifPresent(
                bag -> collectBag(result, bag, player));
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof SyringeBagItem) {
                collectBag(result, stack, player);
            }
        }
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof AmpouleItem) {
                merge(result, stack);
            }
        }
        return result;
    }

    private static void collectBag(List<WheelEntry> result, ItemStack bag, Player player) {
        MedicineBagItemHandler handler = new MedicineBagItemHandler(bag, player.registryAccess());
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (stack.getItem() instanceof AmpouleItem) {
                merge(result, stack);
            }
        }
    }

    private static void merge(List<WheelEntry> result, ItemStack stack) {
        for (int i = 0; i < result.size(); i++) {
            WheelEntry current = result.get(i);
            if (ItemStack.isSameItemSameComponents(current.prototype(), stack)) {
                result.set(i, new WheelEntry(current.prototype(), current.count() + stack.getCount()));
                return;
            }
        }
        result.add(new WheelEntry(stack.copyWithCount(1), stack.getCount()));
    }

    private record WheelEntry(ItemStack prototype, int count) {
    }
}
