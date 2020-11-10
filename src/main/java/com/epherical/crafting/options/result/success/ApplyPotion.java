package com.epherical.crafting.options.result.success;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.SuccessOptions;
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

    @Override
    public void playOption(OptionContext context) {
        context.getPlayer().addPotionEffect(new PotionEffect(type, lengthInTicks, amplifier));
    }
}
