package net.henclient.module.exploration;

import net.henclient.module.Category;
import net.henclient.module.Module;
import net.henclient.module.Setting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnerFinderModule extends Module {
public final Setting<Integer> scanRadius = new Setting<>("Chunk Radius", 4, 1, 8);
private final Set detectedSpawners = ConcurrentHashMap.newKeySet();
private int tickTimer = 0;
public SpawnerFinderModule() {
    super("Spawner Finder", "Finds active and dormant mob spawners in loaded chunks.", Category.EXPLORATION, GLFW.GLFW_KEY_UNKNOWN);
    addSetting(scanRadius);
}

@Override
public void onTick(MinecraftClient client) {
    if (client.world == null || client.player == null) return;

    if (tickTimer++ < 60) return;
    tickTimer = 0;

    detectedSpawners.clear();
    int cx = client.player.getChunkPos().x;
    int cz = client.player.getChunkPos().z;
    int r = scanRadius.getValue();

    for (int x = -r; x <= r; x++) {
        for (int z = -r; z <= r; z++) {
            if (!client.world.isChunkLoaded(cx + x, cz + z)) continue;
            WorldChunk chunk = client.world.getChunk(cx + x, cz + z);
            for (BlockEntity be : chunk.getBlockEntities().values()) {
                if (be instanceof MobSpawnerBlockEntity) {
                    detectedSpawners.add(be.getPos());
                }
            }
        }
    }
}

public Set<BlockPos> getDetectedSpawners() {
    return Collections.unmodifiableSet(detectedSpawners);
}
}
