package com.epherical.crafting.options;


import org.bukkit.NamespacedKey;

public abstract class SuccessOptions extends Options {

    public SuccessOptions(NamespacedKey key) {
        super(key);
    }

    public abstract void playOption(OptionContext context);

}
