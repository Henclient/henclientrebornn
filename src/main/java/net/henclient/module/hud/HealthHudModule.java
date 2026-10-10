package net.henclient.module.hud;

import net.henclient.module.Category;
import net.henclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class HealthHudModule extends Module {
public HealthHudModule() {
super("Health HUD", "Displays numeric and visual health status.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
setEnabled(true);
}
}
