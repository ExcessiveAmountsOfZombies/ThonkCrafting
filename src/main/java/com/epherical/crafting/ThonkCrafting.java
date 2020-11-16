package com.epherical.crafting;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.epherical.crafting.commands.RecipeCommand;
import com.epherical.crafting.config.ConfigManager;
import com.epherical.crafting.config.MainConfig;
import com.epherical.crafting.listener.CookingBlockListener;
import com.epherical.crafting.listener.RecipeListener;
import com.epherical.crafting.nms.NMS1_16V3;
import com.epherical.crafting.nms.NMSInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;



public class ThonkCrafting extends JavaPlugin implements Listener {

    private ProtocolManager protocolManager;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static ConfigManager manager;

    private NMSInterface nmsInterface;

    @Override
    public void onLoad() {
        CraftingRegistry.init();
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        try {
            nmsInterface = new NMS1_16V3();
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

        getCommand("bingo").setExecutor(new RecipeCommand(this));

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

    public NMSInterface getNmsInterface() {
        return nmsInterface;
    }
}
