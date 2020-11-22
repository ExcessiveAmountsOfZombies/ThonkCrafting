package com.epherical.crafting.config;

import com.epherical.crafting.ThonkCrafting;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.io.*;
import java.util.ArrayList;

public class MainConfig {

    private final Gson GSON;
    private final File configFile;

    private static boolean debugEnabled;
    private static ArrayList<NamespacedKey> removedRecipes = new ArrayList<>();

    public MainConfig(File file, Gson gson) {
        this.configFile = file;
        this.GSON = gson;

        // If something was deleted we'll add it back
        readConfigAndFixDefaults();
    }

    private void readConfigAndFixDefaults() {
        try (Reader reader = new FileReader(configFile)) {
            JsonObject main = GSON.fromJson(reader, JsonObject.class);

            addDefaultIfNotPresent(main, "debug", new JsonPrimitive(false));
            addDefaultIfNotPresent(main, "removed-recipes", new JsonArray());

            debugEnabled = main.getAsJsonPrimitive("debug").getAsBoolean();
            for (JsonElement element : main.getAsJsonArray("removed-recipes")) {
                if (element.isJsonPrimitive()) {
                    String recipe = element.getAsString();
                    NamespacedKey key = ThonkCrafting.createKey(recipe);
                    if (key != null) {
                        removedRecipes.add(key);
                    }
                }
            }
            // TODO: something different
            removedRecipes.forEach(Bukkit::removeRecipe);

            try (Writer writer = new FileWriter(configFile)) {
                GSON.toJson(main, writer);
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

    public static ArrayList<NamespacedKey> getRemovedRecipes() {
        return removedRecipes;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }
}
