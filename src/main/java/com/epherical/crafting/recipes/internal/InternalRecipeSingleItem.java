package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.OptionRegister;
import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R3.*;

import java.util.ArrayList;

public abstract class InternalRecipeSingleItem extends RecipeStonecutting implements CustomRecipe, IRecipe<IInventory> {
    protected final RecipeItemStack ingredient;
    protected final ItemStack result;
    private final Recipes<?> type;
    private final RecipeSerializer<?> serializer;
    protected final MinecraftKey key;
    protected final String group;
    protected final ArrayList<Options> options;

    public InternalRecipeSingleItem(Recipes<?> type, RecipeSerializer<?> serializer,
                                    MinecraftKey key, String group,
                                    RecipeItemStack input, ItemStack output, ArrayList<Options> options) {
        super(key, group, input, output);
        this.type = type;
        this.serializer = serializer;
        this.key = key;
        this.group = group;
        this.ingredient = input;
        this.result = output;
        this.options = options;
    }

    @Override
    public Recipes<?> g() {
        return type;
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return serializer;
    }

    @Override
    public MinecraftKey getKey() {
        return key;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public NonNullList<RecipeItemStack> a() {
        NonNullList<RecipeItemStack> var0 = NonNullList.a();
        var0.add(this.ingredient);
        return var0;
    }


    public ArrayList<Options> getOptions() {
        return options;
    }

    @Override
    public ItemStack a(IInventory iInventory) {
        return this.result.cloneItemStack();
    }

    public static class SingleItemSerializer<T extends InternalRecipeSingleItem> implements RecipeSerializer<T> {
        private final SingleItemSerializer.a<T> instance;

        public SingleItemSerializer(SingleItemSerializer.a<T> instance) {
            this.instance = instance;
        }

        @Override
        public T a(MinecraftKey key, JsonObject json) {
            String group = ChatDeserializer.a(json, "group", "");
            RecipeItemStack input;
            if (ChatDeserializer.d(json, "ingredient")) {
                // This might be a problem because it expects a JSONArray, so if someone actually tries to use a JSON array it'll probably error
                input = (RecipeItemStack) ThonkCrafting.getNmsInterface().createRecipeItemStack(ChatDeserializer.u(json, "ingredient"));
            } else {
                // ChatDeserializer.t is the one used in other serializers so this is the one that'll probably be called more often.
                input = (RecipeItemStack) ThonkCrafting.getNmsInterface().createRecipeItemStack(ChatDeserializer.t(json, "ingredient"));
            }

            ItemStack result = (ItemStack) ThonkCrafting.getNmsInterface().createNMSItemStack(ChatDeserializer.t(json, "result"));
            ArrayList<Options> options = OptionRegister.getOptions(json);
            return this.instance.create(key, group, input, result, options);
        }

        @Override
        public T a(MinecraftKey key, PacketDataSerializer packet) {
            // client read?
            String var2 = packet.e(32767);
            RecipeItemStack var3 = RecipeItemStack.b(packet);
            ItemStack var4 = packet.n();
            return this.instance.create(key, var2, var3, var4, new ArrayList<>());
        }

        @Override
        public void a(PacketDataSerializer packet, T recipe) {
            // to send to the client, just copy the default one
            packet.a(recipe.group);
            recipe.ingredient.a(packet);
            packet.a(recipe.result);
        }

        public interface a<T extends InternalRecipeSingleItem> {
            T create(MinecraftKey var1, String var2, RecipeItemStack var3, ItemStack var4, ArrayList<Options> options);
        }
    }
}
