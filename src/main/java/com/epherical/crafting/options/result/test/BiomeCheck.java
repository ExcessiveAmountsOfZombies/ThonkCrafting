package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import org.bukkit.block.Biome;

import java.util.List;

public class BiomeCheck extends TestOptions {

    private final List<Biome> biomes;

    public BiomeCheck(String failMessage, List<Biome> acceptableBiomes) {
        super(failMessage);
        this.biomes = acceptableBiomes;
    }


    @Override
    public boolean test(OptionContext context) {
        return biomes.contains(context.getPlayer().getLocation().getBlock().getBiome());
    }
}
