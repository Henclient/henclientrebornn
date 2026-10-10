package net.henclient.module.exploration;

import net.henclient.module.Category;
import net.henclient.module.Module;
import net.henclient.module.Setting;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class FreecamModule extends Module {
public final Setting<Double> flightSpeed = new Setting<>("Speed", 1.0, 0.1, 4.0);
private double x, y, z;
private float yaw, pitch;

public FreecamModule() {
    super("Freecam", "Detaches perspective camera from the player body.", Category.EXPLORATION, GLFW.GLFW_KEY_U);
    addSetting(flightSpeed);
}

@Override
protected void onEnable() {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client.player != null) {
        this.x = client.player.getX();
        this.y = client.player.getY() + client.player.getEyeHeight(client.player.getPose());
        this.z = client.player.getZ();
        this.yaw = client.player.getYaw();
        this.pitch = client.player.getPitch();
    }
}

@Override
public void onTick(MinecraftClient client) {
    if (client.player == null) {
        setEnabled(false);
        return;
    }

    double speed = flightSpeed.getValue();
    if (client.options.forwardKey.isPressed()) {
        x += -Math.sin(Math.toRadians(yaw)) * speed;
        z += Math.cos(Math.toRadians(yaw)) * speed;
    }
    if (client.options.backKey.isPressed()) {
        x += Math.sin(Math.toRadians(yaw)) * speed;
        z += -Math.cos(Math.toRadians(yaw)) * speed;
    }
    if (client.options.jumpKey.isPressed()) {
        y += speed;
    }
    if (client.options.sneakKey.isPressed()) {
        y -= speed;
    }
}

public double getX() { return x; }
public double getY() { return y; }
public double getZ() { return z; }
public float getYaw() { return yaw; }
public float getPitch() { return pitch; }
}
