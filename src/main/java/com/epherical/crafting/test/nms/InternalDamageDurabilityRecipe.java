package com.epherical.crafting.test.nms;

import com.epherical.crafting.test.Registry;
import com.epherical.crafting.test.api.DamageDurabilityRecipe;
import com.epherical.crafting.test.bukkit.CraftBukkitDamageDurability;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;


public class InternalDamageDurabilityRecipe implements RecipeCrafting {
    private final MinecraftKey key;
    private final String group;
    private final ItemStack result;
    private final NonNullList<RecipeItemStack> ingredients;
    private ShapelessRecipes recipes;

    public InternalDamageDurabilityRecipe(MinecraftKey minecraftkey, String s, ItemStack itemstack, NonNullList<RecipeItemStack> nonnulllist) {
        this.recipes = new ShapelessRecipes(minecraftkey, s, itemstack, nonnulllist);
        this.key = minecraftkey;
        this.group = s;
        this.result = itemstack;
        this.ingredients = nonnulllist;
    }

    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
        return recipes.a(inventoryCrafting, world);
    }

    @Override
    public ItemStack a(InventoryCrafting inventoryCrafting) {
        return recipes.a(inventoryCrafting);
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public NonNullList<ItemStack> b(InventoryCrafting inventory) {
        NonNullList<ItemStack> defaultedList = NonNullList.a(inventory.getSize(), ItemStack.NULL_ITEM);

        for(int i = 0; i < defaultedList.size(); ++i) {
            ItemStack itemStack = inventory.getItem(i);
            // itemstack.e() is is a boolean check to see if the item can be damaged.
            if (itemStack.e()) {
                int newDamage = itemStack.getDamage() + 1;
                // h() gets the max durability of the item
                if (newDamage < itemStack.h()) {
                    itemStack = itemStack.cloneItemStack();
                    itemStack.setDamage(newDamage);
                    defaultedList.set(i, itemStack);
                }
            }
        }
        System.out.println("Crafted!!");

        return defaultedList;
    }

    @Override
    public MinecraftKey getKey() {
        return key;
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return Registry.DAMAGE_DURABILITY_SERIALIZER;
    }

    @Override
    public DamageDurabilityRecipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(getResult());
        CraftBukkitDamageDurability recipe = new CraftBukkitDamageDurability(result, this);
        recipe.setGroup(group);

        for (RecipeItemStack list : ingredients) {
            recipe.addIngredient(CraftRecipe.toBukkit(list));
        }

        return recipe;
    }

    public static class NMSDamageDurabilitySerializer implements RecipeSerializer<InternalDamageDurabilityRecipe> {


        @Override
        public InternalDamageDurabilityRecipe a(MinecraftKey minecraftkey, JsonObject jsonObject) {
            String string = ChatDeserializer.a(jsonObject, "group", "");
            NonNullList<RecipeItemStack> defaultedList = getIngredients(ChatDeserializer.u(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (defaultedList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                ItemStack itemStack = ShapedRecipes.a(ChatDeserializer.t(jsonObject, "result"));
                return new InternalDamageDurabilityRecipe(minecraftkey, string, itemStack, defaultedList);
            }
        }

        @Override
        public InternalDamageDurabilityRecipe a(MinecraftKey minecraftkey, PacketDataSerializer packetdataserializer) {
            String string = packetdataserializer.readUTF(32767);
            int i = packetdataserializer.readVarInt();
            NonNullList<RecipeItemStack> defaultedList = NonNullList.a(i, RecipeItemStack.a);

            for(int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, RecipeItemStack.b(packetdataserializer));
            }

            ItemStack itemStack = packetdataserializer.n();
            return new InternalDamageDurabilityRecipe(minecraftkey, string, itemStack, defaultedList);
        }

        @Override
        public void a(PacketDataSerializer packetData, InternalDamageDurabilityRecipe recipe) {
            packetData.a(recipe.group);
            packetData.d(recipe.ingredients.size());

            for (RecipeItemStack ingredient : recipe.ingredients) {
                ingredient.a(packetData);
            }

            packetData.a(recipe.result);
        }


        private static NonNullList<RecipeItemStack> getIngredients(JsonArray json) {
            NonNullList<RecipeItemStack> nonNullList = NonNullList.a();

            for(int i = 0; i < json.size(); ++i) {
                RecipeItemStack ingredient = RecipeItemStack.a(json.get(i));
                // checks to see if the RecipeItemStack has any choices
                if (!ingredient.d()) {
                    nonNullList.add(ingredient);
                }
            }

            return nonNullList;
        }

    }
}
