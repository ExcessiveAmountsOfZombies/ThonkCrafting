package com.epherical.crafting.gui;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.listener.ChatListener;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.recipes.RecipeGenerator;
import com.epherical.crafting.recipes.RecipeToJson;
import com.epherical.crafting.recipes.impl.*;
import com.epherical.crafting.ui.Menu;
import com.epherical.crafting.ui.MenuDefaults;
import com.epherical.crafting.ui.click.ContainerButton;
import com.epherical.crafting.ui.click.MenuButton;
import com.epherical.crafting.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RecipeMenus {


    public RecipeMenus(Recipe recipe, CommandSender sender, boolean edit, boolean convertToThonkCraftingRecipe) {
        List<Options> options = new ArrayList<>();
        if (recipe instanceof CustomRecipe) {
            // TODO: add in ability to add options to the recipe.
            CustomRecipe recipe1 = (CustomRecipe) recipe;
            options = recipe1.getOptions();
        }

        Menu menu = null;

        // TODO: how can i improve this, it's ugly and kinda repetitive.
        Player player = (Player) sender;

        if (recipe instanceof ShapedRecipe) {
            // VANILLA RECIPE
            ShapedRecipe cast = (ShapedRecipe) recipe;
            menu = createCraftingMenu(cast.getKey(), cast.getGroup(), shapedRecipeMenu(cast.getIngredientMap(),
                    cast.getShape(), edit), cast.getResult(), edit ? "Shaped Recipe Editor" : "Shaped Recipe Viewer", edit, true, !convertToThonkCraftingRecipe, options);
        } else if (recipe instanceof RecipeShaped) {
            // THONKCRAFTING RECIPE
            RecipeShaped cast = (RecipeShaped) recipe;
            menu = createCraftingMenu(cast.getKey(), cast.getGroup(), shapedRecipeMenu(cast.getIngredientMap(),
                    cast.getShape(), edit), cast.getResult(), edit ? "Shaped Recipe Editor" : "Shaped Recipe Viewer", edit, true, false, options);
        } else if (recipe instanceof ShapelessRecipe) {
            // VANILLA RECIPE
            ShapelessRecipe cast = (ShapelessRecipe) recipe;
            menu = createCraftingMenu(cast.getKey(), cast.getGroup(), shapelessRecipeMenu(cast.getIngredientList(),
                    edit), cast.getResult(), edit ? "Shapeless Recipe Editor" : "Shapeless Recipe Viewer", edit, false, !convertToThonkCraftingRecipe, options);
        } else if (recipe instanceof RecipeShapeless) {
            // THONKCRAFTING RECIPE
            RecipeShapeless cast = (RecipeShapeless) recipe;
            menu = createCraftingMenu(cast.getKey(), cast.getGroup(), shapelessRecipeMenu(cast.getIngredientList(),
                    edit), cast.getResult(), edit ? "Shapeless Recipe Editor" : "Shapeless Recipe Viewer", edit, false, false, options);
        } else if (recipe instanceof CookingRecipe ) { // the blob of code that makes your eyes glaze over
            // VANILLA RECIPE
            CookingRecipe<?> cast = (CookingRecipe<?>) recipe;
            NamespacedKey key;
            if (convertToThonkCraftingRecipe) {
                Class<?> customRecipeClass = CraftingRegistry.baseToCustomClass.get(cast.getClass().getName());
                key = CraftingRegistry.recipeClassSerializerMap.get(customRecipeClass.getName());
            } else {
                key = CraftingRegistry.recipeClassSerializerMap.get(cast.getClass().getName());
            }
            menu = createCookingMenu(cast.getInput(), cast.getResult(), edit ? "Cooking Recipe Editor" : "Cooking Recipe Viewer", edit, cast.getKey(), cast.getGroup(), cast.getExperience(), cast.getCookingTime(), key, player, options);
        } else if (recipe instanceof AbstractCooking) {
            // THONKCRAFTING RECIPE
            AbstractCooking cast = (AbstractCooking) recipe;
            NamespacedKey key = CraftingRegistry.recipeClassSerializerMap.get(cast.getClass().getName());
            menu = createCookingMenu(cast.getInput(), cast.getResult(), edit ? "Cooking Recipe Editor" : "Cooking Recipe Viewer", edit, cast.getKey(), cast.getGroup(), cast.getExperience(), cast.getCookingTime(), key, player, options);
        } else if (recipe instanceof RecipeSmithing) {
            RecipeSmithing cast = (RecipeSmithing) recipe;


        } else if (recipe instanceof SmithingRecipe) {
            SmithingRecipe cast = (SmithingRecipe) recipe;


        } else if (recipe instanceof RecipeStonecutting) {
            // THONKCRAFTING RECIPE
            RecipeStonecutting cast = (RecipeStonecutting) recipe;
            menu = createCuttingMenu(cast.getInput(), cast.getResult(), edit ? "Cutting Recipe Editor" : " Cutting Recipe Viewer", edit, false, cast.getKey(), cast.getGroup(), options);
        } else if (recipe instanceof StonecuttingRecipe) {
            // VANILLA RECIPE
            StonecuttingRecipe cast = (StonecuttingRecipe) recipe;
            menu = createCuttingMenu(cast.getInput(), cast.getResult(), edit ? "Cutting Recipe Editor" : " Cutting Recipe Viewer", edit, !convertToThonkCraftingRecipe, cast.getKey(), cast.getGroup(), options);
        }
        menu.openInventory((HumanEntity) sender);
    }

    private static Map<Integer, MenuButton> shapelessRecipeMenu(List<ItemStack> ingredientList, boolean editingRecipe) {
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

    private static Map<Integer, MenuButton> shapedRecipeMenu(Map<Character, ItemStack> ingredients, String[] shape, boolean editingRecipe) {
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

    public static Menu createCraftingMenu(NamespacedKey key, String group, Map<Integer, MenuButton> ingredients, ItemStack result, String menuName,
                                          boolean editingRecipe, boolean isShapedRecipe, boolean isVanillaRecipe, List<Options> options) {
        Menu.Builder builder = new Menu.Builder(6, menuName, true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(!editingRecipe), result))
                .addMenuButton(38, MenuDefaults.defaultCloseButton(38))
                // TODO: future command block usage
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButtons(new MenuButton(event -> event.setCancelled(!editingRecipe), Material.AIR), 11, 12, 13, 20, 21, 22, 29, 30, 31);
        if (ingredients != null) {
            builder.addMenuButtons(ingredients);
        }
        if (editingRecipe) {
            builder.addMenuButton(40, acceptButtonCrafting(key, group, Material.NETHER_STAR, isShapedRecipe, isVanillaRecipe, options));
        } else {
            builder.addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD));
            builder.addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER));
        }
        return builder.build();
    }

    public static Menu createCookingMenu(ItemStack input, ItemStack result, String menuName, boolean editingRecipe, NamespacedKey recipeKey, String group,
                                         float exp, int cookingTime, NamespacedKey serializerKey, Player player, List<Options> options) {
        Menu.Builder builder = new Menu.Builder(6, menuName, true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(!editingRecipe), result))
                .addMenuButton(12, new MenuButton(event -> event.setCancelled(!editingRecipe), input))
                .addMenuButton(30, new MenuButton(event -> event.setCancelled(true), Material.LAVA_BUCKET))
                .addMenuButton(38, MenuDefaults.defaultCloseButton(38));
        if (editingRecipe) {
            builder.addMenuButton(39, editCookingRecipe(recipeKey, input, result, group, exp, cookingTime, Material.COMMAND_BLOCK, serializerKey, player, options));
            builder.addMenuButton(40, acceptButtonCooking(recipeKey, group, exp, cookingTime, Material.NETHER_STAR, serializerKey, options));
        } else {
            builder.addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK));
            builder.addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD));
            builder.addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER));
        }
        return builder.build();
    }

    public static Menu createCuttingMenu(ItemStack input, ItemStack result, String menuName, boolean editingRecipe, boolean isVanillaRecipe, NamespacedKey key, String group, List<Options> options) {
        Menu.Builder builder = new Menu.Builder(6, menuName, true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(!editingRecipe), result))
                .addMenuButton(21, new MenuButton(event -> event.setCancelled(!editingRecipe), input))
                .addMenuButton(38, MenuDefaults.defaultCloseButton(38))
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButton(22, new MenuButton(event -> event.setCancelled(true), Material.YELLOW_STAINED_GLASS_PANE))
                .addMenuButton(23, new MenuButton(event -> event.setCancelled(true), Material.LIME_STAINED_GLASS_PANE));
        if (editingRecipe) {
            builder.addMenuButton(40, acceptButtonCutting(key, group, Material.NETHER_STAR, isVanillaRecipe, options));
        } else {
            builder.addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD));
            builder.addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER));
        }

        return builder.build();
    }

    /**
     * {@link com.epherical.crafting.CraftingRegistry.RecipeCreator}
     */
    public static Menu recipeCreatorMenu(String menuName, NamespacedKey key, String group, CraftingRegistry.RecipeType type,
                                         boolean vanillaRecipe, NamespacedKey recipeSerializerKey, Player player, List<Options> options) {
        switch (type) {
            case COOKING:
                return createCookingMenu(new ItemStack(Material.AIR), new ItemStack(Material.AIR), menuName, true, key, group, 0.1f, 200, recipeSerializerKey, player, options);
            case CUTTING:
                // TODO: when adding the options for all the types we need a player, and options instance to be passed
                return createCuttingMenu(new ItemStack(Material.AIR), new ItemStack(Material.AIR), menuName, true, vanillaRecipe, key, group, options);
            case SHAPED:
                return createCraftingMenu(key, group, null, new ItemStack(Material.AIR), menuName, true, true, vanillaRecipe, options);
            case SHAPELESS:
                return createCraftingMenu(key, group, null, new ItemStack(Material.AIR), menuName, true, false, vanillaRecipe, options);
            case SMITHING:
            default:
                return null;
        }
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

    private static MenuButton acceptButtonCooking(NamespacedKey recipeKey, String group, float exp, int cookingTime, Material material, NamespacedKey serializerKey, List<Options> options) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Accept");
        itemStack.setItemMeta(meta);

        return new MenuButton(event -> {
            event.setCancelled(true);
            ItemStack input = event.getInventory().getItem(12);
            ItemStack result = event.getInventory().getItem(24);

            RecipeToJson json = RecipeGenerator.createCookingRecipe(recipeKey, group, input, result, exp, cookingTime, serializerKey, options);


            RecipeGenerator.saveRecipeToFile(json);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }

    private static MenuButton acceptButtonCutting(NamespacedKey key, String group, Material material, boolean isVanillaRecipe, List<Options> options) {
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
                json = RecipeGenerator.createVanillaCuttingRecipe(key, group, input, result, result != null ? result.getAmount() : 0, options);
            } else {
                json = RecipeGenerator.createCustomCuttingRecipe(key, group, input, result, options);
            }


            RecipeGenerator.saveRecipeToFile(json);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }


    private static MenuButton acceptButtonCrafting(NamespacedKey key, String group, Material material, boolean isShapedRecipe, boolean isVanillaRecipe, List<Options> options) {
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
                json = new RecipeGenerator.RecipeShapedToJson(key, result, group, shape, ingredients, isVanillaRecipe, options);
            } else {
                if (isVanillaRecipe) {
                    json = RecipeGenerator.createVanillaShapelessRecipe(key, result, result != null ? result.getAmount() : 0, group, ingredients.values(), options);
                } else {
                    json = new RecipeGenerator.RecipeShapelessToJson(key, result, group, ingredients.values(), false, options);
                }

            }

            RecipeGenerator.saveRecipeToFile(json);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }

    private static MenuButton editCookingRecipe(NamespacedKey recipeKey, ItemStack input, ItemStack result, String group,
                                                float exp, int cookingTime, Material material, NamespacedKey serializerKey, Player player, List<Options> options) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET.toString() + ChatColor.WHITE + "Current Properties");
        meta.setLore(Arrays.asList("Cooking Properties", ChatColor.GRAY + "Experience: " + exp, ChatColor.GRAY + "Cooking Time: " + cookingTime));
        itemStack.setItemMeta(meta);

        return new MenuButton(baseClickEvent -> {
            // TODO: improve this, will also include options in the future.
            baseClickEvent.setCancelled(true);
            Menu.Builder builder = new Menu.Builder(6, "Edit Properties", true, Material.BLACK_STAINED_GLASS_PANE);

            ArrayList<Integer> slots = MenuDefaults.acceptableSlots(6);

            ContainerButton<Number> experienceButton = new ContainerButton<>(event -> {
                event.setCancelled(true);
                Menu menu = (Menu) event.getInventory().getHolder();
                ContainerButton<Number> button = (ContainerButton<Number>) menu.getConsumableSlots().get(event.getRawSlot());
                addResponseRequest(event, menu, button, "number");
            }, ItemUtil.createContainerItem(Material.EXPERIENCE_BOTTLE, "", ChatColor.GRAY + "Experience per cooking iteration: " + exp), exp);

            ContainerButton<Number> timeButton = new ContainerButton<>(event -> {
                event.setCancelled(true);
                Menu menu = (Menu) event.getInventory().getHolder();
                ContainerButton<Number> button = (ContainerButton<Number>) menu.getConsumableSlots().get(event.getRawSlot());
                addResponseRequest(event, menu, button, "number");
            }, ItemUtil.createContainerItem( Material.LAVA_BUCKET,"", ChatColor.GRAY + "Time to cook: " + cookingTime), cookingTime);


            builder.addMenuButton(11, experienceButton);
            builder.addMenuButton(12, timeButton);

            builder.addMenuButton(49, new MenuButton(event1 -> {
                event1.setCancelled(true);

                float experience = experienceButton.getContainer().floatValue();
                int time = timeButton.getContainer().intValue();

                createCookingMenu(input, result, "Cooking Recipe Editor", true, recipeKey, group, experience, time, serializerKey, player, options).openInventory(player);
            }, Material.NETHER_STAR));

            builder.build().openInventory(player);
        }, itemStack);
    }

    private static void addResponseRequest(InventoryClickEvent event, Menu menu, ContainerButton<?> button, String type) {
        ThonkCrafting.getInstance().getChatListener().addResponseRequest(event.getWhoClicked().getUniqueId(), button);

        ChatListener listener = ThonkCrafting.getInstance().getChatListener();

        listener.addResponseRequest(event.getWhoClicked().getUniqueId(), button);
        listener.addSavedMenu(event.getWhoClicked().getUniqueId(), menu);

        event.getWhoClicked().sendMessage("Enter in chat a(n): " + type);

        event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
    }
}
