package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;


public class Permission extends TestOptions {
    private final String permissionNode;

    public Permission(String failMessage, String permissionNode) {
        super(failMessage);
        this.permissionNode = permissionNode;
    }


    @Override
    public boolean test(OptionContext context) {
        return context.getPlayer().hasPermission(permissionNode);
    }
}
