package com.epherical.crafting.listener;

import com.epherical.crafting.ThonkCrafting;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Set;


public class CookingBlockListener implements Listener {

    private ThonkCrafting plugin;

    public CookingBlockListener(ThonkCrafting plugin) {
        this.plugin = plugin;
    }
    
    private final Set<Material> blocksWithHiddenRecipes = ImmutableSet.of(
            Material.FURNACE,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.SMOKER,
            Material.BLAST_FURNACE);


    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (!event.isCancelled() && event.getClickedBlock() != null) {
            Material materialClicked = event.getClickedBlock().getType();
            if (blocksWithHiddenRecipes.contains(materialClicked)) {
                event.getClickedBlock().setMetadata(ThonkCrafting.USED_BY, new FixedMetadataValue(plugin, event.getPlayer().getUniqueId()));
            }
        }
    }
}
