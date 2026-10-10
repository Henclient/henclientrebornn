package net.henclient.module;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.henclient.module.combat.AimAssistModule;
import net.henclient.module.combat.AutoClickerModule;
import net.henclient.module.exploration.*;
import net.henclient.module.hud.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleManager {
private final List<Module> modules = new ArrayList<>();
public ModuleManager() {
    register(new CoordinatesModule());
    register(new FpsModule());
    register(new HealthHudModule());
    register(new SprintStatusModule());
    register(new ArmorStatusModule());
    register(new DirectionModule());
    register(new BiomeModule());

    register(new StorageFinderModule());
    register(new SpawnerFinderModule());
    register(new SusChunkFinderModule());
    register(new StorageEspModule());
    register(new FreecamModule());

    register(new AutoClickerModule());
    register(new AimAssistModule());
}

private void register(Module module) {
    modules.add(module);
}

public List<Module> getModules() {
    return Collections.unmodifiableList(modules);
}

public List<Module> getModulesByCategory(Category category) {
    List<Module> list = new ArrayList<>();
    for (Module m : modules) {
        if (m.getCategory() == category) {
            list.add(m);
        }
    }
    return list;
}

public Module getModuleByClass(Class<? extends Module> clazz) {
    for (Module m : modules) {
        if (m.getClass().equals(clazz)) {
            return m;
        }
    }
    return null;
}

public void onTick(MinecraftClient client) {
    for (Module m : modules) {
        if (m.isEnabled()) {
            m.onTick(client);
        }
    }
}

public void onRenderWorld(WorldRenderContext context) {
    for (Module m : modules) {
        if (m.isEnabled()) {
            m.onRenderWorld(context);
        }
    }
}
}
