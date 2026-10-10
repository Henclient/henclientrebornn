package net.henclient.module.combat;

import net.henclient.module.Category;
import net.henclient.module.Module;
import net.henclient.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class AutoClickerModule extends Module {
public final Setting<Integer> clickDelayTicks = new Setting<>("Delay Ticks", 2, 1, 10);
private int counter = 0;
public AutoClickerModule() {
    super("Auto Clicker", "Automates continuous attacks while holding attack input.", Category.COMBAT, GLFW.GLFW_KEY_R);
    addSetting(clickDelayTicks);
}

@Override
public void onTick(MinecraftClient client) {
    if (client.player == null || client.interactionManager == null) return;

    if (client.options.attackKey.isPressed()) {
        if (counter++ >= clickDelayTicks.getValue()) {
            counter = 0;
            if (client.targetedEntity != null) {
                client.interactionManager.attackEntity(client.player, client.targetedEntity);
                client.player.swingHand(Hand.MAIN_HAND);
            }
        }
    } else {
        counter = 0;
    }
}
}
