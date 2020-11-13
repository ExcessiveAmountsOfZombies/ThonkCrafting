package com.epherical.crafting.recipes;

import com.epherical.crafting.options.Options;
import com.epherical.crafting.options.SuccessOptions;
import com.epherical.crafting.options.TestOptions;
import net.minecraft.server.v1_16_R2.IRecipe;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

public interface CustomRecipe {

    default IRecipe<?> getVanillaRecipe() {
        return null;
    }
    
    Material getRelevantMaterial();

    List<Options> getOptions();

    default List<TestOptions> getTestOptions() {
        return getOptions().stream()
                .filter(options1 -> options1 instanceof TestOptions)
                .map(options1 -> (TestOptions) options1).collect(Collectors.toList());
    }

    default List<SuccessOptions> getSuccessOptions() {
        return getOptions().stream()
                .filter(options1 -> options1 instanceof SuccessOptions)
                .map(options1 -> (SuccessOptions) options1).collect(Collectors.toList());
    }

}
