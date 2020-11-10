package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.api.CustomRecipe;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R2.*;

public abstract class InternalRecipeSingleItem extends RecipeStonecutting implements CustomRecipe, IRecipe<IInventory> {
    protected final RecipeItemStack ingredient;
    protected final ItemStack result;
    private final Recipes<?> type;
    private final RecipeSerializer<?> serializer;
    protected final MinecraftKey key;
    protected final String group;

    public InternalRecipeSingleItem(Recipes<?> type, RecipeSerializer<?> serializer,
                                    MinecraftKey key, String group,
                                    RecipeItemStack input, ItemStack output) {
        super(key, group, input, output);
        this.type = type;
        this.serializer = serializer;
        this.key = key;
        this.group = group;
        this.ingredient = input;
        this.result = output;
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

    @Override
    public ItemStack a(IInventory iInventory) {
        return this.result.cloneItemStack();
    }

    public static class SingleItemSerializer<T extends InternalRecipeSingleItem> implements RecipeSerializer<T> {
        private final SingleItemSerializer.a<T> instance;

        public SingleItemSerializer(SingleItemSerializer.a<T> instnace) {
            this.instance = instnace;
        }

        @Override
        public T a(MinecraftKey key, JsonObject json) {
            // TODO: make this better
            String var2 = ChatDeserializer.a(json, "group", "");
            RecipeItemStack var3;
            if (ChatDeserializer.d(json, "ingredient")) {
                var3 = RecipeItemStack.a(ChatDeserializer.u(json, "ingredient"));
            } else {
                var3 = RecipeItemStack.a(ChatDeserializer.t(json, "ingredient"));
            }

            String var4 = ChatDeserializer.h(json, "result");
            int var5 = ChatDeserializer.n(json, "count");
            ItemStack var6 = new ItemStack(IRegistry.ITEM.get(new MinecraftKey(var4)), var5);
            return this.instance.create(key, var2, var3, var6);
        }

        @Override
        public T a(MinecraftKey key, PacketDataSerializer packet) {
            // client read?
            String var2 = packet.e(32767);
            RecipeItemStack var3 = RecipeItemStack.b(packet);
            ItemStack var4 = packet.n();
            return this.instance.create(key, var2, var3, var4);
        }

        @Override
        public void a(PacketDataSerializer packet, T recipe) {
            // to send to the client, just copy the default one
            packet.a(recipe.group);
            recipe.ingredient.a(packet);
            packet.a(recipe.result);
        }

        public interface a<T extends InternalRecipeSingleItem> {
            T create(MinecraftKey var1, String var2, RecipeItemStack var3, ItemStack var4);
        }
    }
}
