package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.NamespacedKey;

public class Cooldown extends TestOptions {

    private final long delayInSeconds;

    public Cooldown(String failMessage, long delayInSeconds) {
        super(failMessage);
        this.delayInSeconds = delayInSeconds;
    }

    public Cooldown(NamespacedKey key, JsonObject object) {
        super(JsonUtil.getValue(object, "fail-message").getAsString());
        this.delayInSeconds = JsonUtil.getValue(object, "cooldown-in-seconds").getAsLong();
    }

    @Override
    public boolean test(OptionContext context) {
        // TODO: Cooldown
        return true;
    }

    @Override
    public String toString() {
        return "Cooldown is " + delayInSeconds + " seconds after crafting";
    }

    @Override
    public BaseComponent[] textDisplay() {
        ComponentBuilder builder = new ComponentBuilder("Cooldown in seconds after crafting: ").color(ChatColor.DARK_GRAY).italic(false);
        builder.append(String.valueOf(delayInSeconds)).color(ChatColor.GRAY).italic(false);
        return builder.create();
    }
}
