package com.epherical.crafting.item;

import com.epherical.crafting.ThonkCrafting;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.craftbukkit.BukkitComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;
import java.util.function.Function;

public class RecipeBook {

    private final ItemStack RECIPE_BOOK;
    private ThonkCrafting plugin;
    private Collection<Component> book;

    public RecipeBook(ThonkCrafting plugin) {
        RECIPE_BOOK = new ItemStack(Material.WRITTEN_BOOK, 1);
        this.plugin = plugin;
        fillRecipeBook();
    }

    private void fillRecipeBook() {
        ArrayList<Component> components = new ArrayList<>();
        BookMeta bookMeta = (BookMeta) RECIPE_BOOK.getItemMeta();
        plugin.getNmsInterface().getRecipeKeys().forEach(key -> {
            Recipe recipe = Bukkit.getRecipe(key);
            if (recipe instanceof ShapedRecipe) {

                Component component = parseShapedRecipe((ShapedRecipe) recipe, "Shaped Recipe ");
                TextComponent hoverText = Component.text("Recipe Location: " + key.toString(), NamedTextColor.AQUA)
                        .append(Component.newline())
                        .append(Component.newline())
                        .append(component.asComponent());

                HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverText);
                TextComponent recipeName = Component.text(key.toString(), NamedTextColor.BLUE)
                        .hoverEvent(HoverEventSource.unbox(hoverEvent));

                components.add(recipeName);
                bookMeta.addPage(BukkitComponentSerializer.gson().serialize(recipeName));
            }




        });
        book = components;
    }

    public Collection<Component> getBook() {
        return book;
    }

    public ItemStack getRecipeBook() {
        return RECIPE_BOOK;
    }


    private Component getRecipeInformation(Recipe recipe) {
        // is there a better way?
        Function<Recipe, Component> recipeStringFunction = funct -> {
            if (funct instanceof ShapedRecipe)
                return parseShapedRecipe((ShapedRecipe) funct, "Shaped Recipe");
            else if (funct instanceof ShapelessRecipe)
                return parseShapelessRecipe((ShapelessRecipe) funct, "Shapeless Recipe");
            else if (funct instanceof BlastingRecipe)
                return parseCookingRecipe((CookingRecipe<BlastingRecipe>) funct, "Blasting Recipe");
            else if (funct instanceof SmokingRecipe)
                return parseCookingRecipe((CookingRecipe<?>) funct, "Smoking Recipe");
            else if (funct instanceof ComplexRecipe)
                return Component.text("complex recipe");
            else if (funct instanceof FurnaceRecipe)
                return parseCookingRecipe((CookingRecipe<?>) funct, "Furnace Recipe");
            else if (funct instanceof SmithingRecipe)
                return parseSmithingRecipe((SmithingRecipe) funct, "Smithing Recipe");
            else if (funct instanceof StonecuttingRecipe) {
                return parseStonecuttingRecipe((StonecuttingRecipe) funct, "Stonecutting Recipe");
            }
            return Component.text("??? unknown");
        };

        return Component.text("Recipe Type: " + recipeStringFunction.apply(recipe));
    }
    // -=-=-=-=-=-=-
    // | 1 | 2 | 3 |
    // | 4 | 5 | 6 |
    // | 7 | 8 | 9 |
    // -=-=-=-=-=-=-
    // 1. Material
    // 2. Material
    // 3. Material
    // . . .
    // Result: Material/Name
    private Component parseShapedRecipe(ShapedRecipe recipe, String type) {
        int increment = 1;
        Map<Integer, RecipeChoice> choiceMap = new HashMap<>();
        String[] shape = recipe.getShape();

        for (Map.Entry<Character, RecipeChoice> entry : recipe.getChoiceMap().entrySet()) {
            for (int i = 0; i < shape.length; i++) {
                shape[i] = shape[i].replaceAll(entry.getKey().toString(), String.valueOf(increment));
            }
            choiceMap.put(increment, entry.getValue());
            increment++;
        }



        TextComponent.Builder loopComponent = Component.text();
        for (int s = 0; s < shape.length; s++) {
            shape[s] = shape[s].replace("", " ").replaceAll(" ", " || ").trim();
            loopComponent.append(Component.text(shape[s]));
            loopComponent.append(Component.newline());
        }
        TextComponent ingredientGrid = loopComponent.build();

        TextComponent.Builder ingredientLoop = Component.text();
        for (Map.Entry<Integer, RecipeChoice> entry : choiceMap.entrySet()) {
            RecipeChoice choice = entry.getValue();
            String strChoice = "";
            if (choice instanceof RecipeChoice.ExactChoice) {
                List<ItemStack> choices = ((RecipeChoice.ExactChoice) choice).getChoices();
                strChoice = entry.getKey() + ". " + choice.getItemStack().getType();
                if (choices.size() > 1) {
                    strChoice += " plus " +  choices.size() + " choices.";
                }
            } else if (choice instanceof RecipeChoice.MaterialChoice) {
                List<Material> choices = ((RecipeChoice.MaterialChoice) choice).getChoices();
                strChoice = entry.getKey() + ". " + choice.getItemStack().getType();
                if (choices.size() > 1) {
                    strChoice += " plus " +  choices.size() + " choices.";
                }
            }
            ingredientLoop.append(Component.text(strChoice));
            ingredientLoop.append(Component.newline());
        }

        ingredientLoop.append(Component.text("Result: " + recipe.getResult().getType().name()));
        Component ingredientMap = ingredientLoop.build();

        Component component = parseType(type)
                .append(Component.text("Crafting Menu"))
                .append(Component.newline())
                .append(Component.text("-=-=-=-=-=-=-"))
                .append(Component.newline())
                .append(ingredientGrid.asComponent())
                .append(Component.text("-=-=-=-=-=-=-"))
                .append(Component.newline())
                .append(ingredientMap.asComponent());

        return component;
    }

    private Component parseShapelessRecipe(ShapelessRecipe recipe, String type) {
        return Component.empty();
    }

    private Component parseCookingRecipe(CookingRecipe<?> recipe, String type) {
        return Component.empty();
    }

    private Component parseSmithingRecipe(SmithingRecipe recipe, String type) {
        return Component.empty();
    }

    private Component parseStonecuttingRecipe(StonecuttingRecipe recipe, String type) {
        return Component.empty();
    }

    private Component parseType(String type) {
        return Component.text(type);
    }
}
