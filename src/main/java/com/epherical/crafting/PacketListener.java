package com.epherical.crafting;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.epherical.crafting.api.CustomRecipe;
import net.minecraft.server.v1_16_R2.IRecipe;

import java.util.ArrayList;

public class PacketListener {


    public static void addPacketRecipeListener(ProtocolManager manager, ThonkCrafting plugin) {
        manager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.RECIPE_UPDATE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Server.RECIPE_UPDATE) {
                            PacketContainer container = event.getPacket();
                            container.getAttributeCollectionModifier().getField(0).setAccessible(true);
                            try {
                                ArrayList<IRecipe<?>> recipes = (ArrayList<IRecipe<?>>) container.getAttributeCollectionModifier().getField(0).get(container.getHandle());
                                ArrayList<IRecipe<?>> copy = new ArrayList<>();


                                for (IRecipe<?> recipe : recipes) {
                                    if (recipe instanceof CustomRecipe) {
                                        copy.add(((CustomRecipe) recipe).getVanillaRecipe());
                                    } else {
                                        copy.add(recipe);
                                    }
                                }
                                recipes.clear();
                                recipes.addAll(copy);

                                /*recipes.removeIf(iRecipe -> {

                                    if (iRecipe instanceof InternalPermissionRecipe) {
                                        return true;
                                    }

                                    if (iRecipe instanceof InternalDamageDurabilityRecipe) {
                                        System.out.println("Removed Custom recipe class" + iRecipe.getClass());
                                        return true;
                                    }
                                    return false;
                                });*/
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }
}
