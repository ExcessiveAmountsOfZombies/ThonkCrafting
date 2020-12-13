package com.epherical.crafting.options;

import com.google.gson.JsonObject;

public abstract class TestOptions extends Options {

    private String failMessage;

    public TestOptions(String failMessage) {
        this.failMessage = failMessage;
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
