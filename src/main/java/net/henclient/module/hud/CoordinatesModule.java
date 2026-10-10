package net.henclient.module.hud;

import net.henclient.module.Category;
import net.henclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class CoordinatesModule extends Module {
public CoordinatesModule() {
super("Coordinates", "Shows current player XYZ position.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
setEnabled(true);
}
}
