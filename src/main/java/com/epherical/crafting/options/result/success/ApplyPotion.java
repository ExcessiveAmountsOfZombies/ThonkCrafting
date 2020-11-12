package com.epherical.crafting.options.result.success;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.SuccessOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ApplyPotion extends SuccessOptions {

    private final PotionEffectType type;
    private final int lengthInTicks;
    private final int amplifier;

    public ApplyPotion(String potionEffectName, int lengthInTicks, int amplifier) throws NullPointerException {
        this.type = PotionEffectType.getByName(potionEffectName);
        this.lengthInTicks = lengthInTicks;
        this.amplifier = amplifier;
    }

    public ApplyPotion(NamespacedKey key, JsonObject object) {
        this.type = PotionEffectType.getByName(JsonUtil.getValue(object, "potion-effect").getAsString());
        this.lengthInTicks = JsonUtil.getValue(object, "length-in-ticks").getAsInt();
        this.amplifier = JsonUtil.getValue(object, "amplifier").getAsInt();
    }

    @Override
    public void playOption(OptionContext context) {
        context.getPlayer().addPotionEffect(new PotionEffect(type, lengthInTicks, amplifier));
    }
}
