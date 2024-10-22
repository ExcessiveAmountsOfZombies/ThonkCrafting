package com.epherical.crafting.config;

import com.epherical.crafting.ThonkCrafting;

import java.io.File;

public class ConfigManager {

    private File config;

    private File recipeFolder;

    private File recipeOverrides;
    private File customRecipes;


    private MainConfig mainConfig;
    private RecipeLoader loader;

    public ConfigManager(ThonkCrafting plugin) {
        config = new File(plugin.getDataFolder(), "config.json");
        DefaultConfig.createDefaultConfig(plugin, plugin.getGson(), config);

        mainConfig = new MainConfig(config, plugin.getGson());

        recipeFolder = new File(plugin.getDataFolder(), "recipes");

        recipeOverrides = new File(recipeFolder, "minecraft");
        customRecipes = new File(recipeFolder, "thonkcrafting");

        DefaultConfig.createDefaultFolder(plugin, recipeOverrides);
        DefaultConfig.createDefaultFolder(plugin, customRecipes);

        loader = new RecipeLoader(recipeFolder, plugin.getGson(), plugin.getNmsInterface());
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public RecipeLoader getLoader() {
        return loader;
    }
}
