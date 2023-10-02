package cc.zoyn.worldboss.listener;

import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.model.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Boss boss = BossManager.getBossByPlayerName(player.getName());
        // 检测是否正在打世界Boss
        if (boss == null) {
            return;
        }
        // 清空玩家数据
        boss.getCalculator().leavePlayer(player.getName());
    }

}
