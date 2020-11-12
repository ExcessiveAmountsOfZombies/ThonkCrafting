package com.epherical.crafting.options;


import com.google.gson.JsonObject;

public abstract class TestOptions extends Options {

    private String failMessage;

    public TestOptions(String failMessage) {
        this.failMessage = failMessage;
    }

    public abstract boolean test(OptionContext context);

    public String getFailureMessage(OptionContext context) {
        return failMessage;
    }


    public Options createInstance(JsonObject object) {
        this.failMessage = object.getAsJsonPrimitive("fail-message").getAsString();
        return this;
    }

    public String getFailMessage() {
        return failMessage;
    }
}
