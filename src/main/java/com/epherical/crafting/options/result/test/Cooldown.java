package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
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
}
