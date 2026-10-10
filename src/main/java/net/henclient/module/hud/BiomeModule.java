package net.henclient.module.hud;

import net.henclient.module.Category;
import net.henclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class BiomeModule extends Module {
public BiomeModule() {
super("Biome", "Displays current biome at player location.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
setEnabled(true);
}
}
