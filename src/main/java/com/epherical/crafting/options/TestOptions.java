package com.epherical.crafting.options;

import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

public abstract class TestOptions extends Options {

    private final String failMessage;

    public TestOptions(NamespacedKey key, JsonObject object) {
        super(key);
        this.failMessage = JsonUtil.getValue(object, "fail-message").getAsString();
    }

    public abstract boolean test(OptionContext context);

    public String getFailMessage() {
        return failMessage;
    }

    @Override
    public void serialize(JsonObject object) {
        object.addProperty("fail-message", failMessage);
    }
}
