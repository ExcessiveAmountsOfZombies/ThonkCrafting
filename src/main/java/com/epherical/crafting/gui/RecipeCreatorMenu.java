package com.epherical.crafting.gui;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.recipes.RecipeGenerator;
import com.epherical.crafting.recipes.RecipeToJson;
import com.epherical.crafting.recipes.impl.*;
import com.epherical.crafting.ui.Menu;
import com.epherical.crafting.ui.MenuDefaults;
import com.epherical.crafting.ui.click.MenuButton;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeCreatorMenu {


    public RecipeCreatorMenu(Recipe recipe, CommandSender sender, boolean edit, boolean convertToThonkCraftingRecipe) {
        if (recipe instanceof CustomRecipe) {
            // TODO: add in ability to add options to the recipe.
            CustomRecipe recipe1 = (CustomRecipe) recipe;
            recipe1.getOptions();
        }

        // TODO: how can i improve this, it's ugly and kinda repetitive.
        if (recipe instanceof ShapedRecipe) {
            // VANILLA RECIPE
            ShapedRecipe cast = (ShapedRecipe) recipe;
            createCraftingMenu(cast.getKey(), cast.getGroup(), shapedRecipeMenu(cast.getIngredientMap(),
                    cast.getShape(), edit), cast.getResult(), edit ? "Shaped Recipe Editor" : "Shaped Recipe Viewer", sender, edit, true, !convertToThonkCraftingRecipe);
        } else if (recipe instanceof RecipeShaped) {
            // THONKCRAFTING RECIPE
            RecipeShaped cast = (RecipeShaped) recipe;
            createCraftingMenu(cast.getKey(), cast.getGroup(), shapedRecipeMenu(cast.getIngredientMap(),
                    cast.getShape(), edit), cast.getResult(), edit ? "Shaped Recipe Editor" : "Shaped Recipe Viewer", sender, edit, true, false);
        } else if (recipe instanceof ShapelessRecipe) {
            // VANILLA RECIPE
            ShapelessRecipe cast = (ShapelessRecipe) recipe;
            createCraftingMenu(cast.getKey(), cast.getGroup(), shapelessRecipeMenu(cast.getIngredientList(),
                    edit), cast.getResult(), edit ? "Shapeless Recipe Editor" : "Shapeless Recipe Viewer", sender, edit, false, !convertToThonkCraftingRecipe);
        } else if (recipe instanceof RecipeShapeless) {
            // THONKCRAFTING RECIPE
            RecipeShapeless cast = (RecipeShapeless) recipe;
            createCraftingMenu(cast.getKey(), cast.getGroup(), shapelessRecipeMenu(cast.getIngredientList(),
                    edit), cast.getResult(), edit ? "Shapeless Recipe Editor" : "Shapeless Recipe Viewer", sender, edit, false, false);
        } else if (recipe instanceof CookingRecipe ) { // the blob of code that makes your eyes glaze over
            // VANILLA RECIPE
            CookingRecipe<?> cast = (CookingRecipe<?>) recipe;
            createCookingMenu(cast.getInput(), cast.getResult(), edit ? "Cooking Recipe Editor" : "Cooking Recipe Viewer", sender, edit, cast, convertToThonkCraftingRecipe);
        } else if (recipe instanceof AbstractCooking) {
            // THONKCRAFTING RECIPE
            AbstractCooking cast = (AbstractCooking) recipe;
            createCookingMenu(cast.getInput(), cast.getResult(), edit ? "Cooking Recipe Editor" : "Cooking Recipe Viewer", sender, edit, cast, false);
        } else if (recipe instanceof RecipeSmithing) {
            RecipeSmithing cast = (RecipeSmithing) recipe;


        } else if (recipe instanceof SmithingRecipe) {
            SmithingRecipe cast = (SmithingRecipe) recipe;


        } else if (recipe instanceof RecipeStonecutting) {
            // THONKCRAFTING RECIPE
            RecipeStonecutting cast = (RecipeStonecutting) recipe;
            createCuttingMenu(cast.getInput(), cast.getResult(), edit ? "Cutting Recipe Editor" : " Cutting Recipe Viewer", sender, edit, false, cast.getKey(), cast.getGroup());
        } else if (recipe instanceof StonecuttingRecipe) {
            // VANILLA RECIPE
            StonecuttingRecipe cast = (StonecuttingRecipe) recipe;
            createCuttingMenu(cast.getInput(), cast.getResult(), edit ? "Cutting Recipe Editor" : " Cutting Recipe Viewer", sender, edit, !convertToThonkCraftingRecipe, cast.getKey(), cast.getGroup());
        }
    }

    private Map<Integer, MenuButton> shapelessRecipeMenu(List<ItemStack> ingredientList, boolean editingRecipe) {
        int firstSlot = 11;
        int slotRow = 0;
        int i = 0;
        Map<Integer, MenuButton> displayedIngredients = new HashMap<>();
        for (ItemStack itemStack : ingredientList) {
            int slot = firstSlot+(slotRow*9)+i;
            displayedIngredients.put(slot, new MenuButton(event -> event.setCancelled(!editingRecipe), itemStack));
            if (i % 2 == 0) {
                slotRow++;
                i = 0;
            }
            i++;
        }
        return displayedIngredients;
    }

    private Map<Integer, MenuButton> shapedRecipeMenu(Map<Character, ItemStack> ingredients, String[] shape, boolean editingRecipe) {
        Map<Integer, MenuButton> displayedIngredients = new HashMap<>();
        int firstSlot = 11;
        int slotRow = 0;
        for (String row : shape) {
            for (int i = 0; i < row.toCharArray().length; i++) {
                ItemStack item = ingredients.get(row.toCharArray()[i]);
                int slot = firstSlot+(slotRow*9)+i;
                displayedIngredients.put(slot, new MenuButton(event -> event.setCancelled(!editingRecipe), item));
            }
            slotRow++;
        }
        return displayedIngredients;
    }

    private void createCraftingMenu(NamespacedKey key, String group, Map<Integer, MenuButton> ingredients, ItemStack result, String menuName,
                                    CommandSender sender, boolean editingRecipe, boolean isShapedRecipe, boolean isVanillaRecipe) {
        Menu.Builder builder = new Menu.Builder(6, menuName, true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(!editingRecipe), result))
                .addMenuButton(38, MenuDefaults.defaultCloseButton(38))
                // TODO: future command block usage
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButtons(new MenuButton(event -> event.setCancelled(!editingRecipe), Material.AIR), 11, 12, 13, 20, 21, 22, 29, 30, 31)
                .addMenuButtons(ingredients);
        if (editingRecipe) {
            builder.addMenuButton(40, acceptButtonCrafting(key, group, Material.NETHER_STAR, isShapedRecipe, isVanillaRecipe));
        } else {
            builder.addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD));
            builder.addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER));
        }
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    private void createCookingMenu(ItemStack input, ItemStack result, String menuName, CommandSender sender, boolean editingRecipe, Recipe recipe, boolean convertToThonkCraftingRecipe) {
        Menu.Builder builder = new Menu.Builder(6, menuName, true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(!editingRecipe), result))
                .addMenuButton(12, new MenuButton(event -> event.setCancelled(!editingRecipe), input))
                .addMenuButton(30, new MenuButton(event -> event.setCancelled(true), Material.LAVA_BUCKET))
                .addMenuButton(38, MenuDefaults.defaultCloseButton(38))
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK));
        if (editingRecipe) {
            builder.addMenuButton(40, acceptButtonCooking(recipe, Material.NETHER_STAR, convertToThonkCraftingRecipe));
        } else {
            builder.addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD));
            builder.addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER));
        }
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    private void createCuttingMenu(ItemStack input, ItemStack result, String menuName, CommandSender sender, boolean editingRecipe, boolean isVanillaRecipe, NamespacedKey key, String group) {
        Menu.Builder builder = new Menu.Builder(6, menuName, true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(!editingRecipe), result))
                .addMenuButton(21, new MenuButton(event -> event.setCancelled(!editingRecipe), input))
                .addMenuButton(38, MenuDefaults.defaultCloseButton(38))
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButton(22, new MenuButton(event -> event.setCancelled(true), Material.YELLOW_STAINED_GLASS_PANE))
                .addMenuButton(23, new MenuButton(event -> event.setCancelled(true), Material.LIME_STAINED_GLASS_PANE));
        if (editingRecipe) {
            builder.addMenuButton(40, acceptButtonCutting(key, group, Material.NETHER_STAR, isVanillaRecipe));
        } else {
            builder.addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD));
            builder.addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER));
        }

        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    /*private void createSmithingMenu(ItemStack input, ItemStack secondaryInput, ItemStack result, String menuName, CommandSender sender, boolean editingRecipe) {
        Menu.Builder builder = new Menu.Builder(6, "Smithing Recipe", true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(true), result))
                .addMenuButton(37, MenuDefaults.defaultCloseButton(37))
                .addMenuButton(38, informationButton)
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD))
                .addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER))
                .addMenuButton(21, new MenuButton(event -> event.setCancelled(true), input))
                .addMenuButton(22, new MenuButton(event -> event.setCancelled(true), secondaryInput));
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }*/

    private MenuButton acceptButtonCooking(Recipe recipe, Material material, boolean convertToThonkCraftingRecipe) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Accept");
        itemStack.setItemMeta(meta);

        return new MenuButton(event -> {
            event.setCancelled(true);
            ItemStack input = event.getInventory().getItem(12);
            ItemStack result = event.getInventory().getItem(24);

            RecipeToJson json = null;

            // TODO: find a way to not use instanceof trash it looks so ugly and i'm sure there is a better way that makes sense

            if (recipe instanceof RecipeBlasting) {
                // ThonkCrafting Blasting
                RecipeBlasting cast = (RecipeBlasting) recipe;
                json = RecipeGenerator.createCustomBlastingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            } else if (recipe instanceof RecipeCampfire) {
                // ThonkCrafting Campfire
                RecipeCampfire cast = (RecipeCampfire) recipe;
                json = RecipeGenerator.createCustomCampfireRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            } else if (recipe instanceof RecipeSmoking) {
                // ThonkCrafting Smoking
                RecipeSmoking cast = (RecipeSmoking) recipe;
                json = RecipeGenerator.createCustomSmokingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            } else if (recipe instanceof RecipeSmelting) {
                // ThonkCrafting Smelting
                RecipeSmelting cast = (RecipeSmelting) recipe;
                json = RecipeGenerator.createCustomSmeltingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            } else if (recipe instanceof BlastingRecipe) {
                // Vanilla Blasting
                BlastingRecipe cast = (BlastingRecipe) recipe;
                json = convertToThonkCraftingRecipe
                        ? RecipeGenerator.createCustomBlastingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime())
                        : RecipeGenerator.createVanillaBlastingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            } else if (recipe instanceof CampfireRecipe) {
                // Vanilla Campfire
                CampfireRecipe cast = (CampfireRecipe) recipe;
                json = convertToThonkCraftingRecipe
                        ? RecipeGenerator.createCustomCampfireRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime())
                        : RecipeGenerator.createVanillaCampfireRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            } else if (recipe instanceof SmokingRecipe) {
                // Vanilla Smoking
                SmokingRecipe cast = (SmokingRecipe) recipe;
                json = convertToThonkCraftingRecipe
                        ? RecipeGenerator.createCustomSmokingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime())
                        : RecipeGenerator.createVanillaSmokingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            } else if (recipe instanceof FurnaceRecipe) {
                // Vanilla Smelting
                FurnaceRecipe cast = (FurnaceRecipe) recipe;
                json = convertToThonkCraftingRecipe
                        ? RecipeGenerator.createCustomSmeltingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime())
                        : RecipeGenerator.createVanillaSmeltingRecipe(cast.getKey(), cast.getGroup(), input, result, cast.getExperience(), cast.getCookingTime());
            }


            RecipeGenerator.saveRecipeToFile(json);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }

    private MenuButton acceptButtonCutting(NamespacedKey key, String group, Material material, boolean isVanillaRecipe) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Accept");
        itemStack.setItemMeta(meta);
        return new MenuButton(event -> {
            event.setCancelled(true);
            ItemStack input = event.getInventory().getItem(21);
            ItemStack result = event.getInventory().getItem(24);


            RecipeToJson json;

            if (isVanillaRecipe) {
                json = RecipeGenerator.createVanillaCuttingRecipe(key, group, input, result, result != null ? result.getAmount() : 0);
            } else {
                json = RecipeGenerator.createCustomCuttingRecipe(key, group, input, result);
            }


            RecipeGenerator.saveRecipeToFile(json);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }


    private MenuButton acceptButtonCrafting(NamespacedKey key, String group, Material material, boolean isShapedRecipe, boolean isVanillaRecipe) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Accept");
        itemStack.setItemMeta(meta);
        return new MenuButton(event -> {
            event.setCancelled(true);

            int firstSlot = 11;

            Map<Character, ItemStack> ingredients = new HashMap<>();
            int increment = 0;
            String[] shape = new String[3];

            for (int slotRow = 0; slotRow < 3; slotRow++) {
                String row = "";
                for (int slot = 0; slot < 3; slot++) {
                    ItemStack item = event.getInventory().getItem(firstSlot+(slotRow*9)+slot);
                    if (item != null) {
                        ingredients.put(Character.forDigit(increment, 10), item);
                        row += increment;
                        increment++;
                    } else {
                        row += " ";
                    }
                }
                shape[slotRow] = row;
            }

            // result slot
            ItemStack result = event.getInventory().getItem(24);

            RecipeToJson json;

            if (isShapedRecipe) {
                json = new RecipeGenerator.RecipeShapedToJson(key, result, group, shape, ingredients, isVanillaRecipe);
            } else {
                if (isVanillaRecipe) {
                    json = RecipeGenerator.createVanillaShapelessRecipe(key, result, result != null ? result.getAmount() : 0, group, ingredients.values());
                } else {
                    json = new RecipeGenerator.RecipeShapelessToJson(key, result, group, ingredients.values(), false);
                }

            }

            RecipeGenerator.saveRecipeToFile(json);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }
}
