package com.ftl2nd.knuawhitelistblock.command.sub;

import com.ftl2nd.knuawhitelistblock.KnuaWhitelistBlock;
import com.ftl2nd.knuawhitelistblock.command.SubCommand;
import com.ftl2nd.knuawhitelistblock.config.AppConfig;
import com.ftl2nd.knuawhitelistblock.config.LangConfig;
import com.ftl2nd.knuawhitelistblock.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ListCommand implements SubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List break/place rules";
    }

    @Override
    public String getSyntax() {
        return "/kwb list [world|all]";
    }

    @Override
    public String getPermission() {
        return "knuawhitelistblock.admin";
    }

    @Override
    public void execute(CommandSender sender, String[] args, KnuaWhitelistBlock plugin) {
        LangConfig lang = plugin.getLangConfig();
        if (lang == null) return;

        String prefix = lang.prefix != null ? lang.prefix : "";
        String targetWorld = args.length > 1 ? args[1] : "all";

        displayRules(sender, targetWorld, plugin, lang, prefix);
    }

    private void displayRules(CommandSender sender, String targetWorld, KnuaWhitelistBlock plugin, LangConfig lang, String prefix) {
        AppConfig config = plugin.getAppConfig();
        if (config == null) return;

        if (targetWorld.equalsIgnoreCase("all")) {
            sendWorldRuleInfo(sender, "default", config.getDefaultRule(), lang, prefix);
            for (Map.Entry<String, AppConfig.WorldRule> entry : config.getWorldRules().entrySet()) {
                sendWorldRuleInfo(sender, entry.getKey(), entry.getValue(), lang, prefix);
            }
        } else {
            AppConfig.WorldRule rule = config.getWorldRules().get(targetWorld);
            if (rule == null) {
                World world = Bukkit.getWorld(targetWorld);
                if (world == null && !targetWorld.equalsIgnoreCase("default")) {
                    String nf = lang.worldNotFound != null ? lang.worldNotFound : "<#ff7675>World <yellow>{world}</yellow> does not exist.</#ff7675>";
                    sendMessage(sender, nf.replace("{world}", targetWorld), prefix);
                    return;
                }
                rule = config.getDefaultRule();
            }
            sendWorldRuleInfo(sender, targetWorld, rule, lang, prefix);
        }
    }

    private void sendWorldRuleInfo(CommandSender sender, String worldName, AppConfig.WorldRule rule, LangConfig lang, String prefix) {
        String header = lang.listHeader != null ? lang.listHeader : "<gradient:#00C9FF:#92FE9D><bold>Block List for World: {world}</bold></gradient>";
        sendMessage(sender, header.replace("{world}", worldName), prefix);

        String itemFmt = lang.listItem != null ? lang.listItem : "<gray>Place Mode: <yellow>{place_mode}</yellow> | Break Mode: <yellow>{break_mode}</yellow></gray>";
        String item = itemFmt
                .replace("{place_mode}", rule.placeMode)
                .replace("{break_mode}", rule.breakMode);
        sendMessage(sender, item, "");

        String placeBlocksStr = rule.placeBlocks.stream().map(Material::name).sorted().collect(Collectors.joining(", "));
        String placeFmt = lang.listBlocksPlace != null ? lang.listBlocksPlace : "<gray>Allowed/Blocked Place: <white>{blocks}</white></gray>";
        String placeLine = placeFmt.replace("{blocks}", placeBlocksStr.isEmpty() ? "None" : placeBlocksStr);
        sendMessage(sender, placeLine, "");

        String breakBlocksStr = rule.breakBlocks.stream().map(Material::name).sorted().collect(Collectors.joining(", "));
        String breakFmt = lang.listBlocksBreak != null ? lang.listBlocksBreak : "<gray>Allowed/Blocked Break: <white>{blocks}</white></gray>";
        String breakLine = breakFmt.replace("{blocks}", breakBlocksStr.isEmpty() ? "None" : breakBlocksStr);
        sendMessage(sender, breakLine, "");
    }

    private void sendMessage(CommandSender sender, String msg, String prefix) {
        if (sender instanceof Player player) {
            MessageUtil.sendMessage(player, msg, prefix);
        } else {
            sender.sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(prefix + msg));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args, KnuaWhitelistBlock plugin) {
        if (args.length == 2) {
            List<String> suggestion = new ArrayList<>();
            suggestion.add("all");
            suggestion.add("default");
            for (World world : Bukkit.getWorlds()) {
                suggestion.add(world.getName());
            }
            return filterSuggestions(suggestion, args[1]);
        }
        return List.of();
    }

    private List<String> filterSuggestions(List<String> list, String query) {
        String q = query.toLowerCase();
        return list.stream().filter(s -> s.toLowerCase().startsWith(q)).collect(Collectors.toList());
    }
}
