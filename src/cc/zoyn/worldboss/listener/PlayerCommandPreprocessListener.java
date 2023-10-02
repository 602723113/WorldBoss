package cc.zoyn.worldboss.listener;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.WorldBoss;
import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.model.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Boss boss = BossManager.getBossByPlayerName(player.getName());
        // 检测是否正在打世界Boss
        if (boss == null) {
            return;
        }

        for (String command : WorldBoss.getInstance().getConfig().getStringList("option.permittedCommands")) {
            if (event.getMessage().startsWith(command)) {
                event.setCancelled(false);
                return;
            }
        }
        player.sendMessage(I18n.CANNOT_USE_OTHER_COMMAND.getMessage());
        event.setCancelled(true);
    }

}
