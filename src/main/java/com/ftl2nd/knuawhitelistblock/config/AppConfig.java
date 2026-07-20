package com.ftl2nd.knuawhitelistblock.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class AppConfig extends BaseConfig {

    public int configVersion = 1;
    public String language = "en";
    public boolean debug = false;

    public String defaultPlaceMode = "blacklist";
    public List<Material> defaultPlaceBlocks = Arrays.asList(Material.TNT, Material.LAVA);
    public String defaultBreakMode = "blacklist";
    public List<Material> defaultBreakBlocks = List.of(Material.BEDROCK);

    // Dynamic worlds mapping
    private final Map<String, WorldRule> worldRules = new ConcurrentHashMap<>();
    private volatile WorldRule defaultRule;

    public WorldRule getDefaultRule() {
        return defaultRule;
    }

    public Map<String, WorldRule> getWorldRules() {
        return worldRules;
    }

    @Override
    public void onLoad(FileConfiguration config) {
        this.configVersion = config.getInt("config-version", 1);
        this.language = config.getString("language", "en");
        this.debug = config.getBoolean("debug", false);

        this.defaultPlaceMode = config.getString("rules.default.place-mode", "blacklist");
        this.defaultPlaceBlocks = parseMaterialList(config.getStringList("rules.default.place-blocks"), Arrays.asList(Material.TNT, Material.LAVA));

        this.defaultBreakMode = config.getString("rules.default.break-mode", "blacklist");
        this.defaultBreakBlocks = parseMaterialList(config.getStringList("rules.default.break-blocks"), List.of(Material.BEDROCK));

        // Initialize default rules
        defaultRule = new WorldRule(defaultPlaceMode, defaultPlaceBlocks, defaultBreakMode, defaultBreakBlocks);

        // Load world-specific overrides
        worldRules.clear();
        ConfigurationSection worldsSection = config.getConfigurationSection("rules.worlds");
        if (worldsSection != null) {
            for (String worldName : worldsSection.getKeys(false)) {
                ConfigurationSection ws = worldsSection.getConfigurationSection(worldName);
                if (ws != null) {
                    String pMode = ws.getString("place-mode", "blacklist");
                    List<Material> pBlocks = parseMaterialList(ws.getStringList("place-blocks"), Collections.emptyList());
                    String bMode = ws.getString("break-mode", "blacklist");
                    List<Material> bBlocks = parseMaterialList(ws.getStringList("break-blocks"), Collections.emptyList());
                    worldRules.put(worldName, new WorldRule(pMode, pBlocks, bMode, bBlocks));
                }
            }
        }
    }

    @Override
    public void onSave(FileConfiguration config) {
        config.set("config-version", this.configVersion);
        config.setComments("config-version", List.of("Internal configuration version. Do not modify."));

        config.set("language", this.language);
        config.setComments("language", List.of("Default plugin language (en: English, vi: Vietnamese)"));

        config.set("debug", this.debug);
        config.setComments("debug", List.of("Enable debug logging in console for place/break actions"));

        config.set("rules.default.place-mode", this.defaultPlaceMode);
        config.setComments("rules.default.place-mode", List.of("Default place rule type (blacklist / whitelist)"));
        config.set("rules.default.place-blocks", this.defaultPlaceBlocks.stream().map(Material::name).collect(Collectors.toList()));
        config.setComments("rules.default.place-blocks", List.of("Default place block list"));

        config.set("rules.default.break-mode", this.defaultBreakMode);
        config.setComments("rules.default.break-mode", List.of("Default break rule type (blacklist / whitelist)"));
        config.set("rules.default.break-blocks", this.defaultBreakBlocks.stream().map(Material::name).collect(Collectors.toList()));
        config.setComments("rules.default.break-blocks", List.of("Default break block list"));

        // Save world rules back to YAML
        config.set("rules.worlds", null); // Clear existing section to avoid leftovers
        for (Map.Entry<String, WorldRule> entry : worldRules.entrySet()) {
            String path = "rules.worlds." + entry.getKey();
            config.set(path + ".place-mode", entry.getValue().placeMode);
            config.set(path + ".place-blocks", entry.getValue().placeBlocks.stream().map(Material::name).collect(Collectors.toList()));
            config.set(path + ".break-mode", entry.getValue().breakMode);
            config.set(path + ".break-blocks", entry.getValue().breakBlocks.stream().map(Material::name).collect(Collectors.toList()));
        }
    }

    private List<Material> parseMaterialList(List<String> rawList, List<Material> fallback) {
        if (rawList == null || rawList.isEmpty()) {
            return fallback;
        }
        List<Material> list = new ArrayList<>();
        for (String s : rawList) {
            if (s == null || s.trim().isEmpty()) continue;
            Material mat = Material.matchMaterial(s.toUpperCase().trim());
            if (mat != null && mat.isBlock()) {
                list.add(mat);
            } else {
                Bukkit.getLogger().warning("[KnuaWhitelistBlock] Config validation: Ignored invalid block material '" + s + "'");
            }
        }
        return list.isEmpty() ? fallback : list;
    }

    public static class WorldRule {
        public final String placeMode;
        public final Set<Material> placeBlocks;
        public final String breakMode;
        public final Set<Material> breakBlocks;

        public WorldRule(String placeMode, Collection<?> placeBlocks, String breakMode, Collection<?> breakBlocks) {
            String pMode = placeMode != null ? placeMode.toLowerCase() : "blacklist";
            this.placeMode = pMode.equals("whitelist") ? "whitelist" : "blacklist";

            String bMode = breakMode != null ? breakMode.toLowerCase() : "blacklist";
            this.breakMode = bMode.equals("whitelist") ? "whitelist" : "blacklist";

            this.placeBlocks = ConcurrentHashMap.newKeySet();
            if (placeBlocks != null) {
                for (Object obj : placeBlocks) {
                    if (obj == null) continue;
                    Material mat = null;
                    if (obj instanceof Material m) {
                        mat = m;
                    } else if (obj instanceof String s) {
                        if (s.trim().isEmpty()) continue;
                        mat = Material.matchMaterial(s.toUpperCase().trim());
                    }
                    if (mat == null || !mat.isBlock()) {
                        Bukkit.getLogger().warning("[KnuaWhitelistBlock] Config validation: Ignored invalid block material '" + obj + "'");
                    } else {
                        this.placeBlocks.add(mat);
                    }
                }
            }

            this.breakBlocks = ConcurrentHashMap.newKeySet();
            if (breakBlocks != null) {
                for (Object obj : breakBlocks) {
                    if (obj == null) continue;
                    Material mat = null;
                    if (obj instanceof Material m) {
                        mat = m;
                    } else if (obj instanceof String s) {
                        if (s.trim().isEmpty()) continue;
                        mat = Material.matchMaterial(s.toUpperCase().trim());
                    }
                    if (mat == null || !mat.isBlock()) {
                        Bukkit.getLogger().warning("[KnuaWhitelistBlock] Config validation: Ignored invalid block material '" + obj + "'");
                    } else {
                        this.breakBlocks.add(mat);
                    }
                }
            }
        }
    }
}
