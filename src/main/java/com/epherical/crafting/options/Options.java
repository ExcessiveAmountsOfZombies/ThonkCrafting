package com.epherical.crafting.options;


import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.NamespacedKey;

public abstract class Options {

    private NamespacedKey key;

    public Options(NamespacedKey key) {
        this.key = key;
    }

    public BaseComponent[] textDisplay() {
        return new BaseComponent[]{};
    }

    public void serialize(JsonObject object) {
        object.addProperty("type", key.toString());
    }

    public interface IOption<OPT extends Options> {
        OPT create(NamespacedKey key, JsonObject object);
    }
}
