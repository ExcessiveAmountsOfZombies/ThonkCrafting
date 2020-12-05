package com.epherical.crafting.recipes;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.RecipeSerializer;

public interface RecipeToJson {


    void serializeRecipe(JsonObject var1);

    default JsonObject createJsonObject() {
        JsonObject var0 = new JsonObject();
        var0.addProperty("type", IRegistry.RECIPE_SERIALIZER.getKey(this.getSerializer()).toString());
        this.serializeRecipe(var0);
        return var0;
    }

    MinecraftKey getKey();

    RecipeSerializer<?> getSerializer();

}
