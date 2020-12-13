package com.epherical.crafting.options;


import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.NamespacedKey;

public abstract class Options {

    public interface IOption<OPT extends Options> {
        OPT create(NamespacedKey key, JsonObject object);
    }

    public BaseComponent[] textDisplay() {
        return new BaseComponent[]{};
    }

    public abstract void serialize(JsonObject object);
}
