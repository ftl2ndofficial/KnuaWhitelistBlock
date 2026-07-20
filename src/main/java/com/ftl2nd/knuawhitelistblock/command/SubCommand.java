package com.ftl2nd.knuawhitelistblock.command;

import com.ftl2nd.knuawhitelistblock.KnuaWhitelistBlock;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String getName();
    String getDescription();
    String getSyntax();
    String getPermission();
    void execute(CommandSender sender, String[] args, KnuaWhitelistBlock plugin);
    List<String> tabComplete(CommandSender sender, String[] args, KnuaWhitelistBlock plugin);
}
