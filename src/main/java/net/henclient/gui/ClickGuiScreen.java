package net.henclient.gui;

import net.henclient.HenClient;
import net.henclient.module.Category;
import net.henclient.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ClickGuiScreen extends Screen {
private final HenClient client;
private Module listeningModule = null;
public ClickGuiScreen(HenClient client) {
    super(Text.literal("HenClient Reborn"));
    this.client = client;
}

@Override
public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    context.fillGradient(0, 0, width, height, 0xCC110505, 0xEE1E0303);

    int panelX = 40;
    for (Category category : Category.values()) {
        renderCategoryPanel(context, category, panelX, 40, mouseX, mouseY);
        panelX += 135;
    }

    super.render(context, mouseX, mouseY, delta);
}

private void renderCategoryPanel(DrawContext context, Category cat, int x, int y, int mouseX, int mouseY) {
    int width = 125;
    int headerHeight = 18;

    context.fill(x, y, x + width, y + headerHeight, 0xFF880000);
    context.drawText(this.textRenderer, cat.getDisplayName(), x + 6, y + 5, 0xFFFFFFFF, false);

    int modY = y + headerHeight;
    List<Module> mods = client.getModuleManager().getModulesByCategory(cat);

    for (Module m : mods) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= modY && mouseY <= modY + 16;
        int bg = m.isEnabled() ? 0xFF2A0000 : 0xFF141414;
        if (hovered) bg = 0xFF4A0808;

        context.fill(x, modY, x + width, modY + 16, bg);

        int textColor = m.isEnabled() ? 0xFFFF4444 : 0xFF888888;
        String label = m.getName();
        if (listeningModule == m) {
            label = "> Press Key <";
            textColor = 0xFFFFFF00;
        }

        context.drawText(this.textRenderer, label, x + 6, modY + 4, textColor, false);

        String keyName = m.getKeyCode() == GLFW.GLFW_KEY_UNKNOWN ? "-" : GLFW.glfwGetKeyName(m.getKeyCode(), 0);
        if (keyName == null && m.getKeyCode() > 0) keyName = "K" + m.getKeyCode();
        if (keyName != null && listeningModule != m) {
            int keyW = this.textRenderer.getWidth("[" + keyName + "]");
            context.drawText(this.textRenderer, "[" + keyName + "]", x + width - keyW - 4, modY + 4, 0xFF555555, false);
        }

        modY += 17;
    }
}

@Override
public boolean mouseClicked(double mouseX, double mouseY, int button) {
    int panelX = 40;
    for (Category category : Category.values()) {
        int width = 125;
        int headerHeight = 18;
        int modY = 40 + headerHeight;

        List<Module> mods = client.getModuleManager().getModulesByCategory(category);
        for (Module m : mods) {
            if (mouseX >= panelX && mouseX <= panelX + width && mouseY >= modY && mouseY <= modY + 16) {
                if (button == 0) {
                    m.toggle();
                    client.getConfigManager().save();
                    return true;
                } else if (button == 1) {
                    listeningModule = (listeningModule == m) ? null : m;
                    return true;
                }
            }
            modY += 17;
        }
        panelX += 135;
    }
    return super.mouseClicked(mouseX, mouseY, button);
}

@Override
public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (listeningModule != null) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            listeningModule.setKeyCode(GLFW.GLFW_KEY_UNKNOWN);
        } else {
            listeningModule.setKeyCode(keyCode);
        }
        listeningModule = null;
        client.getConfigManager().save();
        return true;
    }

    if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
        this.close();
        return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
}

@Override
public boolean shouldPause() {
    return false;
}
}
