package com.epherical.crafting.options;

import org.bukkit.entity.Player;

public class OptionContext {

    private Player player;

    public OptionContext(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
