package com.ftl2nd.knuawhitelistblock.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class ConfigProcessor {

    private ConfigProcessor() {}

    /**
     * Loads a configuration file into the provided config instance.
     * If the file does not exist, it generates default values from the instance.
     * If the config version is upgraded, it rewrites the file programmatically.
     * Handles syntax errors gracefully to prevent configuration wipes (dump-proof).
     */
    public static <T extends BaseConfig> T load(JavaPlugin plugin, String fileName, T instance) {
        File file = new File(plugin.getDataFolder(), fileName);
        boolean isNewFile = false;

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                isNewFile = true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create configuration file: " + fileName, e);
            }
        }

        YamlConfiguration yamlConfig = new YamlConfiguration();
        try {
            yamlConfig.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().severe("==================================================");
            plugin.getLogger().severe("CRITICAL: Failed to parse configuration file: " + fileName);
            plugin.getLogger().severe("Please check your YAML syntax. Error: " + e.getMessage());
            plugin.getLogger().severe("The plugin will fallback to safe default rules in-memory.");
            plugin.getLogger().severe("==================================================");
            
            // Populate defaults in-memory but DO NOT save to disk to prevent wiping the admin's file
            instance.onLoad(yamlConfig);
            return instance;
        }

        // Load configuration values from YAML into the Java instance
        instance.onLoad(yamlConfig);

        if (isNewFile) {
            // Write defaults to new config file
            instance.onSave(yamlConfig);
            saveYaml(plugin, yamlConfig, file);
        } else {
            // Check for version upgrades to automatically append missing fields
            int fileVersion = yamlConfig.getInt(fileName.contains("config") ? "config-version" : "message-version", 0);
            int codeVersion = 0;

            if (instance instanceof AppConfig appConfig) {
                codeVersion = appConfig.configVersion;
            } else if (instance instanceof LangConfig langConfig) {
                codeVersion = langConfig.messageVersion;
            }

            if (fileVersion < codeVersion) {
                // Save updated configurations back to disk
                instance.onSave(yamlConfig);
                saveYaml(plugin, yamlConfig, file);
            }
        }

        return instance;
    }

    /**
     * Saves the current config instance state back to the disk.
     */
    public static <T extends BaseConfig> void save(JavaPlugin plugin, String fileName, T instance) {
        File file = new File(plugin.getDataFolder(), fileName);
        YamlConfiguration yamlConfig = new YamlConfiguration();
        try {
            // Load current file content first to avoid losing other keys if edited externally
            if (file.exists()) {
                yamlConfig.load(file);
            }
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().severe("CRITICAL: Failed to load config before saving " + fileName + ". Saving aborted to prevent file corruption.");
            return;
        }
        
        instance.onSave(yamlConfig);
        saveYaml(plugin, yamlConfig, file);
    }

    private static void saveYaml(JavaPlugin plugin, FileConfiguration yamlConfig, File file) {
        try {
            yamlConfig.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save configuration file: " + file.getName(), e);
        }
    }
}
