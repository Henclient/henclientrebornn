package net.henclient.module.exploration;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.henclient.HenClient;
import net.henclient.module.Category;
import net.henclient.module.Module;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class StorageEspModule extends Module {
public StorageEspModule() {
    super("Storage ESP", "Outlines nearby chests and container blocks.", Category.EXPLORATION, GLFW.GLFW_KEY_V);
}

@Override
public void onRenderWorld(WorldRenderContext context) {
    StorageFinderModule finder = (StorageFinderModule) HenClient.getInstance()
            .getModuleManager()
            .getModuleByClass(StorageFinderModule.class);

    if (finder == null || !finder.isEnabled()) return;

    Vec3d cameraPos = context.camera().getPos();
    MatrixStack matrices = context.matrixStack();

    matrices.push();
    matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

    RenderSystem.disableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

    for (BlockPos pos : finder.getDetectedStorages()) {
        drawBoxOutline(buffer, pos.getX(), pos.getY(), pos.getZ(), 1.0f, 0.2f, 0.2f, 0.8f);
    }

    BufferRenderer.drawWithGlobalProgram(buffer.end());

    RenderSystem.enableDepthTest();
    RenderSystem.disableBlend();
    matrices.pop();
}

private void drawBoxOutline(BufferBuilder buffer, double x, double y, double z, float r, float g, float b, float a) {
    float x1 = (float) x, y1 = (float) y, z1 = (float) z;
    float x2 = x1 + 1.0f, y2 = y1 + 1.0f, z2 = z1 + 1.0f;

    buffer.vertex(x1, y1, z1).color(r, g, b, a); buffer.vertex(x2, y1, z1).color(r, g, b, a);
    buffer.vertex(x2, y1, z1).color(r, g, b, a); buffer.vertex(x2, y1, z2).color(r, g, b, a);
    buffer.vertex(x2, y1, z2).color(r, g, b, a); buffer.vertex(x1, y1, z2).color(r, g, b, a);
    buffer.vertex(x1, y1, z2).color(r, g, b, a); buffer.vertex(x1, y1, z1).color(r, g, b, a);

    buffer.vertex(x1, y2, z1).color(r, g, b, a); buffer.vertex(x2, y2, z1).color(r, g, b, a);
    buffer.vertex(x2, y2, z1).color(r, g, b, a); buffer.vertex(x2, y2, z2).color(r, g, b, a);
    buffer.vertex(x2, y2, z2).color(r, g, b, a); buffer.vertex(x1, y2, z2).color(r, g, b, a);
    buffer.vertex(x1, y2, z2).color(r, g, b, a); buffer.vertex(x1, y2, z1).color(r, g, b, a);

    buffer.vertex(x1, y1, z1).color(r, g, b, a); buffer.vertex(x1, y2, z1).color(r, g, b, a);
    buffer.vertex(x2, y1, z1).color(r, g, b, a); buffer.vertex(x2, y2, z1).color(r, g, b, a);
    buffer.vertex(x2, y1, z2).color(r, g, b, a); buffer.vertex(x2, y2, z2).color(r, g, b, a);
    buffer.vertex(x1, y1, z2).color(r, g, b, a); buffer.vertex(x1, y2, z2).color(r, g, b, a);
}
}
