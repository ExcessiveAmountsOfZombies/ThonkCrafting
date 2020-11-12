package com.epherical.crafting.options;

public abstract class TestOptions extends Options {

    private String failMessage;

    public TestOptions(String failMessage) {
        this.failMessage = failMessage;
    }

    public abstract boolean test(OptionContext context);

    public String getFailMessage() {
        return failMessage;
    }
}
