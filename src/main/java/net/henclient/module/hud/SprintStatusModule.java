package net.henclient.module.hud;

import net.henclient.module.Category;
import net.henclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class SprintStatusModule extends Module {
public SprintStatusModule() {
super("Sprint Status", "Displays if sprint mode is currently engaged.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
setEnabled(true);
}
}
