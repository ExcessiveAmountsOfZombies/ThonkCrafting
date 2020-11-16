package com.epherical.crafting.test.bukkit;

import com.epherical.crafting.test.api.PermissionRecipe;
import com.epherical.crafting.test.nms.InternalPermissionRecipe;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NonNullList;
import net.minecraft.server.v1_16_R3.RecipeItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.List;

public class CraftBukkitPermissionRecipe extends PermissionRecipe implements CraftRecipe {

    private InternalPermissionRecipe recipe;


    public CraftBukkitPermissionRecipe(NamespacedKey key, ItemStack result, String permission, String failMessage, int counter) {
        super(key, result, permission, failMessage, counter);
    }

    public CraftBukkitPermissionRecipe(ItemStack result, InternalPermissionRecipe recipe) {
        this(CraftNamespacedKey.fromMinecraft(recipe.getKey()), result, recipe.getPermission(), recipe.getFailMessage(), recipe.getCounter());
        this.recipe = recipe;
    }

    @Override
    public void addToCraftingManager() {
        List<RecipeChoice> ingred = this.getChoiceList();
        NonNullList<RecipeItemStack> data = NonNullList.a(ingred.size(), RecipeItemStack.a);

        for(int i = 0; i < ingred.size(); ++i) {
            data.set(i, this.toNMS(ingred.get(i), true));
        }

        MinecraftServer.getServer().getCraftingManager().addRecipe(new InternalPermissionRecipe(CraftNamespacedKey.toMinecraft(this.getKey()), this.getGroup(), CraftItemStack.asNMSCopy(this.getResult()), data, this.getPermission(), this.getFailMessage()));
    }
}
