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

    public static void saveRecipeToFile(RecipeToJson json) {
        Path path = ThonkCrafting.getInstance().getDataFolder().toPath();

        // Serialize recipe to Json
        JsonObject object = json.createJsonObject();

        String strJson = ThonkCrafting.getInstance().getGson().toJson(object);

        Path resultingPath = path.resolve("recipes/" + json.getKey().getNamespace() + "/" + json.getKey().getKey() + ".json");

        ThonkCrafting.getNmsInterface().overrideRecipe(json.getKey(), object);

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

    public static class RecipeShapedToJson implements RecipeToJson {

        private final MinecraftKey key;
        private final ItemStack result;
        private final String group;
        private final String[] shape;
        private Map<Character, ItemStack> input;
        private boolean vanillaRecipe;

        public RecipeShapedToJson(MinecraftKey key, ItemStack result, String group, String[] shape, Map<Character, ItemStack> input, boolean vanillaRecipe) {
            this.key = key;
            this.result = result;
            this.group = group;
            this.shape = shape;
            this.input = input;
            this.vanillaRecipe = vanillaRecipe;
        }

        public RecipeShapedToJson(NamespacedKey key, org.bukkit.inventory.ItemStack result,
                                  String group, String[] shape, Map<Character, org.bukkit.inventory.ItemStack> input, boolean vanillaRecipe) {
            this(CraftNamespacedKey.toMinecraft(key), CraftItemStack.asNMSCopy(result), group, shape, null, vanillaRecipe);
            Map<Character, ItemStack> nmsMap = new HashMap<>();
            input.forEach((character, itemStack) -> nmsMap.put(character, CraftItemStack.asNMSCopy(itemStack)));
            this.input = nmsMap;
        }

        @Override // Serialize
        public void serializeRecipe(JsonObject object) {
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

                if ((entry.getValue().getTag() != null && !entry.getValue().getTag().isEmpty()) && !vanillaRecipe) {
                    addNBTData(key, entry.getValue().getTag());
                }
                keys.add(String.valueOf(entry.getKey()), key);
            }

            object.add("key", keys);
            JsonObject result = new JsonObject();
            result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
            if ((this.result.getTag() != null && !this.result.getTag().isEmpty()) && !vanillaRecipe) {
                addNBTData(result, this.result.getTag());
            }

            if (this.result.getCount() > 1) {
                result.addProperty("count", this.result.getCount());
            }

            object.add("result", result);
        }

        @Override // getKey
        public MinecraftKey getKey() {
            return key;
        }

        @Override // getRecipeSerializer
        public RecipeSerializer<?> getSerializer() {
            RecipeSerializer<ShapedRecipes> serializer = RecipeSerializer.a;
            return vanillaRecipe ? serializer : CraftingRegistry.SHAPED_SERIALIZER;
        }
    }


    public static class RecipeShapelessToJson implements RecipeToJson {

        private MinecraftKey key;
        private ItemStack result;
        private String group;
        private List<ItemStack> input;
        private boolean vanillaRecipe;

        public RecipeShapelessToJson(MinecraftKey key, ItemStack result, String group, List<ItemStack> input, boolean vanillaRecipe) {
            this.key = key;
            this.result = result;
            this.group = group;
            this.input = input;
            this.vanillaRecipe = vanillaRecipe;
        }

        public RecipeShapelessToJson(NamespacedKey key, org.bukkit.inventory.ItemStack result, String group, Collection<org.bukkit.inventory.ItemStack> input, boolean vanillaRecipe) {
            this(CraftNamespacedKey.toMinecraft(key), CraftItemStack.asNMSCopy(result), group, input.stream().map(CraftItemStack::asNMSCopy).collect(Collectors.toList()), vanillaRecipe);
        }

        @Override
        public void serializeRecipe(JsonObject object) {
            if (!this.group.isEmpty()) {
                object.addProperty("group", group);
            }

            JsonArray ingredients = new JsonArray();

            for (ItemStack itemStack : input) {
                RecipeItemStack recipe = RecipeItemStack.a(Stream.of(itemStack));
                JsonObject key = (JsonObject) recipe.c();

                if ((itemStack.getTag() != null && !itemStack.getTag().isEmpty()) && !vanillaRecipe) {
                    addNBTData(key, itemStack.getTag());
                }
                ingredients.add(key);
            }


            object.add("ingredients", ingredients);
            JsonObject result = new JsonObject();
            result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
            if ((this.result.getTag() != null && !this.result.getTag().isEmpty()) && !vanillaRecipe) {
                addNBTData(result, this.result.getTag());
            }

            if (this.result.getCount() > 1) {
                result.addProperty("count", this.result.getCount());
            }

            object.add("result", result);

        }

        @Override
        public MinecraftKey getKey() {
            return key;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            RecipeSerializer<ShapelessRecipes> serializer = RecipeSerializer.b;
            return vanillaRecipe ? serializer : CraftingRegistry.SHAPELESS_SERIALIZER;
        }
    }

    public static class CookingRecipeToJson implements RecipeToJson {

        private final MinecraftKey key;
        private final String group;
        private final ItemStack input;
        private final ItemStack result;
        private final float experience;
        private final int cookingTime;
        private final RecipeSerializer<?> serializer;
        private boolean vanillaRecipe;


        public CookingRecipeToJson(MinecraftKey key, String group, ItemStack input, ItemStack result, float experience,
                                   int cookingTime, RecipeSerializer<?> serializer, boolean vanillaRecipe) {
            this.key = key;
            this.group = group;
            this.input = input;
            this.result = result;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.serializer = serializer;
            this.vanillaRecipe = vanillaRecipe;
        }


        @Override
        public void serializeRecipe(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            RecipeItemStack itemStack = RecipeItemStack.a(Stream.of(input));


            // TODO: this works as long as there is only 1 choice. otherwise we have to use another solution look at itemStack.c() to see
            JsonObject ingredient = (JsonObject) itemStack.c();
            if ((input.getTag() != null && !input.getTag().isEmpty()) && !vanillaRecipe) {
                addNBTData(ingredient, input.getTag());
            }

            json.add("ingredient", ingredient);
            JsonObject result = new JsonObject();

            if (!vanillaRecipe) {
                result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
                if ((this.result.getTag() != null && !this.result.getTag().isEmpty())) {
                    addNBTData(result, this.result.getTag());
                }

                if (this.result.getCount() > 1) {
                    result.addProperty("count", this.result.getCount());
                }
                json.add("result", result);
            } else {
                json.addProperty("result", IRegistry.ITEM.getKey(this.result.getItem()).toString());
            }
            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        @Override
        public MinecraftKey getKey() {
            return key;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return serializer;
        }
    }

    public static class SingleItemRecipeToJson implements RecipeToJson {
        private final MinecraftKey key;
        private final RecipeSerializer<?> serializer;
        private final String group;
        private final ItemStack input;
        private final ItemStack result;
        private final boolean vanillaRecipe;


        public SingleItemRecipeToJson(MinecraftKey key, RecipeSerializer<?> serializer, String group, ItemStack input, ItemStack result, boolean vanillaRecipe) {
            this.key = key;
            this.serializer = serializer;
            this.group = group;
            this.input = input;
            this.result = result;
            this.vanillaRecipe = vanillaRecipe;
        }

        @Override
        public void serializeRecipe(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            RecipeItemStack itemStack = RecipeItemStack.a(Stream.of(input));


            // TODO: this works as long as there is only 1 choice. otherwise we have to use another solution
            JsonObject ingredient = (JsonObject) itemStack.c();
            if ((input.getTag() != null && !input.getTag().isEmpty()) && !vanillaRecipe) {
                addNBTData(ingredient, input.getTag());
            }

            json.add("ingredient", ingredient);
            JsonObject result = new JsonObject();
            if (!vanillaRecipe) {
                result.addProperty("item", IRegistry.ITEM.getKey(this.result.getItem()).toString());
                if ((this.result.getTag() != null && !this.result.getTag().isEmpty())) {
                    addNBTData(result, this.result.getTag());
                }
                if (this.result.getCount() > 1) {
                    result.addProperty("count", this.result.getCount());
                }
                json.add("result", result);
            } else {
                json.addProperty("result", IRegistry.ITEM.getKey(this.result.getItem()).toString());
                json.addProperty("count", this.result.getCount());
            }
        }

        @Override
        public MinecraftKey getKey() {
            return key;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return serializer;
        }
    }

    public static CookingRecipeToJson createCookingRecipe(NamespacedKey recipeKey, String group, org.bukkit.inventory.ItemStack input,
                                                          org.bukkit.inventory.ItemStack result, float experience, int cookingTime, NamespacedKey recipeSerializerKey) {
        RecipeSerializer<?> serializer = IRegistry.RECIPE_SERIALIZER.get(CraftNamespacedKey.toMinecraft(recipeSerializerKey));
        boolean vanillaRecipe = false;
        if (recipeSerializerKey.getNamespace().startsWith("minecraft")) {
            vanillaRecipe = true;
        }
        return new CookingRecipeToJson(CraftNamespacedKey.toMinecraft(recipeKey), group,
                CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result), experience, cookingTime, serializer, vanillaRecipe);
    }

    public static RecipeShapelessToJson createVanillaShapelessRecipe(NamespacedKey key, org.bukkit.inventory.ItemStack result, int outputAmount,
                                                    String group, Collection<org.bukkit.inventory.ItemStack> ingredients) {
        List<ItemStack> recipeItemStacks = ingredients.stream().map(CraftItemStack::asNMSCopy).collect(Collectors.toList());

        ItemStack nmsResult = CraftItemStack.asNMSCopy(result);
        return new RecipeShapelessToJson(CraftNamespacedKey.toMinecraft(key), nmsResult, group, recipeItemStacks, true);
    }

    public static SingleItemRecipeToJson createVanillaCuttingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input, org.bukkit.inventory.ItemStack output, int outputCount) {
        RecipeSerializer<RecipeStonecutting> serializer = RecipeSerializer.t;
        return new SingleItemRecipeToJson(CraftNamespacedKey.toMinecraft(key), serializer, group,
                CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(output), true);
    }

    public static SingleItemRecipeToJson createCustomCuttingRecipe(NamespacedKey key, String group, org.bukkit.inventory.ItemStack input, org.bukkit.inventory.ItemStack output) {
        RecipeSerializer<InternalRecipeStonecutting> serializer = CraftingRegistry.STONECUTTING_SERIALIZER;
        return new SingleItemRecipeToJson(CraftNamespacedKey.toMinecraft(key), serializer, group, CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(output), false);
    }

}
