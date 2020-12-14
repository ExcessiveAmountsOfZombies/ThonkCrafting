package com.epherical.crafting.options.result.success;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.SuccessOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public class PlaySound extends SuccessOptions {

    private final String sound;
    private final float volume;
    private final float pitch;

    public PlaySound(NamespacedKey key, JsonObject object) {
        super(key);
        this.sound = JsonUtil.getValue(object, "sound").getAsString();
        this.volume = JsonUtil.getValue(object, "volume").getAsFloat();
        this.pitch = JsonUtil.getValue(object, "pitch").getAsFloat();
    }

    @Override
    public void playOption(OptionContext context) {
        Location location = context.getPlayer().getLocation();
        context.getPlayer().playSound(location, sound, volume, pitch);
    }

    @Override
    public String toString() {
        return "Will play sound: " + sound + " at " + volume + " volume and " + pitch + " pitch";
    }

    @Override
    public void serialize(JsonObject object) {
        super.serialize(object);
        object.addProperty("sound", sound);
        object.addProperty("volume", volume);
        object.addProperty("pitch", pitch);
    }
}
