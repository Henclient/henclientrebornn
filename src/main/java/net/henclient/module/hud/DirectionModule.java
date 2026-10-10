package net.henclient.module.hud;

import net.henclient.module.Category;
import net.henclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class DirectionModule extends Module {
public DirectionModule() {
super("Direction", "Displays player facing cardinal direction and yaw axis.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
setEnabled(true);
}
}
