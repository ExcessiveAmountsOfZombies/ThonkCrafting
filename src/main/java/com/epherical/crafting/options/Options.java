package com.epherical.crafting.options;


import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

public abstract class Options {

    public interface IOption<OPT extends Options> {
        OPT create(NamespacedKey key, JsonObject object);
    }
}
