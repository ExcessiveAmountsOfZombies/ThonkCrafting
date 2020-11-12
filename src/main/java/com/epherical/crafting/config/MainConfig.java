package com.epherical.crafting.config;

import com.google.gson.*;
import org.bukkit.NamespacedKey;

import java.io.*;
import java.util.ArrayList;

public class MainConfig {

    private final Gson GSON;
    private final File configFile;

    private boolean debugEnabled;
    private ArrayList<NamespacedKey> removedRecipes = new ArrayList<>();

    public MainConfig(File file, Gson gson) {
        this.configFile = file;
        this.GSON = gson;

        checkDefaults();
        readFile();
    }

    private void checkDefaults() {
        try (Reader reader = new FileReader(configFile)) {
            JsonObject main = GSON.fromJson(reader, JsonObject.class);

            addDefaultIfNotPresent(main, "debug", new JsonPrimitive(false));
            addDefaultIfNotPresent(main, "removed-recipes", new JsonArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile() {
        try (Reader reader = new FileReader(configFile)) {
            JsonObject config = GSON.fromJson(reader, JsonObject.class);
            debugEnabled = config.getAsJsonPrimitive("debug").getAsBoolean();
            for (JsonElement element : config.getAsJsonArray("removed-recipes")) {
                if (element.isJsonPrimitive()) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDefaultIfNotPresent(JsonObject object, String key, JsonElement element) {
        if (!object.has(key)) {
            object.add(key, element);
        }
    }

    public ArrayList<NamespacedKey> getRemovedRecipes() {
        return removedRecipes;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}
