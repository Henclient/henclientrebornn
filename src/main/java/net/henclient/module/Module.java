package net.henclient.module;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
private final String name;
private final String description;
private final Category category;
private boolean enabled;
private int keyCode;
private final List<Setting<?>> settings = new ArrayList<>();
public Module(String name, String description, Category category, int defaultKey) {
    this.name = name;
    this.description = description;
    this.category = category;
    this.keyCode = defaultKey;
    this.enabled = false;
}

public void toggle() {
    setEnabled(!enabled);
}

public void setEnabled(boolean state) {
    if (this.enabled != state) {
        this.enabled = state;
        if (this.enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }
}

protected void onEnable() {}
protected void onDisable() {}
public void onTick(MinecraftClient client) {}
public void onRenderWorld(WorldRenderContext context) {}

protected void addSetting(Setting<?> setting) {
    this.settings.add(setting);
}

public String getName() { return name; }
public String getDescription() { return description; }
public Category getCategory() { return category; }
public boolean isEnabled() { return enabled; }
public int getKeyCode() { return keyCode; }
public void setKeyCode(int keyCode) { this.keyCode = keyCode; }
public List<Setting<?>> getSettings() { return settings; }
}
