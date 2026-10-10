package net.henclient.module.hud;

import net.henclient.module.Category;
import net.henclient.module.Module;
import net.henclient.module.Setting;
import org.lwjgl.glfw.GLFW;

public class ArmorStatusModule extends Module {
public final Setting<Integer> warningThreshold = new Setting<>("Damage Warn %", 15, 1, 50);
public ArmorStatusModule() {
    super("Armor Status", "Monitors equipped armor and low-durability warnings.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
    addSetting(warningThreshold);
    setEnabled(true);
}
}
