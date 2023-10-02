package cc.zoyn.worldboss.command.subcommand;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.command.SubCommand;
import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.model.Boss;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(I18n.MUST_BE_A_PLAYER.getMessage());
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(I18n.WRONG_PARAMETER.getMessage());
            return;
        }
        String name = args[1];
        Boss boss = BossManager.getBossByName(name);
        if (boss == null) {
            sender.sendMessage(I18n.BOSS_DOES_NOT_EXIST.getMessage());
            return;
        }

        Player player = (Player) sender;
        if (!boss.getCalculator().isCanEnter()) {
            sender.sendMessage(I18n.CANNOT_JOIN_WORLD_BOSS.getMessage());
            return;
        }
        // 检查最大人数
        if (boss.getCalculator().getJoinedPlayers().size() >= boss.getMaxPlayer() && !player.isOp() && !player.hasPermission(boss.getPermission())) {
            sender.sendMessage(I18n.BOSS_IS_FULL_OF_PLAYER.getMessage());
            return;
        }
        boss.getCalculator().joinPlayer(player);
        player.sendMessage(I18n.JOIN_WORLD_BOSS.getMessage().replace("%name%", boss.getName()));
    }
}
