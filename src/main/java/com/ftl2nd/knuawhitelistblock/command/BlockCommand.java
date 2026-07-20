package com.ftl2nd.knuawhitelistblock.command;

import com.ftl2nd.knuawhitelistblock.KnuaWhitelistBlock;
import com.ftl2nd.knuawhitelistblock.command.sub.BreakCommand;
import com.ftl2nd.knuawhitelistblock.command.sub.ListCommand;
import com.ftl2nd.knuawhitelistblock.command.sub.PlaceCommand;
import com.ftl2nd.knuawhitelistblock.command.sub.ReloadCommand;
import com.ftl2nd.knuawhitelistblock.config.LangConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public final class BlockCommand implements CommandExecutor, TabCompleter {

    private final KnuaWhitelistBlock plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BlockCommand(KnuaWhitelistBlock plugin) {
        this.plugin = plugin;

        // Cache block materials once for performance
        List<String> blockList = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isBlock() && !material.isLegacy()) {
                blockList.add(material.name().toLowerCase());
            }
        }
        Collections.sort(blockList);
        List<String> unmodifiableBlocks = Collections.unmodifiableList(blockList);

        // Register SubCommands
        registerSub(new ReloadCommand());
        registerSub(new ListCommand());
        registerSub(new PlaceCommand(unmodifiableBlocks));
        registerSub(new BreakCommand(unmodifiableBlocks));
    }

    private void registerSub(SubCommand sub) {
        subCommands.put(sub.getName().toLowerCase(), sub);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LangConfig lang = plugin.getLangConfig();
        String prefix = lang != null && lang.prefix != null ? lang.prefix : "";

        if (lang == null) {
            sender.sendMessage("Plugin is not fully loaded.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender, prefix);
            return true;
        }

        String subName = args[0].toLowerCase();
        SubCommand sub = subCommands.get(subName);

        if (sub == null) {
            sendHelp(sender, prefix);
            return true;
        }

        // Check Permission
        if (!sender.hasPermission(sub.getPermission())) {
            sender.sendMessage(miniMessage.deserialize(prefix + lang.noPermission));
            return true;
        }

        // Execute SubCommand
        try {
            sub.execute(sender, args, plugin);
        } catch (Exception e) {
            sender.sendMessage(miniMessage.deserialize(prefix + "<red>An error occurred executing this command.</red>"));
            plugin.getLogger().log(java.util.logging.Level.SEVERE, "Error executing subcommand: " + subName, e);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission("knuawhitelistblock.admin")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>(subCommands.keySet());
            suggestions.add("help");
            return filterSuggestions(suggestions, args[0]);
        }

        String subName = args[0].toLowerCase();
        SubCommand sub = subCommands.get(subName);

        if (sub != null) {
            return sub.tabComplete(sender, args, plugin);
        }

        return Collections.emptyList();
    }

    private void sendHelp(CommandSender sender, String prefix) {
        sender.sendMessage(miniMessage.deserialize(prefix + "<#00C9FF><bold>KnuaWhitelistBlock commands:</bold></#00C9FF>"));
        for (SubCommand sub : subCommands.values()) {
            String format = prefix + "<gray>• </gray><click:suggest_command:\"" + sub.getSyntax() + "\"><hover:show_text:\"<gradient:#00C9FF:#92FE9D>Click to suggest: " + sub.getSyntax() + "</gradient>\"><yellow>" + sub.getSyntax() + "</yellow></hover></click> - <gray>" + sub.getDescription() + "</gray>";
            sender.sendMessage(miniMessage.deserialize(format));
        }
    }

    private List<String> filterSuggestions(List<String> list, String query) {
        String q = query.toLowerCase();
        return list.stream().filter(s -> s.toLowerCase().startsWith(q)).collect(Collectors.toList());
    }
}
