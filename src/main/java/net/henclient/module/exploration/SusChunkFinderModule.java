package net.henclient.module.exploration;

import net.henclient.module.Category;
import net.henclient.module.Module;
import net.henclient.module.Setting;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SusChunkFinderModule extends Module {
public final Setting<Integer> scoreThreshold = new Setting<>("Threshold Score", 45, 10, 150);
private final Set flaggedChunks = ConcurrentHashMap.newKeySet();
private final Deque scanQueue = new ArrayDeque<>();
private int cooldown = 0;
public SusChunkFinderModule() {
    super("Sus Chunk Finder", "Heuristic analyzer targeting hidden player bases & structures.", Category.EXPLORATION, GLFW.GLFW_KEY_UNKNOWN);
    addSetting(scoreThreshold);
}

@Override
public void onTick(MinecraftClient client) {
    if (client.world == null || client.player == null) return;

    if (scanQueue.isEmpty()) {
        if (cooldown++ > 100) {
            cooldown = 0;
            int playerChunkX = client.player.getChunkPos().x;
            int playerChunkZ = client.player.getChunkPos().z;
            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    scanQueue.add(new ChunkPos(playerChunkX + dx, playerChunkZ + dz));
                }
            }
        }
        return;
    }

    ChunkPos target = scanQueue.poll();
    if (target != null && client.world.isChunkLoaded(target.x, target.z)) {
        analyzeChunk(client.world.getChunk(target.x, target.z));
    }
}

private void analyzeChunk(WorldChunk chunk) {
    int score = 0;

    for (BlockEntity be : chunk.getBlockEntities().values()) {
        if (be instanceof ChestBlockEntity) score += 5;
    }

    for (ChunkSection section : chunk.getSectionArray()) {
        if (section == null || section.isEmpty()) continue;
        var container = section.getBlockStateContainer();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    var state = container.get(x, y, z);
                    if (state.isOf(Blocks.OBSIDIAN)) score += 4;
                    else if (state.isOf(Blocks.MOSSY_COBBLESTONE)) score += 3;
                    else if (state.isOf(Blocks.RAIL) || state.isOf(Blocks.POWERED_RAIL)) score += 2;
                }
            }
        }
    }

    if (score >= scoreThreshold.getValue()) {
        flaggedChunks.add(chunk.getPos());
    } else {
        flaggedChunks.remove(chunk.getPos());
    }
}

public Set<ChunkPos> getFlaggedChunks() {
    return Collections.unmodifiableSet(flaggedChunks);
}
}
