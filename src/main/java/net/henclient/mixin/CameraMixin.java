package net.henclient.mixin;

import net.henclient.HenClient;
import net.henclient.module.exploration.FreecamModule;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
@Shadow
protected abstract void setPos(double x, double y, double z);

@Shadow
protected abstract void setRotation(float yaw, float pitch);

@Inject(method = "update", at = @At("TAIL"))
private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
    FreecamModule freecam = (FreecamModule) HenClient.getInstance()
            .getModuleManager()
            .getModuleByClass(FreecamModule.class);

    if (freecam != null && freecam.isEnabled()) {
        this.setPos(freecam.getX(), freecam.getY(), freecam.getZ());
        this.setRotation(freecam.getYaw(), freecam.getPitch());
    }
}
}
