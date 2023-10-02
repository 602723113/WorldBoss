package cc.zoyn.worldboss.command.subcommand;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.WorldBoss;
import cc.zoyn.worldboss.command.SubCommand;
import cc.zoyn.worldboss.manager.BossManager;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.isOp()) {
            WorldBoss.getInstance().reloadConfig();
            BossManager.loadBosses();

            sender.sendMessage(I18n.RELOAD_SUCCESS.getMessage());
        } else {
            sender.sendMessage(I18n.NO_PERMISSION.getMessage());
        }
    }
}
