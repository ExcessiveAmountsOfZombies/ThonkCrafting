package com.epherical.crafting.listener;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.ui.Menu;
import com.epherical.crafting.ui.click.ContainerButton;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {

    private ThonkCrafting thonkCrafting;

    private Map<UUID, ContainerButton<?>> awaitingResponses = new HashMap<>();
    private Map<UUID, Menu> savedMenu = new HashMap<>();

    public ChatListener(ThonkCrafting thonkCrafting) {
        this.thonkCrafting = thonkCrafting;
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (awaitingResponses.containsKey(uuid)) {
            ContainerButton<Object> button = (ContainerButton<Object>) awaitingResponses.get(uuid);
            String message = event.getMessage();
            try {
                button.setContainer(NumberFormat.getInstance().parse(message));
            } catch (ParseException | ClassCastException e) {
                event.getPlayer().sendMessage("Improper input for request. Make sure to input a number.");
            }

            event.setMessage("");
            event.setCancelled(true);

            removeResponseRequest(uuid);
            Bukkit.getScheduler().runTask(thonkCrafting, () -> {
                savedMenu.get(uuid).openInventory(event.getPlayer());
                removeSavedMenu(uuid);
            });


        }
    }

    public void addResponseRequest(UUID uuid, ContainerButton<?> numberButton) {
        this.awaitingResponses.put(uuid, numberButton);
    }

    public void removeResponseRequest(UUID uuid) {
        this.awaitingResponses.remove(uuid);
    }

    public void addSavedMenu(UUID uuid, Menu menu) {
        this.savedMenu.put(uuid, menu);
    }

    public void removeSavedMenu(UUID uuid) {
        this.savedMenu.remove(uuid);
    }
}
