package com.epherical.crafting;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.epherical.crafting.config.ConfigManager;
import com.epherical.crafting.config.MainConfig;
import com.epherical.crafting.listener.CookingBlockListener;
import com.epherical.crafting.listener.RecipeListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;



public class ThonkCrafting extends JavaPlugin implements Listener {

    private ProtocolManager protocolManager;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static ConfigManager manager;

    @Override
    public void onLoad() {
        CraftingRegistry.init();
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        PacketListener.addPacketRecipeListener(protocolManager, this);
        DataPackAdder.addDataPackLocation(this);
        OptionRegister.init();
        manager = new ConfigManager(this);
        //getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RecipeListener(), this);
        getServer().getPluginManager().registerEvents(new CookingBlockListener(this), this);

    }

    @Override
    public void onDisable() {
        protocolManager.removePacketListeners(this);
    }

    public Gson getGson() {
        return gson;
    }

    public static NamespacedKey createKey(String namespace, String value) {
        return new NamespacedKey(namespace, value);
    }

    public static NamespacedKey createKey(String entireKey) {
        String[] split = entireKey.split(":");
        return split.length > 1 ? new NamespacedKey(split[0], split[1]) : null;
    }

    public static MainConfig getMainConfig() {
        return manager.getMainConfig();
    }
}
