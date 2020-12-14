package com.epherical.crafting.options.result.success;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.SuccessOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ApplyPotion extends SuccessOptions {

    private final PotionEffectType type;
    private final int lengthInTicks;
    private final int amplifier;

    public ApplyPotion(NamespacedKey key, JsonObject object) {
        super(key);
        this.type = PotionEffectType.getByName(JsonUtil.getValue(object, "potion-effect").getAsString());
        this.lengthInTicks = JsonUtil.getValue(object, "length-in-ticks").getAsInt();
        this.amplifier = JsonUtil.getValue(object, "amplifier").getAsInt();
    }

    @Override
    public void playOption(OptionContext context) {
        context.getPlayer().addPotionEffect(new PotionEffect(type, lengthInTicks, amplifier));
    }

    @Override
    public BaseComponent[] textDisplay() {
        return new ComponentBuilder("Apply potion effect ").color(ChatColor.GRAY)
                .append(type.getName().toLowerCase()).color(ChatColor.DARK_GRAY).bold(true)
                .append(" with a duration of ").color(ChatColor.GRAY)
                .append(String.valueOf((int) lengthInTicks / 20)).color(ChatColor.DARK_GRAY).bold(true)
                .append(" and an amplifier of ").color(ChatColor.GRAY)
                .append(String.valueOf(amplifier)).color(ChatColor.DARK_GRAY).bold(true)
                .create();
    }

    @Override
    public void serialize(JsonObject object) {
        super.serialize(object);
        object.addProperty("potion-effect", type.getName());
        object.addProperty("length-in-ticks", lengthInTicks);
        object.addProperty("amplifier", amplifier);
    }
}
