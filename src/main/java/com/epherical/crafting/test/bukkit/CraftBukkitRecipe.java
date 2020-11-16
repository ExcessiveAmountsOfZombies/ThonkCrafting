package com.epherical.crafting.test.bukkit;

import com.epherical.crafting.test.api.UpdatedBukkitRecipe;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NonNullList;
import net.minecraft.server.v1_16_R3.RecipeItemStack;
import net.minecraft.server.v1_16_R3.ShapedRecipes;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.Map;

public class CraftBukkitRecipe extends UpdatedBukkitRecipe implements CraftRecipe {


    public CraftBukkitRecipe(NamespacedKey key, ItemStack result, String permission) {
        super(key, result, permission);
    }

    @Override
    public void addToCraftingManager() {
        String[] shape = this.getShape();
        Map<Character, RecipeChoice> ingred = this.getChoiceMap();
        int width = shape[0].length();
        NonNullList<RecipeItemStack> data = NonNullList.a(shape.length * width, RecipeItemStack.a);

        for(int i = 0; i < shape.length; ++i) {
            String row = shape[i];

            for(int j = 0; j < row.length(); ++j) {
                data.set(i * width + j, this.toNMS(ingred.get(row.charAt(j)), false));
            }
        }

        MinecraftServer.getServer().getCraftingManager().addRecipe(new ShapedRecipes(CraftNamespacedKey.toMinecraft(this.getKey()), this.getGroup(), width, shape.length, data, CraftItemStack.asNMSCopy(this.getResult())));
    }

}
