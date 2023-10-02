package cc.zoyn.worldboss.command.subcommand;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.command.SubCommand;
import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.model.Boss;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(I18n.MUST_BE_A_PLAYER.getMessage());
            return;
        }
        Player player = (Player) sender;
        Boss boss = BossManager.getBossByPlayerName(player.getName());
        if (boss == null) {
            player.sendMessage(I18n.NOT_IN_WORLD_BOSS.getMessage());
            return;
        }

        boss.getCalculator().leavePlayer(player.getName());
        player.sendMessage(I18n.LEAVE_WORLD_BOSS.getMessage().replace("%name%", boss.getName()));
    }
}
