package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;

public class Cooldown extends TestOptions {

    private final long delayInSeconds;

    public Cooldown(String failMessage, long delayInSeconds) {
        super(failMessage);
        this.delayInSeconds = delayInSeconds;
    }

    @Override
    public boolean test(OptionContext context) {
        // TODO: Cooldown
        return false;
    }
}
