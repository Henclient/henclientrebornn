package net.henclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.henclient.config.ConfigManager;
import net.henclient.gui.ClickGuiScreen;
import net.henclient.gui.HudOverlay;
import net.henclient.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HenClient implements ClientModInitializer {
public static final String MOD_ID = "henclient";
public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
private static HenClient instance;
private ModuleManager moduleManager;
private ConfigManager configManager;
private KeyBinding guiKeyBinding;

@Override
public void onInitializeClient() {
    instance = this;
    LOGGER.info("Initializing HenClient Reborn for Minecraft 1.21.11...");

    this.moduleManager = new ModuleManager();
    this.configManager = new ConfigManager(this);

    this.guiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.henclient.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.henclient"
    ));

    this.configManager.load();

    ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    HudRenderCallback.EVENT.register(new HudOverlay(this)::render);
    WorldRenderEvents.AFTER_ENTITIES.register(context -> moduleManager.onRenderWorld(context));

    LOGGER.info("HenClient Reborn successfully initialized.");
}

private void onClientTick(MinecraftClient client) {
    if (client.player == null || client.world == null) {
        return;
    }

    while (guiKeyBinding.wasPressed()) {
        client.setScreen(new ClickGuiScreen(this));
    }

    moduleManager.onTick(client);
}

public static HenClient getInstance() {
    return instance;
}

public ModuleManager getModuleManager() {
    return moduleManager;
}

public ConfigManager getConfigManager() {
    return configManager;
}
}
