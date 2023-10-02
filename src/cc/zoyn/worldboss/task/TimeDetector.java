package cc.zoyn.worldboss.task;

import cc.zoyn.worldboss.manager.BossManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDetector extends BukkitRunnable {

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void run() {
        BossManager.getBosses().forEach(boss -> {
            String temp = format.format(new Date());
            // 判断时间生成
            if (boss.getExecuteTime().equalsIgnoreCase(temp)) {
                boss.getCalculator().start();
            }
        });
    }
}
