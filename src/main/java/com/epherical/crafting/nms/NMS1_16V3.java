package com.epherical.crafting.nms;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.logging.Log;
import com.epherical.crafting.recipes.nbt.JsonToNBT;
import com.google.gson.*;

import com.mojang.serialization.Dynamic;
import net.minecraft.server.v1_16_R3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class NMS1_16V3 implements NMSInterface {

    private static final Logger LOG_MANAGER = LogManager.getLogger();

    private ThonkCrafting thonkCrafting;
    private Class<?> craftNamespacedKey;
    private Method toMinecraftKey;



    public NMS1_16V3(ThonkCrafting thonkCrafting) throws ClassNotFoundException, NoSuchMethodException {
        this.thonkCrafting = thonkCrafting;
        this.craftNamespacedKey = Class.forName("org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey");
        this.toMinecraftKey = craftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class);
    }

    @Override
    public Object getMinecraftKey(NamespacedKey key) {
        try {
            return toMinecraftKey.invoke(craftNamespacedKey, key);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void overrideRecipe(Object minecraftKey, JsonObject object) {
        Map<NamespacedKey, JsonObject> map = new HashMap<>();
        map.put(CraftNamespacedKey.fromMinecraft((MinecraftKey) minecraftKey), object);
        registerRecipes(map);
    }

    @Override
    public void registerRecipes(Map<NamespacedKey, JsonObject> recipes) {
        try {
            Class<?> craftingClass = Class.forName("net.minecraft.server.v1_16_R3.CraftingManager");
            Class<?> minecraftKey = Class.forName("net.minecraft.server.v1_16_R3.MinecraftKey");
            Class<?> serverClass = Class.forName("net.minecraft.server.v1_16_R3.MinecraftServer");
            Class<?> iRecipe = Class.forName("net.minecraft.server.v1_16_R3.IRecipe");
            // MinecraftServer object
            Object minecraftServer = serverClass.getMethod("getServer").invoke(serverClass);
            Object craftManagerClass = null;
            Method addRecipe = null;
            if (minecraftServer != null) {
                Method craftingMethod = minecraftServer.getClass().getMethod("getCraftingManager");
                craftManagerClass = craftingMethod.invoke(minecraftServer);
                if (craftManagerClass != null) {
                    addRecipe = craftManagerClass.getClass().getMethod("addRecipe", iRecipe);
                }
            }

            if (addRecipe == null) {
                LOG_MANAGER.error("Could not find NMS method to add recipe, aborting recipe initialization.");
                return;
            }

            Method deserializeRecipe = craftingClass.getDeclaredMethod("a", minecraftKey, JsonObject.class);
            for (Map.Entry<NamespacedKey, JsonObject> entry : recipes.entrySet()) {
                // IRecipe<?>
                try {
                    Object recipe = deserializeRecipe.invoke(craftingClass, getMinecraftKey(entry.getKey()), entry.getValue());
                    if (recipe != null) {
                        Bukkit.getServer().removeRecipe(entry.getKey());
                        addRecipe.invoke(craftManagerClass, recipe);
                    }
                } catch (InvocationTargetException ex) {
                    Log.error("Could not parse the recipe {} {}", ex, entry.getKey(), ex.getCause());
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOG_MANAGER.error("Could not reflect into NMS", e);
        }
    }


    public Recipe getCookingRecipeFromIngredient(TileState block, World world) {
        // TODO: reflection or somehow get API this is bad

        CraftingManager manager = MinecraftServer.getServer().getCraftingManager();
        IRecipe<?> recipes = null;
        /*if (block instanceof Campfire) {
            CraftBlockEntityState<TileEntityCampfire> tile = (CraftBlockEntityState<TileEntityCampfire>) block;
            recipes = manager.craft(Recipes.CAMPFIRE_COOKING, tile.getTileEntity()., ((CraftWorld) world).getHandle()).orElse(null);
        }*/ if (block instanceof BlastFurnace) {
            CraftBlockEntityState<TileEntityBlastFurnace> tile = (CraftBlockEntityState<TileEntityBlastFurnace>) block;
            recipes = manager.craft(Recipes.BLASTING, tile.getTileEntity(), ((CraftWorld) world).getHandle()).orElse(null);
        } else if (block instanceof Smoker) {
            CraftBlockEntityState<TileEntitySmoker> tile = (CraftBlockEntityState<TileEntitySmoker>) block;
            recipes = manager.craft(Recipes.SMOKING, tile.getTileEntity(), ((CraftWorld) world).getHandle()).orElse(null);
        } else if (block instanceof Furnace) {
            CraftBlockEntityState<TileEntityFurnaceFurnace> tile = (CraftBlockEntityState<TileEntityFurnaceFurnace>) block;
            recipes = manager.craft(Recipes.SMELTING, tile.getTileEntity(), ((CraftWorld) world).getHandle()).orElse(null);
        }

        return recipes != null ? recipes.toBukkitRecipe() : null;
    }

    public ArrayList<NamespacedKey> getRecipeKeys() {
        // TODO: reflection i guess lazy
        ArrayList<NamespacedKey> keys = new ArrayList<>();
        MinecraftServer.getServer().getCraftingManager().b().forEach(iRecipe -> {
            keys.add(CraftNamespacedKey.fromMinecraft(iRecipe.getKey()));
        });
        return keys;
    }

    @Override // RecipeItemStack Replicates  public static RecipeItemStack a(@Nullable JsonElement jsonelement)
    public Object createRecipeItemStack(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            if (jsonElement.isJsonObject()) {
                if (jsonElement.getAsJsonObject().has("data")) {
                    return constructRecipeItemStack(Stream.of(createRecipeItemStackProvider(jsonElement.getAsJsonObject())), true);
                }
                // todo maybe not true all the time, we'll leave it here for now
                return constructRecipeItemStack(Stream.of(createRecipeItemStackProvider(jsonElement.getAsJsonObject())), true);
            } else if (jsonElement.isJsonArray()) {
                JsonArray jsonarray = jsonElement.getAsJsonArray();
                if (jsonarray.size() == 0) {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                } else {
                    return constructRecipeItemStack(StreamSupport.stream(jsonarray.spliterator(), false).map((element) -> createRecipeItemStackProvider(ChatDeserializer.m(element, "item"))), true);
                }
            } else {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    @Override // RecipeItemStack.Provider
    public Object createRecipeItemStackProvider(JsonObject object) {
        if (object.has("item") && object.has("tag")) {
            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        } else {
            MinecraftKey minecraftkey;
            if (object.has("item")) {
                ItemStack item = (ItemStack) createNMSItemStack(object);
                return new RecipeItemStack.StackProvider(item);
            } else if (object.has("tag")) {
                minecraftkey = new MinecraftKey(ChatDeserializer.h(object, "tag"));
                Tag<Item> tag = TagsInstance.a().getItemTags().a(minecraftkey);
                if (tag == null) {
                    throw new JsonSyntaxException("Unknown item tag '" + minecraftkey + "'");
                } else {
                    Object instance = null;
                    try {
                        Class<?> clazz = Class.forName("net.minecraft.server.v1_16_R3.RecipeItemStack.b");
                        Constructor<?> constructor = clazz.getDeclaredConstructor(Tag.class);
                        constructor.setAccessible(true);
                        instance = constructor.newInstance(tag);
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return instance;
                }
            } else {
                throw new JsonParseException("An ingredient entry needs either a tag or an item");
            }
        }
    }


    @Override // NMS ItemStack
    public Object createNMSItemStack(JsonObject jsonObject) {
        String s = ChatDeserializer.h(jsonObject, "item");
        Item item = IRegistry.ITEM.getOptional(new MinecraftKey(s)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + s + "'"));
        ItemStack itemStack = new ItemStack(item);
        if (jsonObject.has("data")) {

            NBTBase base = Dynamic.convert(JsonToNBT.INSTANCE, DynamicOpsNBT.a, jsonObject.get("data"));
            NBTTagCompound compound = (NBTTagCompound) base;
            NBTTagCompound tag = compound.getCompound("tag");
            // We need to override the enchantment level tags to make them shorts since that's how they're made in game.
            // the ones created here are using integer tags.
            if (tag.hasKeyOfType("Enchantments", 9)) {
                NBTTagList list = tag.getList("Enchantments", 10);
                for (NBTBase nbtBase : list) {
                    NBTTagCompound enchantment = (NBTTagCompound) nbtBase;
                    enchantment.set("lvl", NBTTagShort.a((short) enchantment.getInt("lvl")));
                }
            }
            // TODO: CODECS in the future???
            //ItemStack nbtDecode = ItemStack.a.decode(DynamicOpsNBT.a, base).result().get().getFirst();

            //ItemStack decoded = ItemStack.a.decode(JsonToNBT.INSTANCE, jsonObject).result().get().getFirst();

            itemStack = ItemStack.a(compound.a(itemStack.save(new NBTTagCompound())));
        }
        int i = ChatDeserializer.a(jsonObject, "count", 1);
        itemStack.setCount(i);
        return itemStack;
    }

    @Override // Access to private RecipeItemStack constructor
    public Object constructRecipeItemStack(Stream<?> object, boolean hasData) {
        Object recipeItemStack = null;
        try {
            Class<?> clazz = Class.forName("net.minecraft.server.v1_16_R3.RecipeItemStack");
            Method method = clazz.getDeclaredMethod("b", Stream.class);
            method.setAccessible(true);
            recipeItemStack = method.invoke(clazz, object);
            clazz.getField("exact").setBoolean(recipeItemStack, hasData);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return recipeItemStack;
    }
}
