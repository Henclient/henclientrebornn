package net.henclient.module.exploration;

import net.henclient.module.Category;
import net.henclient.module.Module;
import net.henclient.module.Setting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StorageFinderModule extends Module {
public final Setting<Integer> rangeChunks = new Setting<>("Chunk Radius", 3, 1, 6);
private final Set detectedStorages = ConcurrentHashMap.newKeySet();
private int tickDelay = 0;
public StorageFinderModule() {
    super("Storage Finder", "Scans loaded chunks for chests, barrels and shulker boxes.", Category.EXPLORATION, GLFW.GLFW_KEY_UNKNOWN);
    addSetting(rangeChunks);
}

@Override
public void onTick(MinecraftClient client) {
    if (client.world == null || client.player == null) return;

    if (tickDelay++ < 40) return;
    tickDelay = 0;

    detectedStorages.clear();
    int playerChunkX = client.player.getChunkPos().x;
    int playerChunkZ = client.player.getChunkPos().z;
    int radius = rangeChunks.getValue();

    for (int dx = -radius; dx <= radius; dx++) {
        for (int dz = -radius; dz <= radius; dz++) {
            if (!client.world.isChunkLoaded(playerChunkX + dx, playerChunkZ + dz)) continue;
            WorldChunk chunk = client.world.getChunk(playerChunkX + dx, playerChunkZ + dz);
            for (BlockEntity be : chunk.getBlockEntities().values()) {
                if (be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity || be instanceof ShulkerBoxBlockEntity) {
                    detectedStorages.add(be.getPos());
                }
            }
        }
    }
}

public Set<BlockPos> getDetectedStorages() {
    return Collections.unmodifiableSet(detectedStorages);
}
}
