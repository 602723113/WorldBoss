package cc.zoyn.worldboss;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public enum I18n {

    UNKNOWN_COMMAND("unknownCommand"),
    NO_PERMISSION("noPermission"),
    RELOAD_SUCCESS("reloadSuccess"),
    MUST_BE_A_PLAYER("mustBeAPlayer"),
    WRONG_PARAMETER("wrongParameter"),
    BOSS_DOES_NOT_EXIST("theBossDoesnotExist"),
    NOT_IN_WORLD_BOSS("youAreNotInWorldBoss"),
    CANNOT_JOIN_WORLD_BOSS("cannotJoinWorldBoss"),
    JOIN_WORLD_BOSS("joinWorldBoss"),
    LEAVE_WORLD_BOSS("leaveWorldBoss"),
    SHOW_STATISTICS_HEAD("showStatisticsHead"),
    SHOW_STATISTICS_FORMAT("showStatisticsFormat"),
    SHOW_STATISTICS_FOOT("showStatisticsFoot"),
    BOSS_DOES_NOT_DEAD("bossDoesnotDead"),
    CANNOT_USE_OTHER_COMMAND("cannotUseOtherCommand"),
    BOSS_IS_FULL_OF_PLAYER("bossIsFullOfPlayer"),
    HELP("help");

    private Object message;

    I18n(String key) {
        key = "message." + key;
        FileConfiguration config = WorldBoss.getInstance().getConfig();
        if (config.isString(key)) {
            this.message = translateColorCode(config.getString(key));
        } else if (config.isList(key)) {
            this.message = translateColorCode(config.getStringList(key));
        }

    }

    public String getMessage() {
        return (String) this.message;
    }

    public List<String> getAsStringList() {
        return (List<String>) this.message;
    }

    private static String translateColorCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static List<String> translateColorCode(List<String> messages) {
        return messages.stream().map(I18n::translateColorCode).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
