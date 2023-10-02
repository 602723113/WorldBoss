package cc.zoyn.worldboss.listener;

import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.model.Boss;
import cc.zoyn.worldboss.model.Statistics;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Boss boss = BossManager.getBossByPlayerName(player.getName());
            // 检测是否正在打世界Boss
            if (boss == null) {
                return;
            }
            Statistics statistics = boss.getCalculator().getStatisticsByName(player.getName());
            if (statistics == null) {
                statistics = new Statistics();
            }
            // 伤害计数
            statistics.addDamage(event.getDamage());
        }

        // 使用弓箭 什么别的东西进行攻击
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                // 玩家判断
                Player player = (Player) projectile.getShooter();
                Boss boss = BossManager.getBossByPlayerName(player.getName());
                // 检测是否正在打世界Boss
                if (boss == null) {
                    return;
                }
                Statistics statistics = boss.getCalculator().getStatisticsByName(player.getName());
                if (statistics == null) {
                    statistics = new Statistics();
                }
                // 伤害计数
                statistics.addDamage(event.getDamage());
            }

        }
    }

}
