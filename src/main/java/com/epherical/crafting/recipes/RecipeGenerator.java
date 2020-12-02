package com.epherical.crafting.recipes;


import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.recipes.internal.InternalRecipeStonecutting;
import com.epherical.crafting.recipes.nbt.JsonToNBT;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeGenerator {

    public static void saveRecipeToFile(jf json) {
        Path path = ThonkCrafting.getInstance().getDataFolder().toPath();

        // Serialize recipe to Json
        JsonObject object = json.a();

        String strJson = ThonkCrafting.getInstance().getGson().toJson(object);

        Path resultingPath = path.resolve("recipes/" + json.b().getNamespace() + "/" + json.b().getKey() + ".json");

        ThonkCrafting.getNmsInterface().overrideRecipe(json.b(), object);

        try (BufferedWriter writer = Files.newBufferedWriter(resultingPath)) {
            writer.write(strJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addNBTData(JsonObject base, NBTBase nbtData) {
        JsonElement element = Dynamic.convert(DynamicOpsNBT.a, JsonToNBT.INSTANCE, nbtData);
        JsonObject tag = new JsonObject();
        tag.add("tag", element);
        base.add("data", tag);
    }

    public static class RecipeShapedToJson implements jf {

        private final MinecraftKey key;
        private final ItemStack result;
        private final String group;
        private final String[] shape;
        private Map<Character, ItemStack> input;

        public RecipeShapedToJson(MinecraftKey key, ItemStack result, String group, String[] shape, Map<Character, ItemStack> input) {
            this.key = key;
            this.result = result;
            this.group = group;
            this.shape = shape;
            this.input = input;
        }

        public RecipeShapedToJson(NamespacedKey key, org.bukkit.inventory.ItemStack result,
                                  String group, String[] shape, Map<Character, org.bukkit.inventory.ItemStack> input) {
            this(CraftNamespacedKey.toMinecraft(key), CraftItemStack.asNMSCopy(result), group, shape, null);
            Map<Character, ItemStack> nmsMap = new HashMap<>();
            input.forEach((character, itemStack) -> nmsMap.put(character, CraftItemStack.asNMSCopy(itemStack)));
            this.input = nmsMap;
        }

        @Override // Serialize
        public void a(JsonObject object) {
            if (!group.isEmpty()) {
                object.addProperty("group", group);
            }

            JsonArray pattern = new JsonArray();

            for (String s : shape) {
                pattern.add(s);
            }

            object.add("pattern", pattern);
            JsonObject keys = new JsonObject();

            for (Map.Entry<Character, ItemStack> entry : this.input.entrySet()) {
                RecipeItemStack itemStack = RecipeItemStack.a(Stream.of(entry.getValue()));
                JsonObject key = (JsonObject) itemStack.c();

                if (entry.getValue().getTag() != null && !entry.getValue().getTag().isEmpty()) {
                    addNBTData(key, entry.getValue().getTag());
                }
                keys.add(String.valueOf(entry.getKey()), key);
            }

            object.add("key", keys);
            JsonObject result = new JsonObject();
            result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
            if (this.result.getTag() != null && !this.result.getTag().isEmpty()) {
                addNBTData(result, this.result.getTag());
            }

            if (this.result.getCount() > 1) {
                result.addProperty("count", this.result.getCount());
            }

            object.add("result", result);
        }

        @Override // getKey
        public MinecraftKey b() {
            return key;
        }

        @Override // getRecipeSerializer
        public RecipeSerializer<?> c() {
            return CraftingRegistry.SHAPED_SERIALIZER;
        }

        @Override // Advancement Json
        public JsonObject d() {
            return null;
        }

        @Override // Advancement Key
        public MinecraftKey e() {
            return null;
        }
    }


    public static class RecipeShapelessToJson implements jf {

        private MinecraftKey key;
        private ItemStack result;
        private String group;
        private List<ItemStack> input;

        public RecipeShapelessToJson(MinecraftKey key, ItemStack result, String group, List<ItemStack> input) {
            this.key = key;
            this.result = result;
            this.group = group;
            this.input = input;
        }

        public RecipeShapelessToJson(NamespacedKey key, org.bukkit.inventory.ItemStack result, String group, Collection<org.bukkit.inventory.ItemStack> input) {
            this(CraftNamespacedKey.toMinecraft(key), CraftItemStack.asNMSCopy(result), group, input.stream().map(CraftItemStack::asNMSCopy).collect(Collectors.toList()));
        }

        @Override
        public void a(JsonObject object) {
            if (!this.group.isEmpty()) {
                object.addProperty("group", group);
            }

            JsonArray ingredients = new JsonArray();

            for (ItemStack itemStack : input) {
                RecipeItemStack recipe = RecipeItemStack.a(Stream.of(itemStack));
                JsonObject key = (JsonObject) recipe.c();

                if (itemStack.getTag() != null && !itemStack.getTag().isEmpty()) {
                    addNBTData(key, itemStack.getTag());
                }
                ingredients.add(key);
            }


            object.add("ingredients", ingredients);
            JsonObject result = new JsonObject();
            result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
            if (this.result.getTag() != null && !this.result.getTag().isEmpty()) {
                addNBTData(result, this.result.getTag());
            }

            if (this.result.getCount() > 1) {
                result.addProperty("count", this.result.getCount());
            }

            object.add("result", result);

        }

        @Override
        public MinecraftKey b() {
            return key;
        }

        @Override
        public RecipeSerializer<?> c() {
            return CraftingRegistry.SHAPELESS_SERIALIZER;
        }

        @Override
        public JsonObject d() {
            return null;
        }

        @Override
        public MinecraftKey e() {
            return null;
        }
    }

    public static class CookingRecipeToJson implements jf {

        private final MinecraftKey key;
        private final String group;
        private final ItemStack input;
        private final ItemStack result;
        private final float experience;
        private final int cookingTime;
        private final RecipeSerializer<?> serializer;


        public CookingRecipeToJson(MinecraftKey key, String group, ItemStack input, ItemStack result, float experience,
                                   int cookingTime, RecipeSerializer<?> serializer) {
            this.key = key;
            this.group = group;
            this.input = input;
            this.result = result;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.serializer = serializer;
        }


        @Override
        public void a(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            RecipeItemStack itemStack = RecipeItemStack.a(Stream.of(input));


            // TODO: this works as long as there is only 1 choice. otherwise we have to use another solution look at itemStack.c() to see
            JsonObject ingredient = (JsonObject) itemStack.c();
            if (input.getTag() != null && !input.getTag().isEmpty()) {
                addNBTData(ingredient, input.getTag());
            }

            json.add("ingredient", ingredient);
            JsonObject result = new JsonObject();
            result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
            if (this.result.getTag() != null && !this.result.getTag().isEmpty()) {
                addNBTData(result, this.result.getTag());
            }

            if (this.result.getCount() > 1) {
                result.addProperty("count", this.result.getCount());
            }

            json.add("result", result);
            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        @Override
        public MinecraftKey b() {
            return key;
        }

        @Override
        public RecipeSerializer<?> c() {
            return serializer;
        }

        @Override
        public JsonObject d() {
            return null;
        }

        @Override
        public MinecraftKey e() {
            return null;
        }
    }

    public static class SingleItemRecipeToJson implements jf {
        private final MinecraftKey key;
        private final RecipeSerializer<?> serializer;
        private final String group;
        private final ItemStack input;
        private final ItemStack result;


        public SingleItemRecipeToJson(MinecraftKey key, RecipeSerializer<?> serializer, String group, ItemStack input, ItemStack result) {
            this.key = key;
            this.serializer = serializer;
            this.group = group;
            this.input = input;
            this.result = result;
        }

        @Override
        public void a(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            RecipeItemStack itemStack = RecipeItemStack.a(Stream.of(input));


            // TODO: this works as long as there is only 1 choice. otherwise we have to use another solution
            JsonObject ingredient = (JsonObject) itemStack.c();
            if (input.getTag() != null && !input.getTag().isEmpty()) {
                addNBTData(ingredient, input.getTag());
            }

            json.add("ingredient", ingredient);
            JsonObject result = new JsonObject();
            result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
            if (this.result.getTag() != null && !this.result.getTag().isEmpty()) {
                addNBTData(result, this.result.getTag());
            }

            if (this.result.getCount() > 1) {
                result.addProperty("count", this.result.getCount());
            }

            json.add("result", result);
        }

        @Override
        public MinecraftKey b() {
            return key;
        }

        @Override
        public RecipeSerializer<?> c() {
            return serializer;
        }

        @Override
        public JsonObject d() {
            return null;
        }

        @Override
        public MinecraftKey e() {
            return null;
        }
    }

    public static CookingRecipeToJson createCustomCampfireRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                                 org.bukkit.inventory.ItemStack result, float experience, int cookingTime) {
        return new CookingRecipeToJson(CraftNamespacedKey.toMinecraft(key), group,
                CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result), experience, cookingTime, CraftingRegistry.CAMPFIRE_SERIALIZER);
    }

    public static CookingRecipeToJson createCustomSmokingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                                org.bukkit.inventory.ItemStack result, float experience, int cookingTime) {
        return new CookingRecipeToJson(CraftNamespacedKey.toMinecraft(key), group,
                CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result), experience, cookingTime, CraftingRegistry.SMOKING_SERIALIZER);
    }

    public static CookingRecipeToJson createCustomSmeltingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                                 org.bukkit.inventory.ItemStack result, float experience, int cookingTime) {
        return new CookingRecipeToJson(CraftNamespacedKey.toMinecraft(key), group,
                CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result), experience, cookingTime, CraftingRegistry.SMELTING_SERIALIZER);
    }

    public static CookingRecipeToJson createCustomBlastingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                                 org.bukkit.inventory.ItemStack result, float experience, int cookingTime) {
        return new CookingRecipeToJson(CraftNamespacedKey.toMinecraft(key), group,
                CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result), experience, cookingTime, CraftingRegistry.BLASTING_SERIALIZER);
    }

    public static jj.a createVanillaCampfireRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                   org.bukkit.inventory.ItemStack result, float experience, int cookTime) {

        RecipeItemStack recipeItemStack = RecipeItemStack.a(Stream.of(CraftItemStack.asNMSCopy(input)));
        ItemStack nmsResult = CraftItemStack.asNMSCopy(result);
        // Going to make it a variable to if the serializer changes it'll break here potentially
        RecipeSerializerCooking<RecipeCampfire> serializer = RecipeSerializer.s;
        return new jj.a(CraftNamespacedKey.toMinecraft(key), group, recipeItemStack, nmsResult.getItem(), experience, cookTime, null, null, serializer);
    }

    public static jj.a createVanillaSmokingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                  org.bukkit.inventory.ItemStack result, float experience, int cookTime) {

        RecipeItemStack recipeItemStack = RecipeItemStack.a(Stream.of(CraftItemStack.asNMSCopy(input)));
        ItemStack nmsResult = CraftItemStack.asNMSCopy(result);
        RecipeSerializerCooking<RecipeSmoking> serializer = RecipeSerializer.r;
        return new jj.a(CraftNamespacedKey.toMinecraft(key), group, recipeItemStack, nmsResult.getItem(), experience, cookTime, null, null, serializer);
    }

    public static jj.a createVanillaSmeltingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                   org.bukkit.inventory.ItemStack result, float experience, int cookTime) {

        RecipeItemStack recipeItemStack = RecipeItemStack.a(Stream.of(CraftItemStack.asNMSCopy(input)));
        ItemStack nmsResult = CraftItemStack.asNMSCopy(result);
        RecipeSerializerCooking<FurnaceRecipe> serializer = RecipeSerializer.p;
        return new jj.a(CraftNamespacedKey.toMinecraft(key), group, recipeItemStack, nmsResult.getItem(), experience, cookTime, null, null, serializer);
    }

    public static jj.a createVanillaBlastingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input,
                                                   org.bukkit.inventory.ItemStack result, float experience, int cookTime) {

        RecipeItemStack recipeItemStack = RecipeItemStack.a(Stream.of(CraftItemStack.asNMSCopy(input)));
        ItemStack nmsResult = CraftItemStack.asNMSCopy(result);
        RecipeSerializerCooking<RecipeBlasting> serializer = RecipeSerializer.q;
        return new jj.a(CraftNamespacedKey.toMinecraft(key), group, recipeItemStack, nmsResult.getItem(), experience, cookTime, null, null, serializer);
    }

    // Not a public class :( TODO: need to reflect to get our own jh.a object.
    /*public static jh.a createVanillaShapedRecipe(NamespacedKey key, org.bukkit.inventory.ItemStack output, int outputAmount,
                                                 String group, String[] shape, Map<Character, org.bukkit.inventory.ItemStack> ingredients) {
    }*/

    public static ji.a createVanillaShapelessRecipe(NamespacedKey key, org.bukkit.inventory.ItemStack result, int outputAmount,
                                                    String group, Collection<org.bukkit.inventory.ItemStack> ingredients) {
        List<RecipeItemStack> recipeItemStacks = ingredients.stream().map(itemStack -> {
            return RecipeItemStack.a(Stream.of(CraftItemStack.asNMSCopy(itemStack)));
        }).collect(Collectors.toList());

        ItemStack nmsResult = CraftItemStack.asNMSCopy(result);
        return new ji.a(CraftNamespacedKey.toMinecraft(key), nmsResult.getItem(), outputAmount, group, recipeItemStacks, null, null);
    }

    public static jk.a createVanillaCuttingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input, org.bukkit.inventory.ItemStack output, int outputCount) {
        RecipeSerializer<RecipeStonecutting> serializer = RecipeSerializer.t;
        return new jk.a(CraftNamespacedKey.toMinecraft(key), serializer, group,
                RecipeItemStack.a(Stream.of(CraftItemStack.asNMSCopy(input))), CraftItemStack.asNMSCopy(output).getItem(), outputCount, null, null);
    }

    public static SingleItemRecipeToJson createCustomCuttingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input, org.bukkit.inventory.ItemStack output) {
        RecipeSerializer<InternalRecipeStonecutting> serializer = CraftingRegistry.STONECUTTING_SERIALIZER;
        return new SingleItemRecipeToJson(CraftNamespacedKey.toMinecraft(key), serializer, group, CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(output));
    }

}
