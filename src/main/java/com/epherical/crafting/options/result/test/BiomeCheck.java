package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BiomeCheck extends TestOptions {

    private final List<String> biomes;

    public BiomeCheck(NamespacedKey key, JsonObject object) {
        super(key, object);
        ArrayList<String> biomes = new ArrayList<>();
        JsonArray array = JsonUtil.getArrayValue(object, "biomes");
        for (JsonElement element : array) {
            biomes.add(element.getAsJsonPrimitive().getAsString().toLowerCase());
        }
        this.biomes = biomes;
    }

    @Override
    public boolean test(OptionContext context) {
        return biomes.contains(context.getPlayer().getLocation().getBlock().getBiome().name().toLowerCase());
    }

    @Override
    public String toString() {
        return "Can be crafted in biomes " + biomes;
    }

    @Override
    public BaseComponent[] textDisplay() {
        ComponentBuilder builder = new ComponentBuilder("Can be crafted in ").color(ChatColor.GRAY);
        for (String biome : biomes) {
            builder.append(biome.toLowerCase() + " ").color(ChatColor.DARK_GRAY);
        }
        return builder.create();
    }

    @Override
    public void serialize(JsonObject object) {
        super.serialize(object);
        JsonArray array = new JsonArray();
        biomes.forEach(array::add);
        object.add("biomes", array);
    }
}
