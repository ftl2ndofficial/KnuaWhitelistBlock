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
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class BreakCommand implements SubCommand {

    private final List<String> blockNames;

    public BreakCommand(List<String> blockNames) {
        this.blockNames = blockNames;
    }

    @Override
    public String getName() {
        return "break";
    }

    @Override
    public String getDescription() {
        return "Modify break rules";
    }

    @Override
    public String getSyntax() {
        return "/kwb break <add|remove> [block] [world]";
    }

    @Override
    public String getPermission() {
        return "knuawhitelistblock.admin";
    }

    @Override
    public void execute(CommandSender sender, String[] args, KnuaWhitelistBlock plugin) {
        LangConfig lang = plugin.getLangConfig();
        String prefix = (lang != null && lang.prefix != null) ? lang.prefix : "<gradient:#00C9FF:#92FE9D><bold>KnuaWhitelistBlock</bold></gradient> <dark_gray>»</dark_gray> ";

        if (args.length < 2) {
            sendMessage(sender, "<red>Usage: " + getSyntax() + "</red>", prefix);
            return;
        }

        String action = args[1].toLowerCase();
        if (!action.equals("add") && !action.equals("remove")) {
            sendMessage(sender, "<red>Usage: " + getSyntax() + "</red>", prefix);
            return;
        }

        // Determine Material
        Material material;
        if (args.length >= 3 && !args[2].trim().isEmpty()) {
            String matStr = args[2].toUpperCase();
            material = Material.matchMaterial(matStr);
            if (material == null || !material.isBlock()) {
                String invalidMat = (lang != null && lang.invalidMaterial != null) ? lang.invalidMaterial : "<#ff7675>Material <yellow>{block}</yellow> is not a valid block.</#ff7675>";
                sendMessage(sender, invalidMat.replace("{block}", args[2]), prefix);
                return;
            }
        } else {
            if (!(sender instanceof Player player)) {
                sendMessage(sender, "<red>Console must specify the block name.</red>", prefix);
                return;
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null || item.getType() == Material.AIR) {
                String notHolding = (lang != null && lang.notHoldingBlock != null) ? lang.notHoldingBlock : "<#ff7675>You are not holding any block in your main hand.</#ff7675>";
                sendMessage(sender, notHolding, prefix);
                return;
            }
            material = item.getType();
            if (!material.isBlock()) {
                String invalidMat = (lang != null && lang.invalidMaterial != null) ? lang.invalidMaterial : "<#ff7675>Material <yellow>{block}</yellow> is not a valid block.</#ff7675>";
                sendMessage(sender, invalidMat.replace("{block}", material.name()), prefix);
                return;
            }
        }

        // Determine World
        String worldName;
        if (args.length >= 4 && !args[3].trim().isEmpty()) {
            worldName = args[3];
            if (Bukkit.getWorld(worldName) == null) {
                String wNotFound = (lang != null && lang.worldNotFound != null) ? lang.worldNotFound : "<#ff7675>World <yellow>{world}</yellow> does not exist.</#ff7675>";
                sendMessage(sender, wNotFound.replace("{world}", worldName), prefix);
                return;
            }
        } else {
            if (!(sender instanceof Player player)) {
                sendMessage(sender, "<red>Console must specify the world name.</red>", prefix);
                return;
            }
            worldName = player.getWorld().getName();
        }

        String matName = material.name();
        boolean isAdd = action.equals("add");

        if (isAdd) {
            boolean added = plugin.getRuleManager().addBreakRule(worldName, material);
            if (added) {
                String msg = (lang != null && lang.ruleAdded != null) ? lang.ruleAdded : "<gray>Added <#ffbe76>{block}</#ffbe76> to <yellow>{action}</yellow> rules in world <yellow>{world}</yellow>.</gray>";
                sendMessage(sender, msg.replace("{block}", matName).replace("{action}", "break").replace("{world}", worldName), prefix);
            } else {
                String msg = (lang != null && lang.ruleAlreadyExists != null) ? lang.ruleAlreadyExists : "<red>Block {block} is already in the break rules of world {world}.</red>";
                sendMessage(sender, msg.replace("{block}", matName).replace("{action}", "break").replace("{world}", worldName), prefix);
            }
        } else {
            boolean removed = plugin.getRuleManager().removeBreakRule(worldName, material);
            if (removed) {
                String msg = lang.ruleRemoved != null ? lang.ruleRemoved : "<gray>Removed <#ffbe76>{block}</#ffbe76> from <yellow>{action}</yellow> rules in world <yellow>{world}</yellow>.</gray>";
                sendMessage(sender, msg.replace("{block}", matName).replace("{action}", "break").replace("{world}", worldName), prefix);
            } else {
                String msg = lang.ruleNotFound != null ? lang.ruleNotFound : "<red>Block {block} is not in the break rules of world {world}.</red>";
                sendMessage(sender, msg.replace("{block}", matName).replace("{action}", "break").replace("{world}", worldName), prefix);
            }
        }
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
            return filterSuggestions(List.of("add", "remove"), args[1]);
        }
        if (args.length == 3) {
            String action = args[1].toLowerCase();
            String worldName = null;
            if (args.length >= 4 && !args[3].trim().isEmpty()) {
                worldName = args[3];
            } else if (sender instanceof Player player) {
                worldName = player.getWorld().getName();
            }

            AppConfig config = plugin.getAppConfig();
            if (config != null && worldName != null) {
                AppConfig.WorldRule ruleTemp = config.getWorldRules().get(worldName);
                if (ruleTemp == null) {
                    ruleTemp = config.getDefaultRule();
                }
                final AppConfig.WorldRule rule = ruleTemp;
                if (rule != null) {
                    if (action.equals("remove")) {
                        // Smart filter: suggest ONLY blocks currently registered
                        List<String> currentBlocks = rule.breakBlocks.stream().map((Material m) -> m.name().toLowerCase()).collect(Collectors.toList());
                        return filterSuggestions(currentBlocks, args[2]);
                    } else if (action.equals("add")) {
                        // Smart filter: exclude blocks already registered
                        List<String> availableBlocks = blockNames.stream()
                                .filter(b -> {
                                    Material mat = Material.matchMaterial(b);
                                    return mat == null || !rule.breakBlocks.contains(mat);
                                })
                                .collect(Collectors.toList());
                        return filterSuggestions(availableBlocks, args[2]);
                    }
                }
            }
            return filterSuggestions(blockNames, args[2]);
        }
        if (args.length == 4) {
            List<String> worlds = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
            return filterSuggestions(worlds, args[3]);
        }
        return Collections.emptyList();
    }

    private List<String> filterSuggestions(List<String> list, String query) {
        String q = query.toLowerCase();
        return list.stream().filter(s -> s.toLowerCase().startsWith(q)).collect(Collectors.toList());
    }
}
