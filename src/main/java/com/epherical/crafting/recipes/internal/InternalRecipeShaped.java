package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.OptionRegister;
import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.impl.RecipeShaped;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class InternalRecipeShaped implements CustomRecipe, RecipeCrafting {
    private ShapedRecipes recipes;
    private String group;
    private ArrayList<Options> options;


    public InternalRecipeShaped(MinecraftKey key, String group, int width, int height, NonNullList<RecipeItemStack> ingredients,
                                ItemStack result, ArrayList<Options> options) {
        this.recipes = new ShapedRecipes(key, group, width, height, ingredients, result);
        this.options = options;
        this.group = group;
    }

    public InternalRecipeShaped(ShapedRecipes recipes, String group, ArrayList<Options> options) {
        this(recipes.getKey(), group, recipes.i(), recipes.j(), recipes.a(), recipes.getResult(), options);
    }

    public ArrayList<Options> getOptions() {
        return options;
    }

    public String getGroup() {
        return group;
    }

    public ShapedRecipes getRecipes() {
        return recipes;
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
        return recipes.getResult();
    }

    @Override
    public MinecraftKey getKey() {
        return recipes.getKey();
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return CraftingRegistry.SHAPED_SERIALIZER;
    }

    @Override
    public RecipeShaped toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(recipes.getResult());
        return new RecipeShaped(result, this);
    }

    private static Map<String, RecipeItemStack> createRecipeMap(JsonObject jsonObject) {
        Map<String, RecipeItemStack> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (( entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            RecipeItemStack stack = (RecipeItemStack) ThonkCrafting.getNmsInterface().createRecipeChoice(entry.getValue());

            map.put(entry.getKey(), stack);
        }

        map.put(" ", RecipeItemStack.a);
        return map;
    }

    private static String[] b(JsonArray jsonarray) {
        String[] astring = new String[jsonarray.size()];
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = ChatDeserializer.a(jsonarray.get(i), "pattern[" + i + "]");
                if (s.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    private static int a(String s) {
        int i;
        for(i = 0; i < s.length() && s.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int b(String s) {
        int i;
        for(i = s.length() - 1; i >= 0 && s.charAt(i) == ' '; --i) {
        }

        return i;
    }

    @VisibleForTesting
    static String[] a(String... astring) {
        int i = 2147483647;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < astring.length; ++i1) {
            String s = astring[i1];
            i = Math.min(i, a(s));
            int j1 = b(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (astring.length == l) {
            return new String[0];
        } else {
            String[] astring1 = new String[astring.length - l - k];

            for(int k1 = 0; k1 < astring1.length; ++k1) {
                astring1[k1] = astring[k1 + k].substring(i, j + 1);
            }

            return astring1;
        }
    }

    private static NonNullList<RecipeItemStack> b(String[] astring, Map<String, RecipeItemStack> map, int i, int j) {
        NonNullList<RecipeItemStack> nonnulllist = NonNullList.a(i * j, RecipeItemStack.a);
        Set<String> set = Sets.newHashSet(map.keySet());
        set.remove(" ");

        for(int k = 0; k < astring.length; ++k) {
            for(int l = 0; l < astring[k].length(); ++l) {
                String s = astring[k].substring(l, l + 1);
                RecipeItemStack recipeitemstack = map.get(s);
                if (recipeitemstack == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(l + i * k, recipeitemstack);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    public static class ShapedSerializer implements RecipeSerializer<InternalRecipeShaped> {

        @Override
        public InternalRecipeShaped a(MinecraftKey minecraftKey, JsonObject jsonObject) {

            Map<String, RecipeItemStack> map = InternalRecipeShaped.createRecipeMap(ChatDeserializer.t(jsonObject, "key"));
            String[] astring = InternalRecipeShaped.a(InternalRecipeShaped.b(ChatDeserializer.u(jsonObject, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<RecipeItemStack> recipeItemStacks = InternalRecipeShaped.b(astring, map, i, j);
            ItemStack itemstack = ShapedRecipes.a(ChatDeserializer.t(jsonObject, "result"));

            String s = ChatDeserializer.a(jsonObject, "group", "");
            ShapedRecipes recipe = RecipeSerializer.a.a(minecraftKey, jsonObject);
            ArrayList<Options> options = OptionRegister.getOptions(jsonObject);
            //new InternalRecipeShaped(minecraftKey, s, i, j, nonnulllist, itemstack, options);
            return new InternalRecipeShaped(recipe, s, options);
        }

        @Override
        public InternalRecipeShaped a(MinecraftKey minecraftKey, PacketDataSerializer packetDataSerializer) {
            ShapedRecipes recipe = RecipeSerializer.a.a(minecraftKey, packetDataSerializer);
            // no group for a packet because the packet has already been parsed out. this method is probably never used to create
            // a recipe anyways
            return new InternalRecipeShaped(recipe, "", new ArrayList<>());
        }

        @Override
        public void a(PacketDataSerializer packetDataSerializer, InternalRecipeShaped internalRecipeShaped) {
            // matching the vanilla shaped recipe serialization.
            // width
            packetDataSerializer.d(internalRecipeShaped.recipes.i());
            // height
            packetDataSerializer.d(internalRecipeShaped.recipes.j());
            packetDataSerializer.a(internalRecipeShaped.group);

            for (RecipeItemStack recipeitemstack : internalRecipeShaped.recipes.a()) {
                recipeitemstack.a(packetDataSerializer);
            }

            packetDataSerializer.a(internalRecipeShaped.recipes.getResult());

        }
    }

    @Override
    public IRecipe<?> getVanillaRecipe() {
        return recipes;
    }

    @Override
    public org.bukkit.Material getRelevantMaterial() {
        return null;
    }
}
