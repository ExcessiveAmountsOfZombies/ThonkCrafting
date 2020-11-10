package com.epherical.crafting;

public class DataPackAdder {


    public static void addDataPackLocation(ThonkCrafting plugin) {
        /*DedicatedServer server = ((CraftServer) this.getServer()).getServer();
        ResourcePackRepository serverRepository = server.getResourcePackRepository();

        File dataFolder = new File(this.getDataFolder(), "thonkcrafting");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        DataPackConfiguration configuration = new DataPackConfiguration(ImmutableList.of("file/thonkcrafting"), ImmutableList.of());

        ResourcePackRepository pluginRepo = null;
        try {
            Field packSource = serverRepository.getClass().getDeclaredField("a");
            Field modifierField = Field.class.getDeclaredField("modifiers");
            packSource.setAccessible(true);
            modifierField.setAccessible(true);
            modifierField.setInt(packSource, packSource.getModifiers() & ~Modifier.FINAL);


            // server repo
            ImmutableSet<ResourcePackSource> sources = (ImmutableSet<ResourcePackSource>) packSource.get(serverRepository);
            pluginRepo = new ResourcePackRepository(
                    sources.toArray(new ResourcePackSource[]{new ResourcePackSourceFolder(this.getDataFolder(), PackSource.d)}));

            MinecraftServer.a(serverRepository, configuration, false);
            CompletableFuture<DataPackResources> future = DataPackResources.a(pluginRepo.f(), CommandDispatcher.ServerType.DEDICATED, server.propertyManager.getProperties().functionPermissionLevel, SystemUtils.f(), Runnable::run);

            DataPackResources datapackresources = future.get();
            datapackresources.i();

        } catch (NoSuchFieldException | IllegalAccessException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            pluginRepo.close();
        }*/
    }
}
