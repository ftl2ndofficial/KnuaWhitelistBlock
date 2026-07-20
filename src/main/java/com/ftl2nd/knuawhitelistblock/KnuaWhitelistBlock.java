package com.ftl2nd.knuawhitelistblock;

import com.ftl2nd.knuawhitelistblock.command.BlockCommand;
import com.ftl2nd.knuawhitelistblock.config.AppConfig;
import com.ftl2nd.knuawhitelistblock.config.ConfigProcessor;
import com.ftl2nd.knuawhitelistblock.config.LangConfig;
import com.ftl2nd.knuawhitelistblock.listener.BlockEventListener;
import com.ftl2nd.knuawhitelistblock.manager.BlockRuleManager;
import com.ftl2nd.knuawhitelistblock.util.SchedulerUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class KnuaWhitelistBlock extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 32782;

    private volatile AppConfig appConfig;
    private volatile LangConfig langConfig;
    private BlockRuleManager ruleManager;

    @Override
    public void onEnable() {
        // 1. Initialize and load configurations
        reloadPluginConfig();

        // 2. Initialize rule manager
        this.ruleManager = new BlockRuleManager(this);

        // 3. Register listeners
        getServer().getPluginManager().registerEvents(new BlockEventListener(this), this);

        // 4. Register command
        BlockCommand blockCommand = new BlockCommand(this);
        PluginCommand cmd = getCommand("knuawhitelistblock");
        if (cmd != null) {
            cmd.setExecutor(blockCommand);
            cmd.setTabCompleter(blockCommand);
        }

        // 5. Initialize metrics
        try {
            new Metrics(this, BSTATS_PLUGIN_ID);
        } catch (Exception e) {
            getLogger().warning("Failed to initialize bStats metrics.");
        }

        logColored("");
        logColored("§b __  __     __     __     ______    ");
        logColored("§b/\\ \\/ /    /\\ \\  _ \\ \\   /\\  == \\   ");
        logColored("§b\\ \\  _\"-.  \\ \\ \\/ \".\\ \\  \\ \\  __<   ");
        logColored("§b \\ \\_\\ \\_\\  \\ \\__/\".~\\_\\  \\ \\_____\\ ");
        logColored("§b  \\/_/\\/_/   \\/_/   \\/_/   \\/_____/ ");
        logColored("");
        logColored("§7Version: §f" + getDescription().getVersion() + "§7 | Author: §fftl2nd");
        logColored("§7Website: §bhttps://discord.knualabs.icu");
        logColored("§7Platform: §e" + (SchedulerUtil.isFolia() ? "Folia" : "Paper/Spigot") + "§7 | Status: §aEnabled");
        logColored("");
    }

    private void logColored(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    @Override
    public void onDisable() {
        getLogger().info("KnuaWhitelistBlock has been disabled.");
    }

    /**
     * Reloads configuration and translation settings atomically.
     * Prevents race conditions during event handling by using volatile swaps.
     */
    public synchronized void reloadPluginConfig() {
        AppConfig newAppConfig = ConfigProcessor.load(this, "config.yml", new AppConfig());

        String lang = newAppConfig.language != null ? newAppConfig.language : "en";
        LangConfig.loadingLanguage = lang;

        LangConfig newLangConfig = ConfigProcessor.load(
                this, 
                "languages/messages_" + lang.toLowerCase() + ".yml", 
                new LangConfig()
        );

        // Atomic swap of references
        this.appConfig = newAppConfig;
        this.langConfig = newLangConfig;
    }

    public AppConfig getAppConfig() {
        return this.appConfig;
    }

    public LangConfig getLangConfig() {
        return this.langConfig;
    }

    public BlockRuleManager getRuleManager() {
        return this.ruleManager;
    }
}
