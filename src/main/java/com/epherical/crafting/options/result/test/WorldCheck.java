package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.NamespacedKey;

public class WorldCheck extends TestOptions {

    private final String world;

    public WorldCheck(String failMessage, String world) {
        super(failMessage);
        this.world = world;
    }

    public WorldCheck(NamespacedKey key, JsonObject object) {
        super(JsonUtil.getValue(object, "fail-message").getAsString());
        this.world = JsonUtil.getValue(object, "world-to-check").getAsString();
    }

    @Override
    public boolean test(OptionContext context) {
        return context.getPlayer().getWorld().getName().equalsIgnoreCase(world);
    }

    @Override
    public String toString() {
        return "World required to craft this item: " + world;
    }

    @Override
    public BaseComponent[] textDisplay() {
        ComponentBuilder builder = new ComponentBuilder("World required to be in to craft this item: ").color(ChatColor.DARK_GRAY).italic(false);
        builder.append(world).color(ChatColor.GRAY).italic(false);
        return builder.create();
    }

    @Override
    public void serialize(JsonObject object) {
        super.serialize(object);
        object.addProperty("world-to-check", world);
    }
}
