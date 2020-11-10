package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.api.CustomRecipe;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R2.*;

import java.util.HashMap;
import java.util.Map;

public abstract class InternalRecipeCooking extends RecipeCampfire implements CustomRecipe, IRecipe<IInventory> {
    protected final Recipes<?> recipeType;
    protected final MinecraftKey key;
    protected final String group;
    protected final RecipeItemStack input;
    protected final ItemStack output;
    protected final float experience;
    protected final int cookingTime;
    protected final Map<String, Object> options;

    public InternalRecipeCooking(Recipes<?> recipeType, MinecraftKey key, String group, RecipeItemStack input,
                                 ItemStack result, float exp, int cookTime, Map<String, Object> options) {
        super(key, group, input, result, exp, cookTime);
        this.recipeType = recipeType;
        this.key = key;
        this.group = group;
        this.input = input;
        this.output = result;
        this.experience = exp;
        this.cookingTime = cookTime;
        this.options = options;
    }

    public InternalRecipeCooking(RecipeCooking cooking, String group, Map<String, Object> options) {
        this(cooking.g(), cooking.getKey(), group, cooking.a().get(0), cooking.getResult(),
                cooking.getExperience(), cooking.getCookingTime(), options);
    }

    @Override
    public boolean a(IInventory inventory, World world) {
        return this.input.test(inventory.getItem(0));
    }

    @Override
    public ItemStack a(IInventory iInventory) {
        return this.output.cloneItemStack();
    }

    public NonNullList<RecipeItemStack> a() {
        NonNullList<RecipeItemStack> var0 = NonNullList.a();
        var0.add(this.input);
        return var0;
    }

    public float getExperience() {
        return this.experience;
    }

    public ItemStack getResult() {
        return this.output;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public MinecraftKey getKey() {
        return this.key;
    }

    public Recipes<?> g() {
        return this.recipeType;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public static class CookingSerializer<T extends InternalRecipeCooking> implements RecipeSerializer<T> {
        private final CookingSerializer.Instance<T> instance;

        public CookingSerializer(CookingSerializer.Instance<T> instance) {
            this.instance = instance;
        }

        @Override
        public T a(MinecraftKey minecraftKey, JsonObject jsonObject) {
            Map<String, Object> map = new HashMap<>();
            String group = ChatDeserializer.a(jsonObject, "group", "");
            RecipeCooking cooking = RecipeSerializerCooking.s.a(minecraftKey, jsonObject);
            JsonObject object = jsonObject.getAsJsonObject("options");
            map.put("permission", object.getAsJsonPrimitive("permission").getAsString());
            return this.instance.create(minecraftKey, group, cooking.a().get(0), cooking.getResult(), cooking.getExperience(), cooking.getCookingTime(), map);
        }

        @Override
        public T a(MinecraftKey minecraftKey, PacketDataSerializer packetDataSerializer) {
            // Not sure when this is used, probably a client method that's on the server?
            return null;
        }

        @Override
        public void a(PacketDataSerializer packetDataSerializer, T instance) {
            // Writing the recipe to the client
            packetDataSerializer.a(instance.group);
            instance.input.a(packetDataSerializer);
            packetDataSerializer.a(instance.output);
            packetDataSerializer.writeFloat(instance.experience);
            packetDataSerializer.d(instance.cookingTime);
        }


        public interface Instance<T extends InternalRecipeCooking> {
            T create(MinecraftKey key, String group, RecipeItemStack input, ItemStack output, float experience, int cookingTime, Map<String, Object> options);
        }
    }
}
