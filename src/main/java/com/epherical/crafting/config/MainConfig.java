package com.epherical.crafting.config;

import com.google.gson.Gson;

import java.io.File;

public class MainConfig {

    private final Gson GSON;
    private File configFile;

    public MainConfig(File file, Gson gson) {
        this.configFile = file;
        this.GSON = gson;
    }
}
