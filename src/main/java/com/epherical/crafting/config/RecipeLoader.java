package com.epherical.crafting.config;

import com.epherical.crafting.nms.NMS1_16V2;
import com.epherical.crafting.nms.NMSInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecipeLoader {
    private final File recipeFolder;
    private final Gson GSON;
    private NMSInterface nmsInterface;

    public RecipeLoader(File recipeFolder, Gson gson) {
        this.recipeFolder = recipeFolder;
        this.GSON = gson;
        try {
            nmsInterface = new NMS1_16V2();
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        loadRecipeFolders();
    }

    public void loadRecipeFolders() {
        File[] namespacedFolders = recipeFolder.listFiles((dir, name) -> dir.isDirectory());
        Map<NamespacedKey, JsonObject> recipes = new HashMap<>();
        if (namespacedFolders != null) {
            for (File namespacedFolder : namespacedFolders) {
                recipes.putAll(loadRecipes(namespacedFolder.getName().toLowerCase(),
                        namespacedFolder.listFiles((dir, name) -> name.endsWith(".json"))));
            }
        }
        nmsInterface.registerRecipes(recipes);
    }

    public Map<NamespacedKey, JsonObject> loadRecipes(String folderName, File[] files) {
        Map<NamespacedKey, JsonObject> map = new HashMap<>();
        int jsonLength = ".json".length();
        if (files != null && files.length > 0) {
            for (File file : files) {
                int fileLength = file.getName().length();
                NamespacedKey key = new NamespacedKey(folderName, file.getName().substring(0, fileLength - jsonLength));
                try (FileReader reader = new FileReader(file)) {
                    JsonObject element = map.put(key, GSON.fromJson(reader, JsonObject.class));
                    if (element != null) {
                        // TODO: log error. Probably wont ever error though
                        // TODO: throw error here and continue
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return map;
    }


}
