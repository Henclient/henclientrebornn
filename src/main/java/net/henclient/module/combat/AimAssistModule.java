package net.henclient.module.combat;

import net.henclient.module.Category;
import net.henclient.module.Module;
import net.henclient.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;

public class AimAssistModule extends Module {
public final Setting<Double> range = new Setting<>("Range", 4.5, 2.0, 7.0);
public final Setting<Double> smoothness = new Setting<>("Smoothness", 0.25, 0.05, 0.9);
public AimAssistModule() {
    super("Aim Assist", "Provides subtle crosshair adjustments toward target entities.", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN);
    addSetting(range);
    addSetting(smoothness);
}

@Override
public void onTick(MinecraftClient client) {
    if (client.player == null || client.world == null) return;

    double reach = range.getValue();

    List<LivingEntity> targets = client.world.getEntitiesByClass(
            LivingEntity.class,
            client.player.getBoundingBox().expand(reach),
            entity -> entity != client.player && entity.isAlive() && !entity.isInvisible()
    );

    targets.stream()
            .min(Comparator.comparingDouble(e -> e.squaredDistanceTo(client.player)))
            .ifPresent(target -> smoothLookAt(client, target));
}

private void smoothLookAt(MinecraftClient client, Entity target) {
    Vec3d diff = target.getEyePos().subtract(client.player.getEyePos());
    double distXZ = Math.sqrt(diff.x * diff.x + diff.z * diff.z);

    float targetYaw = (float) (Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0F);
    float targetPitch = (float) (-Math.toDegrees(Math.atan2(diff.y, distXZ)));

    float step = smoothness.getValue().floatValue();
    float newYaw = MathHelper.lerpAngleDegrees(step, client.player.getYaw(), targetYaw);
    float newPitch = MathHelper.lerp(step, client.player.getPitch(), targetPitch);

    client.player.setYaw(newYaw);
    client.player.setPitch(newPitch);
}
}
