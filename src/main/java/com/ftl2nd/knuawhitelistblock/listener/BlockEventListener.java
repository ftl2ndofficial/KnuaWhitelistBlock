package com.ftl2nd.knuawhitelistblock.listener;

import com.ftl2nd.knuawhitelistblock.KnuaWhitelistBlock;
import com.ftl2nd.knuawhitelistblock.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockEventListener implements Listener {

    private final KnuaWhitelistBlock plugin;

    public BlockEventListener(KnuaWhitelistBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        Block block = event.getBlockPlaced();
        if (block == null || block.getWorld() == null) return;

        Material material = block.getType();
        String worldName = block.getWorld().getName();

        // 1. Admin and general bypass checks (fast returns)
        if (player.hasPermission("knuawhitelistblock.admin") || player.hasPermission("knuawhitelistblock.bypass")) {
            return;
        }

        // 2. Rule check: Check if restricted first.
        // If placing is allowed by rules, return early without checking permission.
        if (plugin.getRuleManager().canPlace(worldName, material)) {
            return;
        }

        // 3. Specific block bypass check (only checked if restricted!)
        String materialName = material.name();
        if (player.hasPermission("knuawhitelistblock.bypass.place." + materialName.toLowerCase())) {
            return;
        }

        // Otherwise, restrict placement
        event.setCancelled(true);
        MessageUtil.sendRestrictionEffects(player, materialName, true, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        Block block = event.getBlock();
        if (block == null || block.getWorld() == null) return;

        Material material = block.getType();
        String worldName = block.getWorld().getName();

        // 1. Admin and general bypass checks (fast returns)
        if (player.hasPermission("knuawhitelistblock.admin") || player.hasPermission("knuawhitelistblock.bypass")) {
            return;
        }

        // 2. Rule check: Check if restricted first.
        // If breaking is allowed by rules, return early without checking permission.
        if (plugin.getRuleManager().canBreak(worldName, material)) {
            return;
        }

        // 3. Specific block bypass check (only checked if restricted!)
        String materialName = material.name();
        if (player.hasPermission("knuawhitelistblock.bypass.break." + materialName.toLowerCase())) {
            return;
        }

        // Otherwise, restrict break
        event.setCancelled(true);
        MessageUtil.sendRestrictionEffects(player, materialName, false, plugin);
    }
}
