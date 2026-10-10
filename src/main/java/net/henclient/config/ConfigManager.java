package net.henclient.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.henclient.HenClient;
import net.henclient.module.Module;
import net.henclient.module.Setting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
private final HenClient client;
private final File configFile;
private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
public ConfigManager(HenClient client) {
    this.client = client;
    File dir = FabricLoader.getInstance().getConfigDir().toFile();
    this.configFile = new File(dir, "henclient.json");
}

public void save() {
    JsonObject root = new JsonObject();
    JsonObject modulesObj = new JsonObject();

    for (Module module : client.getModuleManager().getModules()) {
        JsonObject modObj = new JsonObject();
        modObj.addProperty("enabled", module.isEnabled());
        modObj.addProperty("key", module.getKeyCode());

        JsonObject settingsObj = new JsonObject();
        for (Setting<?> s : module.getSettings()) {
            if (s.getValue() instanceof Boolean b) {
                settingsObj.addProperty(s.getName(), b);
            } else if (s.getValue() instanceof Number n) {
                settingsObj.addProperty(s.getName(), n);
            } else if (s.getValue() instanceof String str) {
                settingsObj.addProperty(s.getName(), str);
            }
        }
        modObj.add("settings", settingsObj);
        modulesObj.add(module.getName(), modObj);
    }

    root.add("modules", modulesObj);

    try (FileWriter writer = new FileWriter(configFile)) {
        gson.toJson(root, writer);
    } catch (IOException e) {
        HenClient.LOGGER.error("Failed to save HenClient settings", e);
    }
}

public void load() {
    if (!configFile.exists()) {
        return;
    }

    try (FileReader reader = new FileReader(configFile)) {
        JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
        if (root.has("modules")) {
            JsonObject modulesObj = root.getAsJsonObject("modules");
            for (Module module : client.getModuleManager().getModules()) {
                if (modulesObj.has(module.getName())) {
                    JsonObject modObj = modulesObj.getAsJsonObject(module.getName());
                    if (modObj.has("enabled")) {
                        module.setEnabled(modObj.get("enabled").getAsBoolean());
                    }
                    if (modObj.has("key")) {
                        module.setKeyCode(modObj.get("key").getAsInt());
                    }
                    if (modObj.has("settings")) {
                        JsonObject settingsObj = modObj.getAsJsonObject("settings");
                        for (Setting<?> s : module.getSettings()) {
                            if (settingsObj.has(s.getName())) {
                                JsonElement el = settingsObj.get(s.getName());
                                applySettingValue(s, el);
                            }
                        }
                    }
                }
            }
        }
    } catch (Exception e) {
        HenClient.LOGGER.error("Failed to load HenClient settings", e);
    }
}

@SuppressWarnings("unchecked")
private <T> void applySettingValue(Setting<T> setting, JsonElement el) {
    if (setting.getValue() instanceof Boolean) {
        ((Setting<Boolean>) setting).setValue(el.getAsBoolean());
    } else if (setting.getValue() instanceof Integer) {
        ((Setting<Integer>) setting).setValue(el.getAsInt());
    } else if (setting.getValue() instanceof Double) {
        ((Setting<Double>) setting).setValue(el.getAsDouble());
    }
}
}
