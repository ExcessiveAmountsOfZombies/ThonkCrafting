package com.epherical.crafting.recipes;

import com.epherical.crafting.options.Options;
import com.epherical.crafting.options.SuccessOptions;
import com.epherical.crafting.options.TestOptions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_16_R3.IRecipe;
import org.bukkit.ChatColor;
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

    default List<String> getOptionLore() {
        return getOptions().stream().map(options -> ChatColor.GRAY + options.toString()).collect(Collectors.toList());
    }

    default List<BaseComponent[]> getOptionComponent() {
        return getOptions().stream().map(Options::textDisplay).collect(Collectors.toList());
    }

}
