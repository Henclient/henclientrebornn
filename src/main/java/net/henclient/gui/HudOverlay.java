package net.henclient.gui;

import net.henclient.HenClient;
import net.henclient.module.Module;
import net.henclient.module.hud.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HudOverlay {
private final HenClient client;
public HudOverlay(HenClient client) {
    this.client = client;
}

public void render(DrawContext context, RenderTickCounter tickCounter) {
    MinecraftClient mc = MinecraftClient.getInstance();
    if (mc.player == null || mc.options.hudHidden) return;

    int y = 5;
    int activeRed = 0xFFFF3333;
    int dimWhite = 0xFFCCCCCC;

    context.drawTextWithShadow(mc.textRenderer, "HenClient Reborn", 5, y, activeRed);
    y += 11;

    if (isModEnabled(CoordinatesModule.class)) {
        String coords = String.format("XYZ: %.1f, %.1f, %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
        context.drawTextWithShadow(mc.textRenderer, coords, 5, y, dimWhite);
        y += 10;
    }

    if (isModEnabled(FpsModule.class)) {
        context.drawTextWithShadow(mc.textRenderer, "FPS: " + mc.getCurrentFps(), 5, y, dimWhite);
        y += 10;
    }

    if (isModEnabled(HealthHudModule.class)) {
        String hp = String.format("Health: %.1f / %.1f", mc.player.getHealth(), mc.player.getMaxHealth());
        context.drawTextWithShadow(mc.textRenderer, hp, 5, y, 0xFFFF5555);
        y += 10;
    }

    if (isModEnabled(SprintStatusModule.class)) {
        String sprinting = mc.player.isSprinting() ? "Sprinting" : "Walking";
        context.drawTextWithShadow(mc.textRenderer, "Motion: " + sprinting, 5, y, dimWhite);
        y += 10;
    }

    if (isModEnabled(DirectionModule.class)) {
        String dir = mc.player.getHorizontalFacing().asString().toUpperCase();
        context.drawTextWithShadow(mc.textRenderer, "Facing: " + dir, 5, y, dimWhite);
        y += 10;
    }

    if (isModEnabled(BiomeModule.class) && mc.world != null) {
        String biome = mc.world.getBiome(mc.player.getBlockPos()).getKey().map(k -> k.getValue().getPath()).orElse("Unknown");
        context.drawTextWithShadow(mc.textRenderer, "Biome: " + biome, 5, y, dimWhite);
        y += 10;
    }

    if (isModEnabled(ArmorStatusModule.class)) {
        renderArmorHud(context, mc, y);
    }

    renderActiveModulesList(context, mc);
}

private void renderArmorHud(DrawContext context, MinecraftClient mc, int startY) {
    int y = startY;
    EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    for (EquipmentSlot slot : slots) {
        ItemStack stack = mc.player.getEquippedStack(slot);
        if (!stack.isEmpty()) {
            int max = stack.getMaxDamage();
            int current = max - stack.getDamage();
            int pct = max > 0 ? (int) ((current / (float) max) * 100) : 100;
            int color = pct <= 15 ? 0xFFFF2222 : 0xFFAAAAAA;
            context.drawTextWithShadow(mc.textRenderer, stack.getName().getString() + " (" + pct + "%)", 5, y, color);
            y += 10;
        }
    }
}

private void renderActiveModulesList(DrawContext context, MinecraftClient mc) {
    int screenWidth = mc.getWindow().getScaledWidth();
    int y = 5;

    List<Module> active = new ArrayList<>();
    for (Module m : client.getModuleManager().getModules()) {
        if (m.isEnabled()) active.add(m);
    }

    for (Module m : active) {
        int width = mc.textRenderer.getWidth(m.getName());
        context.drawTextWithShadow(mc.textRenderer, m.getName(), screenWidth - width - 5, y, 0xFFFF2244);
        y += 10;
    }
}

private boolean isModEnabled(Class<? extends Module> clazz) {
    Module m = client.getModuleManager().getModuleByClass(clazz);
    return m != null && m.isEnabled();
}
}
