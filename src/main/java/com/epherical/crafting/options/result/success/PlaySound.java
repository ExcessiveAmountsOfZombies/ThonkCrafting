package com.epherical.crafting.options.result.success;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.SuccessOptions;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public class PlaySound extends SuccessOptions {

    private final String sound;
    private final float volume;
    private final float pitch;

    public PlaySound(String sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public PlaySound(NamespacedKey key, JsonObject object) {
        this.sound = object.getAsJsonPrimitive("sound").getAsString();
        this.volume = object.getAsJsonPrimitive("volume").getAsFloat();
        this.pitch = object.getAsJsonPrimitive("pitch").getAsFloat();
    }

    @Override
    public void playOption(OptionContext context) {
        Location location = context.getPlayer().getLocation();
        context.getPlayer().playSound(location, sound, volume, pitch);
    }
}
