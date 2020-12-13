package com.epherical.crafting.ui;

import com.epherical.crafting.ui.click.ContainerButton;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PageMenu {

    private Player player;

    private ArrayList<ContainerButton<?>> availableContainers = new ArrayList<>();
    private int currentPage = 0;
    private int maxPages;

    public PageMenu(Player player) {
        this.player = player;
    }


    public void setContainers(ArrayList<ContainerButton<?>> buttons) {
        this.availableContainers= buttons;
        this.currentPage = 0;
        int size = buttons.size();
    }
}
