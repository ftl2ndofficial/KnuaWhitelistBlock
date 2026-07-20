package com.ftl2nd.knuawhitelistblock.manager;

import com.ftl2nd.knuawhitelistblock.KnuaWhitelistBlock;
import com.ftl2nd.knuawhitelistblock.config.AppConfig;
import com.ftl2nd.knuawhitelistblock.config.ConfigProcessor;
import com.ftl2nd.knuawhitelistblock.util.SchedulerUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlockRuleManager {

    private final KnuaWhitelistBlock plugin;
    private final Object saveLock = new Object();

    public BlockRuleManager(KnuaWhitelistBlock plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if a block placement is allowed in the specified world.
     */
    public boolean canPlace(String worldName, Material material) {
        AppConfig config = plugin.getAppConfig();
        if (config == null) {
            return true;
        }

        AppConfig.WorldRule rule = config.getWorldRules().get(worldName);
        if (rule == null) {
            rule = config.getDefaultRule();
        }

        if (rule == null) {
            return true;
        }

        if (rule.placeMode.equals("whitelist")) {
            return rule.placeBlocks.contains(material);
        } else {
            // blacklist
            return !rule.placeBlocks.contains(material);
        }
    }

    /**
     * Checks if a block breaking is allowed in the specified world.
     */
    public boolean canBreak(String worldName, Material material) {
        AppConfig config = plugin.getAppConfig();
        if (config == null) {
            return true;
        }

        AppConfig.WorldRule rule = config.getWorldRules().get(worldName);
        if (rule == null) {
            rule = config.getDefaultRule();
        }

        if (rule == null) {
            return true;
        }

        if (rule.breakMode.equals("whitelist")) {
            return rule.breakBlocks.contains(material);
        } else {
            // blacklist
            return !rule.breakBlocks.contains(material);
        }
    }

    /**
     * Adds a block to the place rules for a specific world.
     * Returns true if the block was successfully added, false if it was already registered.
     */
    public boolean addPlaceRule(String worldName, Material material) {
        AppConfig config = plugin.getAppConfig();
        if (config == null) return false;

        AppConfig.WorldRule rule = config.getWorldRules().computeIfAbsent(worldName, w ->
                new AppConfig.WorldRule(config.defaultPlaceMode, new ArrayList<>(), config.defaultBreakMode, new ArrayList<>())
        );

        boolean added = rule.placeBlocks.add(material);
        if (added) {
            saveConfigAsync();
        }
        return added;
    }

    /**
     * Removes a block from the place rules for a specific world.
     */
    public boolean removePlaceRule(String worldName, Material material) {
        AppConfig config = plugin.getAppConfig();
        if (config == null) return false;

        AppConfig.WorldRule rule = config.getWorldRules().get(worldName);
        if (rule == null) {
            rule = config.getWorldRules().computeIfAbsent(worldName, w ->
                    new AppConfig.WorldRule(config.defaultPlaceMode, new ArrayList<>(config.defaultPlaceBlocks), config.defaultBreakMode, new ArrayList<>(config.defaultBreakBlocks))
            );
        }

        boolean removed = rule.placeBlocks.remove(material);
        if (removed) {
            saveConfigAsync();
        }
        return removed;
    }

    /**
     * Adds a block to the break rules for a specific world.
     * Returns true if the block was successfully added, false if it was already registered.
     */
    public boolean addBreakRule(String worldName, Material material) {
        AppConfig config = plugin.getAppConfig();
        if (config == null) return false;

        AppConfig.WorldRule rule = config.getWorldRules().computeIfAbsent(worldName, w ->
                new AppConfig.WorldRule(config.defaultPlaceMode, new ArrayList<>(), config.defaultBreakMode, new ArrayList<>())
        );

        boolean added = rule.breakBlocks.add(material);
        if (added) {
            saveConfigAsync();
        }
        return added;
    }

    /**
     * Removes a block from the break rules for a specific world.
     */
    public boolean removeBreakRule(String worldName, Material material) {
        AppConfig config = plugin.getAppConfig();
        if (config == null) return false;

        AppConfig.WorldRule rule = config.getWorldRules().get(worldName);
        if (rule == null) {
            rule = config.getWorldRules().computeIfAbsent(worldName, w ->
                    new AppConfig.WorldRule(config.defaultPlaceMode, new ArrayList<>(config.defaultPlaceBlocks), config.defaultBreakMode, new ArrayList<>(config.defaultBreakBlocks))
            );
        }

        boolean removed = rule.breakBlocks.remove(material);
        if (removed) {
            saveConfigAsync();
        }
        return removed;
    }

    /**
     * Schedules an asynchronous save of the application config.
     * Synchronized on saveLock to avoid concurrent file write issues.
     */
    public void saveConfigAsync() {
        SchedulerUtil.runAsync(plugin, () -> {
            synchronized (saveLock) {
                ConfigProcessor.save(plugin, "config.yml", plugin.getAppConfig());
            }
        });
    }
}
