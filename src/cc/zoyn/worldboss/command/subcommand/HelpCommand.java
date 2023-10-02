package cc.zoyn.worldboss.command.subcommand;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Zoyn
 * @since 2018-8-18
 */
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.isOp()) {
            I18n.HELP.getAsStringList().forEach(sender::sendMessage);
        } else {
            sender.sendMessage(I18n.NO_PERMISSION.getMessage());
        }
    }
}
