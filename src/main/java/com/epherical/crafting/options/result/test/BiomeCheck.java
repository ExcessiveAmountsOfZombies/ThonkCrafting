package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public class BiomeCheck extends TestOptions {

    private final List<String> biomes;

    public BiomeCheck(String failMessage, List<String> acceptableBiomes) {
        super(failMessage);
        this.biomes = acceptableBiomes;
    }

    public BiomeCheck(NamespacedKey key, JsonObject object) {
        super( object.getAsJsonPrimitive("fail-message").getAsString());
        ArrayList<String> biomes = new ArrayList<>();
        JsonArray array = object.getAsJsonArray("biomes");
        for (JsonElement element : array) {
            biomes.add(element.getAsJsonPrimitive().getAsString().toLowerCase());
        }
        this.biomes = biomes;
    }

    @Override
    public boolean test(OptionContext context) {
        return biomes.contains(context.getPlayer().getLocation().getBlock().getBiome().name().toLowerCase());
    }
}
