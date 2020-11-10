package com.epherical.crafting.options;


public abstract class TestOptions extends Options {

    private final String failMessage;

    public TestOptions(String failMessage) {
        this.failMessage = failMessage;
    }

    public abstract boolean test(OptionContext context);

    public String getFailureMessage(OptionContext context) {
        return failMessage;
    }

}
