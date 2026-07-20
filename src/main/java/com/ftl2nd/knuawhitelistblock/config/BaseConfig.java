package com.ftl2nd.knuawhitelistblock.config;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class BaseConfig {
    public void onLoad(FileConfiguration config) {}
    public void onSave(FileConfiguration config) {}
}
