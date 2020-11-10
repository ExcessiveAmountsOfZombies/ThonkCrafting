package com.epherical.crafting.config;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class DefaultConfig {

    /**
     * Pull a default config from the jar of the plugin and then write to the plugins data folder.
     * @param fileToWrite File that needs to match the one that is bundled with the jar.
     */
    public static void createDefaultConfig(JavaPlugin plugin, Gson gson, File fileToWrite) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        JsonElement object = null;
        if (!fileToWrite.exists()) {
            InputStream path = plugin.getClass().getResourceAsStream("/" + fileToWrite.getName());
            try (Reader reader = new BufferedReader(new InputStreamReader(path, Charsets.UTF_8))) {
                object = gson.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (Writer writer = new FileWriter(fileToWrite)) {
                gson.toJson(object, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return Will return true if it successfully creates the directory.
     */
    public static boolean createDefaultFolder(JavaPlugin plugin, File folderToCreate) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        return !folderToCreate.exists() && folderToCreate.mkdirs();
    }
}
