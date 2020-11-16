package com.epherical.crafting.test.nms;

import com.epherical.crafting.test.Registry;
import com.epherical.crafting.test.api.DamageDurabilityRecipe;
import com.epherical.crafting.test.bukkit.CraftBukkitPermissionRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftRecipe;



public class InternalPermissionRecipe implements RecipeCrafting {
    private final MinecraftKey key;
    private final String group;
    private final ItemStack result;
    private final NonNullList<RecipeItemStack> ingredients;
    private ShapelessRecipes recipes;
    private String permission;
    private String failMessage;

    private int counter = 0;

    public InternalPermissionRecipe(MinecraftKey minecraftkey, String s, ItemStack itemstack, NonNullList<RecipeItemStack> nonnulllist, String permission, String failMessage) {
        this.recipes = new ShapelessRecipes(minecraftkey, s, itemstack, nonnulllist);
        this.key = minecraftkey;
        this.group = s;
        this.result = itemstack;
        this.ingredients = nonnulllist;
        this.permission = permission;
        this.failMessage = failMessage;
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
        counter++;
        System.out.println("This is called every time the item is crafted");

        return defaultedList;
    }

    @Override
    public MinecraftKey getKey() {
        return key;
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return Registry.PERMISSION_SERIALIZER;
    }

    @Override
    public DamageDurabilityRecipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(getResult());
        CraftBukkitPermissionRecipe recipe = new CraftBukkitPermissionRecipe(result, this);
        recipe.setGroup(group);

        for (RecipeItemStack list : ingredients) {
            recipe.addIngredient(CraftRecipe.toBukkit(list));
        }

        return recipe;
    }

    public String getPermission() {
        return permission;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public int getCounter() {
        return counter;
    }

    public static class NMSPermissionRecipeSerializer implements RecipeSerializer<InternalPermissionRecipe> {


        @Override
        public InternalPermissionRecipe a(MinecraftKey minecraftkey, JsonObject jsonObject) {
            String string = ChatDeserializer.a(jsonObject, "group", "");
            NonNullList<RecipeItemStack> defaultedList = getIngredients(ChatDeserializer.u(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (defaultedList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                ItemStack itemStack = ShapedRecipes.a(ChatDeserializer.t(jsonObject, "result"));
                String permission = jsonObject.getAsJsonPrimitive("permission").getAsString();
                String failMessage = jsonObject.getAsJsonPrimitive("fail-message").getAsString();
                return new InternalPermissionRecipe(minecraftkey, string, itemStack, defaultedList, permission, failMessage);
            }
        }

        @Override
        public InternalPermissionRecipe a(MinecraftKey minecraftkey, PacketDataSerializer packetdataserializer) {
            String string = packetdataserializer.readUTF(32767);
            int i = packetdataserializer.readVarInt();
            NonNullList<RecipeItemStack> defaultedList = NonNullList.a(i, RecipeItemStack.a);

            for(int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, RecipeItemStack.b(packetdataserializer));
            }

            String permission = packetdataserializer.readUTF(32767);
            String failMessage = packetdataserializer.readUTF(32767);

            ItemStack itemStack = packetdataserializer.n();
            return new InternalPermissionRecipe(minecraftkey, string, itemStack, defaultedList, permission, failMessage);
        }

        @Override
        public void a(PacketDataSerializer packetData, InternalPermissionRecipe recipe) {
            packetData.a(recipe.group);
            packetData.d(recipe.ingredients.size());

            for (RecipeItemStack ingredient : recipe.ingredients) {
                ingredient.a(packetData);
            }

            packetData.a(recipe.permission);
            packetData.a(recipe.failMessage);

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

