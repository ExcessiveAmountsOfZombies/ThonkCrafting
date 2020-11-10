package com.epherical.crafting.test;

import com.epherical.crafting.test.nms.InternalDamageDurabilityRecipe;
import com.epherical.crafting.test.nms.InternalPermissionRecipe;
import net.minecraft.server.v1_16_R2.MinecraftKey;
import net.minecraft.server.v1_16_R2.RegistryMaterials;

public class Registry {


    private static boolean initialized = false;

    public static InternalDamageDurabilityRecipe.NMSDamageDurabilitySerializer DAMAGE_DURABILITY_SERIALIZER;
    public static InternalPermissionRecipe.NMSPermissionRecipeSerializer PERMISSION_SERIALIZER;


    public static void init() {
        // We only need to register this once, if it gets registered multiple times players won't be able to connect to the server
        MinecraftKey durability = new MinecraftKey("thonkcrafting", "damage_durability");
        MinecraftKey permission = new MinecraftKey("thonkcrafting", "permission_recipe");
        DAMAGE_DURABILITY_SERIALIZER = (InternalDamageDurabilityRecipe.NMSDamageDurabilitySerializer) RegistryMaterials.RECIPE_SERIALIZER.get(durability);
        if (DAMAGE_DURABILITY_SERIALIZER == null) {
            DAMAGE_DURABILITY_SERIALIZER = RegistryMaterials.a(RegistryMaterials.RECIPE_SERIALIZER, new MinecraftKey("thonkcrafting",
                    "damage_durability"), new InternalDamageDurabilityRecipe.NMSDamageDurabilitySerializer());
        }

        PERMISSION_SERIALIZER = (InternalPermissionRecipe.NMSPermissionRecipeSerializer) RegistryMaterials.RECIPE_SERIALIZER.get(permission);
        if (PERMISSION_SERIALIZER == null) {
            PERMISSION_SERIALIZER = RegistryMaterials.a(RegistryMaterials.RECIPE_SERIALIZER, new MinecraftKey("thonkcrafting",
                    "permission_recipe"), new InternalPermissionRecipe.NMSPermissionRecipeSerializer());
        }
    }
}
