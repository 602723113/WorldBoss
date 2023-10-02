package cc.zoyn.worldboss.command;

import org.bukkit.command.CommandSender;

/**
 * Represent a sub command
 *
 * @author Zoyn
 * @since 2019-8-18
 */
@FunctionalInterface
public interface SubCommand {

    void execute(CommandSender sender, String[] args);

}
