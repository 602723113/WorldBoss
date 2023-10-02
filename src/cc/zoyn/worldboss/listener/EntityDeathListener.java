package cc.zoyn.worldboss.listener;

import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.model.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Boss boss = BossManager.getBossByPlayerName(player.getName());
            // 检测是否正在打世界Boss
            if (boss == null) {
                return;
            }
            // 死亡计数
            boss.getCalculator().getStatisticsByName(player.getName()).addDeath(1);

            // 死后重生至副本内
            player.setHealth(player.getMaxHealth());
            player.teleport(boss.getSpawnLocation().toLocation());
        }

        // 判断boss
        if (event.getEntity() == null || event.getEntity().getCustomName() == null) {
            return;
        }
        Boss boss = BossManager.getBossByDisplayName(event.getEntity().getCustomName());
        if (boss == null) {
            return;
        }
        boss.getCalculator().mobDeath();
    }

}
