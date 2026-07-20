package com.ftl2nd.knuawhitelistblock.command.sub;

import com.ftl2nd.knuawhitelistblock.KnuaWhitelistBlock;
import com.ftl2nd.knuawhitelistblock.command.SubCommand;
import com.ftl2nd.knuawhitelistblock.config.LangConfig;
import com.ftl2nd.knuawhitelistblock.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public final class ReloadCommand implements SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload configuration";
    }

    @Override
    public String getSyntax() {
        return "/kwb reload";
    }

    @Override
    public String getPermission() {
        return "knuawhitelistblock.admin";
    }

    @Override
    public void execute(CommandSender sender, String[] args, KnuaWhitelistBlock plugin) {
        plugin.reloadPluginConfig();
        LangConfig lang = plugin.getLangConfig();
        String prefix = (lang != null && lang.prefix != null) ? lang.prefix : "<gradient:#00C9FF:#92FE9D><bold>KnuaWhitelistBlock</bold></gradient> <dark_gray>»</dark_gray> ";
        String msg = (lang != null && lang.reload != null) ? lang.reload : "<#55efc4>Configuration and languages reloaded successfully.</#55efc4>";

        if (sender instanceof org.bukkit.entity.Player player) {
            MessageUtil.sendMessage(player, msg, prefix);
        } else {
            sender.sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage()
                    .deserialize(prefix + msg));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args, KnuaWhitelistBlock plugin) {
        return Collections.emptyList();
    }
}
