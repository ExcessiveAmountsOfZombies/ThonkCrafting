package com.epherical.crafting;

import com.epherical.crafting.options.Options;
import com.epherical.crafting.options.Options.IOption;
import com.epherical.crafting.options.result.success.ApplyPotion;
import com.epherical.crafting.options.result.success.PlaySound;
import com.epherical.crafting.options.result.test.BiomeCheck;
import com.epherical.crafting.options.result.test.Cooldown;
import com.epherical.crafting.options.result.test.Permission;
import com.epherical.crafting.options.result.test.WorldCheck;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionRegister {


    private static final Map<NamespacedKey, IOption<?>> optionMap = new HashMap<>();

    private static IOption<WorldCheck> worldCheck;
    private static IOption<Permission> permissionCheck;
    private static IOption<BiomeCheck> biomeCheck;
    private static IOption<Cooldown> cooldownCheck;

    private static IOption<ApplyPotion> applyPotion;
    private static IOption<PlaySound> playSound;



    public static void init() {
        worldCheck = registerOption(createKey("thonkcrafting", "world_check"), WorldCheck::new);
        permissionCheck = registerOption(createKey("thonkcrafting", "permission_check"), Permission::new);
        biomeCheck = registerOption(createKey("thonkcrafting", "biome_check"), BiomeCheck::new);
        cooldownCheck = registerOption(createKey("thonkcrafting", "cooldown_check"), Cooldown::new);

        applyPotion = registerOption(createKey("thonkcrafting", "apply_potion"), ApplyPotion::new);
        playSound = registerOption(createKey("thonkcrafting", "play_sound"), PlaySound::new);

    }


    public static <OPT extends IOption<?>> OPT registerOption(NamespacedKey key, OPT option) {
        if (optionMap.containsKey(key)) {
            // TODO: log error duplicate key
        }
        optionMap.put(key, option);

        return option;
    }


    public static NamespacedKey createKey(String namespace, String value) {
        return new NamespacedKey(namespace, value);
    }

    private static NamespacedKey createKey(String entireKey) {
        String[] split = entireKey.split(":");
        return new NamespacedKey(split[0], split[1]);
    }

    public static ArrayList<Options> getOptions(JsonObject object) {
        ArrayList<Options> options = new ArrayList<>();
        if (object != null && object.has("options")) {
            JsonArray array = object.getAsJsonArray("options");
            for (JsonElement element : array) {
                JsonObject singleOption = element.getAsJsonObject();
                if (singleOption.has("type")) {
                    NamespacedKey key = createKey(singleOption.getAsJsonPrimitive("type").getAsString());
                    IOption<?> unknownOption = optionMap.get(key);
                    if (unknownOption == null) {
                        throw new JsonSyntaxException("Unknown option type: " + key + " can not finish parsing recipe.");
                    }
                    Options parsedOption = unknownOption.create(key, singleOption);
                    options.add(parsedOption);
                }
            }
        }
        return options;
    }
}
