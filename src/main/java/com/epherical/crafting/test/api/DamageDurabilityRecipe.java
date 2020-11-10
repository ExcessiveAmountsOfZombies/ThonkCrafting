package com.epherical.crafting.test.api;

import org.apache.commons.lang.Validate;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// Can't extend ShapelessRecipe for the API portion because it'll turn into a shapeless recipe when you register it on the server
public class DamageDurabilityRecipe implements Recipe, Keyed {
    // it's basically the same as a shapeless recipe, it just can't be extended because of the way the recipe register works?
    private final NamespacedKey key;
    private final ItemStack output;
    private final List<RecipeChoice> ingredients = new ArrayList<>();
    private String group = "";

    public DamageDurabilityRecipe(NamespacedKey key, ItemStack result) {
        this.key = key;
        this.output = new ItemStack(result);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public ItemStack getResult() {
        return output;
    }


    
    public DamageDurabilityRecipe addIngredient( MaterialData ingredient) {
        return this.addIngredient(1, (MaterialData)ingredient);
    }

    
    public DamageDurabilityRecipe addIngredient( Material ingredient) {
        return this.addIngredient(1, ingredient, 0);
    }

    /** @deprecated */
    @Deprecated
    
    public DamageDurabilityRecipe addIngredient( Material ingredient, int rawdata) {
        return this.addIngredient(1, ingredient, rawdata);
    }

    
    public DamageDurabilityRecipe addIngredient(int count,  MaterialData ingredient) {
        return this.addIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    
    public DamageDurabilityRecipe addIngredient(int count,  Material ingredient) {
        return this.addIngredient(count, ingredient, 0);
    }

    /** @deprecated */
    @Deprecated
    
    public DamageDurabilityRecipe addIngredient(int count,  Material ingredient, int rawdata) {
        Validate.isTrue(this.ingredients.size() + count <= 9, "Shapeless recipes cannot have more than 9 ingredients");
        if (rawdata == -1) {
            boolean rawdata1 = true;
        }

        while(count-- > 0) {
            this.ingredients.add(new RecipeChoice.MaterialChoice(Collections.singletonList(ingredient)));
        }

        return this;
    }

    
    public DamageDurabilityRecipe addIngredient( RecipeChoice ingredient) {
        Validate.isTrue(this.ingredients.size() + 1 <= 9, "Shapeless recipes cannot have more than 9 ingredients");
        this.ingredients.add(ingredient);
        return this;
    }

    
    public DamageDurabilityRecipe addIngredient( ItemStack item) {
        return this.addIngredient(1, (ItemStack)item);
    }

    
    public DamageDurabilityRecipe addIngredient(int count,  ItemStack item) {
        Validate.isTrue(this.ingredients.size() + count <= 9, "Shapeless recipes cannot have more than 9 ingredients");

        while(count-- > 0) {
            this.ingredients.add(new RecipeChoice.ExactChoice(item));
        }

        return this;
    }

    
    public DamageDurabilityRecipe removeIngredient( ItemStack item) {
        return this.removeIngredient(1, (ItemStack)item);
    }

    
    public DamageDurabilityRecipe removeIngredient(int count,  ItemStack item) {
        Iterator iterator = this.ingredients.iterator();

        while(count > 0 && iterator.hasNext()) {
            ItemStack stack = ((RecipeChoice)iterator.next()).getItemStack();
            if (stack.equals(item)) {
                iterator.remove();
                --count;
            }
        }

        return this;
    }

    
    public DamageDurabilityRecipe removeIngredient( RecipeChoice ingredient) {
        this.ingredients.remove(ingredient);
        return this;
    }

    
    public DamageDurabilityRecipe removeIngredient( Material ingredient) {
        return this.removeIngredient(ingredient, 0);
    }

    
    public DamageDurabilityRecipe removeIngredient( MaterialData ingredient) {
        return this.removeIngredient(ingredient.getItemType(), ingredient.getData());
    }

    
    public DamageDurabilityRecipe removeIngredient(int count,  Material ingredient) {
        return this.removeIngredient(count, ingredient, 0);
    }

    
    public DamageDurabilityRecipe removeIngredient(int count,  MaterialData ingredient) {
        return this.removeIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    /** @deprecated */
    @Deprecated
    public DamageDurabilityRecipe removeIngredient( Material ingredient, int rawdata) {
        return this.removeIngredient(1, ingredient, rawdata);
    }

    /** @deprecated */
    @Deprecated
    public DamageDurabilityRecipe removeIngredient(int count,  Material ingredient, int rawdata) {
        Iterator iterator = this.ingredients.iterator();

        while(count > 0 && iterator.hasNext()) {
            ItemStack stack = ((RecipeChoice)iterator.next()).getItemStack();
            if (stack.getType() == ingredient && stack.getDurability() == rawdata) {
                iterator.remove();
                --count;
            }
        }

        return this;
    }

    public List<RecipeChoice> getChoiceList() {
        List<RecipeChoice> result = new ArrayList<>(this.ingredients.size());

        for (RecipeChoice ingredient : this.ingredients) {
            result.add(ingredient.clone());
        }

        return result;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
