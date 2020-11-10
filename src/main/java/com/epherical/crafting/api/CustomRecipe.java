package com.epherical.crafting.api;

import net.minecraft.server.v1_16_R2.IRecipe;
import org.bukkit.Material;

public interface CustomRecipe {

    default IRecipe<?> getVanillaRecipe() {
        return null;
    }
    
    Material getRelevantMaterial();
}
