package cc.zoyn.worldboss;

import cc.zoyn.worldboss.command.CommandHandler;
import cc.zoyn.worldboss.listener.EntityDamageListener;
import cc.zoyn.worldboss.listener.EntityDeathListener;
import cc.zoyn.worldboss.listener.PlayerCommandPreprocessListener;
import cc.zoyn.worldboss.listener.PlayerQuitListener;
import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.task.TimeDetector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldBoss extends JavaPlugin {

    private static WorldBoss instance;

    @Override
    public void onEnable() {
        instance = this;

        sendConsoleMessage("正在读取Config信息");
        // 保存config
        saveDefaultConfig();
        // 读取boss
        BossManager.loadBosses();

        Bukkit.getPluginCommand("wb").setExecutor(new CommandHandler());

        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(), this);

        // 时间检测
        TimeDetector timeDetector = new TimeDetector();
        timeDetector.runTaskTimer(this, 0, 20L);
    }

    public static WorldBoss getInstance() {
        return instance;
    }

    public void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage("§f[§a世界Boss§f] " + message.replace("&", "§"));
    }

    public void sendServerMessage(String message) {
        Bukkit.broadcastMessage(message.replace("&", "§"));
    }

}

