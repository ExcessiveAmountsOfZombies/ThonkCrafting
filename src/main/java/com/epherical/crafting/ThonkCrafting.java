package com.epherical.crafting;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.epherical.crafting.commands.RecipeCommand;
import com.epherical.crafting.config.ConfigManager;
import com.epherical.crafting.config.MainConfig;
import com.epherical.crafting.listener.CookingBlockListener;
import com.epherical.crafting.listener.InventoryListener;
import com.epherical.crafting.listener.RecipeListener;
import com.epherical.crafting.nms.NMS1_16V3;
import com.epherical.crafting.nms.NMSInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;



public class ThonkCrafting extends JavaPlugin implements Listener {

    private static ThonkCrafting instance;
    private ProtocolManager protocolManager;
    private Gson gson;
    private static ConfigManager manager;

    public static final String USED_BY = "used-by";

    private static NMSInterface nmsInterface;

    @Override
    public void onLoad() {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.setPrettyPrinting().create();

        CraftingRegistry.init();
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {

        instance = this;
        super.onEnable();
        try {
            nmsInterface = new NMS1_16V3(this);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        PacketListener.addPacketRecipeListener(protocolManager, this);
        DataPackAdder.addDataPackLocation(this);
        OptionRegister.init();
        manager = new ConfigManager(this);
        //getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RecipeListener(this), this);
        getServer().getPluginManager().registerEvents(new CookingBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        RecipeCommand command = new RecipeCommand(this);
        getCommand("bingo").setExecutor(command);
        getCommand("bingo").setTabCompleter(command);

    }



    public static ThonkCrafting getInstance() {
        return instance;
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

    public static NMSInterface getNmsInterface() {
        return nmsInterface;
    }
}
