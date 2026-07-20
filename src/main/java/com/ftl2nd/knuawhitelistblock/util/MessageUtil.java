package com.ftl2nd.knuawhitelistblock.util;

import com.ftl2nd.knuawhitelistblock.KnuaWhitelistBlock;
import com.ftl2nd.knuawhitelistblock.config.LangConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class MessageUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private MessageUtil() {}

    /**
     * Sends a MiniMessage formatted message to a player.
     */
    public static void sendMessage(Player player, String message) {
        if (message == null || message.isEmpty()) return;
        player.sendMessage(MINI_MESSAGE.deserialize(message));
    }

    /**
     * Sends a MiniMessage formatted message to a player with a prefix.
     */
    public static void sendMessage(Player player, String message, String prefix) {
        if (message == null || message.isEmpty()) return;
        String pre = prefix != null ? prefix : "";
        player.sendMessage(MINI_MESSAGE.deserialize(pre + message));
    }

    /**
     * Sends an action bar message using MiniMessage to a player.
     */
    public static void sendActionBar(Player player, String message) {
        if (message == null || message.isEmpty()) return;
        player.sendActionBar(MINI_MESSAGE.deserialize(message));
    }

    /**
     * Safely plays a sound to a player. Fails silently if the sound name is invalid.
     */
    public static void playSound(Player player, String soundName, float volume, float pitch) {
        if (soundName == null || soundName.isEmpty()) return;
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException ignored) {
            // Silently ignore invalid sound configuration
        }
    }

    /**
     * Sends the configured restriction warnings (sound, message, actionbar) to a player.
     */
    public static void sendRestrictionEffects(Player player, String materialName, boolean isPlace, KnuaWhitelistBlock plugin) {
        LangConfig lang = plugin.getLangConfig();
        if (lang == null) return;

        String msg = isPlace ? lang.cannotPlaceMessage : lang.cannotBreakMessage;
        String actionbar = isPlace ? lang.cannotPlaceActionbar : lang.cannotBreakActionbar;
        String soundName = isPlace ? lang.cannotPlaceSound : lang.cannotBreakSound;

        // 1. Text Message
        if (msg != null && !msg.isEmpty()) {
            sendMessage(player, msg.replace("{block}", materialName), lang.prefix);
        }

        // 2. Actionbar
        if (actionbar != null && !actionbar.isEmpty()) {
            sendActionBar(player, actionbar.replace("{block}", materialName));
        }

        // 3. Sound
        if (soundName != null && !soundName.isEmpty()) {
            playSound(player, soundName, 1.0f, 1.0f);
        }
    }
}
