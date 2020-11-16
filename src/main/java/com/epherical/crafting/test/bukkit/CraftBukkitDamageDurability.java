package com.epherical.crafting.test.bukkit;

import com.epherical.crafting.test.api.DamageDurabilityRecipe;
import com.epherical.crafting.test.nms.InternalDamageDurabilityRecipe;
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

public class CraftBukkitDamageDurability extends DamageDurabilityRecipe implements CraftRecipe {
    private InternalDamageDurabilityRecipe recipe;


    public CraftBukkitDamageDurability(NamespacedKey key, ItemStack result) {
        super(key, result);
    }

    public CraftBukkitDamageDurability(ItemStack result, InternalDamageDurabilityRecipe recipe) {
        this(CraftNamespacedKey.fromMinecraft(recipe.getKey()), result);
        this.recipe = recipe;
    }

    @Override
    public void addToCraftingManager() {
        List<RecipeChoice> ingred = this.getChoiceList();
        NonNullList<RecipeItemStack> data = NonNullList.a(ingred.size(), RecipeItemStack.a);

        for(int i = 0; i < ingred.size(); ++i) {
            data.set(i, this.toNMS(ingred.get(i), true));
        }

        MinecraftServer.getServer().getCraftingManager().addRecipe(new InternalDamageDurabilityRecipe(CraftNamespacedKey.toMinecraft(this.getKey()), this.getGroup(), CraftItemStack.asNMSCopy(this.getResult()), data));
    }
}
