package com.ftl2nd.knuawhitelistblock.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public final class LangConfig extends BaseConfig {

    public static volatile String loadingLanguage = "en";

    public int messageVersion = 1;

    public String prefix;
    public String noPermission;
    public String reload;
    public String listHeader;
    public String listItem;
    public String listBlocksPlace;
    public String listBlocksBreak;
    public String ruleAdded;
    public String ruleRemoved;
    public String ruleAlreadyExists;
    public String ruleNotFound;
    public String notHoldingBlock;
    public String worldNotFound;
    public String invalidMaterial;

    public String cannotPlaceMessage;
    public String cannotPlaceActionbar;
    public String cannotPlaceSound;

    public String cannotBreakMessage;
    public String cannotBreakActionbar;
    public String cannotBreakSound;

    public LangConfig() {
        if ("vi".equalsIgnoreCase(loadingLanguage)) {
            prefix = "<gradient:#00C9FF:#92FE9D><bold>KnuaWhitelistBlock</bold></gradient> <dark_gray>»</dark_gray> ";
            noPermission = "<#ff7675>Bạn không có quyền thực hiện hành động này.</#ff7675>";
            reload = "<#55efc4>Đã tải lại cấu hình và hệ thống ngôn ngữ thành công.</#55efc4>";
            listHeader = "<gradient:#00C9FF:#92FE9D><bold>Danh sách Block của World: {world}</bold></gradient>";
            listItem = "<gray>Chế độ Đặt: <yellow>{place_mode}</yellow> | Chế độ Đập: <yellow>{break_mode}</yellow></gray>";
            listBlocksPlace = "<gray>Danh sách đặt: <white>{blocks}</white></gray>";
            listBlocksBreak = "<gray>Danh sách đập: <white>{blocks}</white></gray>";
            ruleAdded = "<gray>Đã thêm block <#ffbe76>{block}</#ffbe76> vào danh sách <yellow>{action}</yellow> của world <yellow>{world}</yellow>.</gray>";
            ruleRemoved = "<gray>Đã xóa block <#ffbe76>{block}</#ffbe76> khỏi danh sách <yellow>{action}</yellow> của world <yellow>{world}</yellow>.</gray>";
            ruleAlreadyExists = "<#ff7675>Block <#ffbe76>{block}</#ffbe76> đã tồn tại trong danh sách <yellow>{action}</yellow> của world <yellow>{world}</yellow>.</#ff7675>";
            ruleNotFound = "<#ff7675>Block <#ffbe76>{block}</#ffbe76> không có trong danh sách <yellow>{action}</yellow> của world <yellow>{world}</yellow>.</#ff7675>";
            notHoldingBlock = "<#ff7675>Bạn không cầm block nào trên tay chính.</#ff7675>";
            worldNotFound = "<#ff7675>Thế giới <yellow>{world}</yellow> không tồn tại.</#ff7675>";
            invalidMaterial = "<#ff7675>Vật phẩm <yellow>{block}</yellow> không phải block hợp lệ.</#ff7675>";

            cannotPlaceMessage = "<gray>Bạn không được phép đặt block <#ffbe76>{block}</#ffbe76> tại thế giới này.</gray>";
            cannotPlaceActionbar = "<#ff7675>Không được phép đặt block tại thế giới này.</#ff7675>";
            cannotPlaceSound = "ENTITY_VILLAGER_NO";

            cannotBreakMessage = "<gray>Bạn không được phép đập block <#ffbe76>{block}</#ffbe76> tại thế giới này.</gray>";
            cannotBreakActionbar = "<#ff7675>Không được phép đập block tại thế giới này.</#ff7675>";
            cannotBreakSound = "ENTITY_VILLAGER_NO";
        } else {
            prefix = "<gradient:#00C9FF:#92FE9D><bold>KnuaWhitelistBlock</bold></gradient> <dark_gray>»</dark_gray> ";
            noPermission = "<#ff7675>You do not have permission to perform this action.</#ff7675>";
            reload = "<#55efc4>Configuration and languages reloaded successfully.</#55efc4>";
            listHeader = "<gradient:#00C9FF:#92FE9D><bold>Block List for World: {world}</bold></gradient>";
            listItem = "<gray>Place Mode: <yellow>{place_mode}</yellow> | Break Mode: <yellow>{break_mode}</yellow></gray>";
            listBlocksPlace = "<gray>Allowed/Blocked Place: <white>{blocks}</white></gray>";
            listBlocksBreak = "<gray>Allowed/Blocked Break: <white>{blocks}</white></gray>";
            ruleAdded = "<gray>Added <#ffbe76>{block}</#ffbe76> to <yellow>{action}</yellow> rules in world <yellow>{world}</yellow>.</gray>";
            ruleRemoved = "<gray>Removed <#ffbe76>{block}</#ffbe76> from <yellow>{action}</yellow> rules in world <yellow>{world}</yellow>.</gray>";
            ruleAlreadyExists = "<#ff7675>Block <#ffbe76>{block}</#ffbe76> is already in the <yellow>{action}</yellow> rules of world <yellow>{world}</yellow>.</#ff7675>";
            ruleNotFound = "<#ff7675>Block <#ffbe76>{block}</#ffbe76> is not in the <yellow>{action}</yellow> rules of world <yellow>{world}</yellow>.</#ff7675>";
            notHoldingBlock = "<#ff7675>You are not holding any block in your main hand.</#ff7675>";
            worldNotFound = "<#ff7675>World <yellow>{world}</yellow> does not exist.</#ff7675>";
            invalidMaterial = "<#ff7675>Material <yellow>{block}</yellow> is not a valid block.</#ff7675>";

            cannotPlaceMessage = "<gray>Block <#ffbe76>{block}</#ffbe76> cannot be placed in this world.</gray>";
            cannotPlaceActionbar = "<#ff7675>Block placement is restricted in this world.</#ff7675>";
            cannotPlaceSound = "ENTITY_VILLAGER_NO";

            cannotBreakMessage = "<gray>Block <#ffbe76>{block}</#ffbe76> cannot be broken in this world.</gray>";
            cannotBreakActionbar = "<#ff7675>Block breaking is restricted in this world.</#ff7675>";
            cannotBreakSound = "ENTITY_VILLAGER_NO";
        }
    }

    @Override
    public void onLoad(FileConfiguration config) {
        this.messageVersion = config.getInt("message-version", 1);
        this.prefix = config.getString("messages.prefix", this.prefix);
        this.noPermission = config.getString("messages.no-permission", this.noPermission);
        this.reload = config.getString("messages.reload", this.reload);
        this.listHeader = config.getString("messages.list-header", this.listHeader);
        this.listItem = config.getString("messages.list-item", this.listItem);
        this.listBlocksPlace = config.getString("messages.list-blocks-place", this.listBlocksPlace);
        this.listBlocksBreak = config.getString("messages.list-blocks-break", this.listBlocksBreak);
        this.ruleAdded = config.getString("messages.rule-added", this.ruleAdded);
        this.ruleRemoved = config.getString("messages.rule-removed", this.ruleRemoved);
        this.ruleAlreadyExists = config.getString("messages.rule-already-exists", this.ruleAlreadyExists);
        this.ruleNotFound = config.getString("messages.rule-not-found", this.ruleNotFound);
        this.notHoldingBlock = config.getString("messages.not-holding-block", this.notHoldingBlock);
        this.worldNotFound = config.getString("messages.world-not-found", this.worldNotFound);
        this.invalidMaterial = config.getString("messages.invalid-material", this.invalidMaterial);

        this.cannotPlaceMessage = config.getString("messages.cannot-place-message", this.cannotPlaceMessage);
        this.cannotPlaceActionbar = config.getString("messages.cannot-place-actionbar", this.cannotPlaceActionbar);
        this.cannotPlaceSound = config.getString("messages.cannot-place-sound", this.cannotPlaceSound);

        this.cannotBreakMessage = config.getString("messages.cannot-break-message", this.cannotBreakMessage);
        this.cannotBreakActionbar = config.getString("messages.cannot-break-actionbar", this.cannotBreakActionbar);
        this.cannotBreakSound = config.getString("messages.cannot-break-sound", this.cannotBreakSound);
    }

    @Override
    public void onSave(FileConfiguration config) {
        config.set("message-version", this.messageVersion);
        config.setComments("message-version", List.of("Translation file version. Do not modify."));

        config.set("messages.prefix", this.prefix);
        config.set("messages.no-permission", this.noPermission);
        config.set("messages.reload", this.reload);
        config.set("messages.list-header", this.listHeader);
        config.set("messages.list-item", this.listItem);
        config.set("messages.list-blocks-place", this.listBlocksPlace);
        config.set("messages.list-blocks-break", this.listBlocksBreak);
        config.set("messages.rule-added", this.ruleAdded);
        config.set("messages.rule-removed", this.ruleRemoved);
        config.set("messages.rule-already-exists", this.ruleAlreadyExists);
        config.set("messages.rule-not-found", this.ruleNotFound);
        config.set("messages.not-holding-block", this.notHoldingBlock);
        config.set("messages.world-not-found", this.worldNotFound);
        config.set("messages.invalid-material", this.invalidMaterial);

        config.set("messages.cannot-place-message", this.cannotPlaceMessage);
        config.setComments("messages.cannot-place-message", List.of("Custom effects when placement or breaking is restricted. Leave empty \"\" to disable."));
        config.set("messages.cannot-place-actionbar", this.cannotPlaceActionbar);
        config.set("messages.cannot-place-sound", this.cannotPlaceSound);

        config.set("messages.cannot-break-message", this.cannotBreakMessage);
        config.set("messages.cannot-break-actionbar", this.cannotBreakActionbar);
        config.set("messages.cannot-break-sound", this.cannotBreakSound);
    }
}
