package cc.zoyn.worldboss.command;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.command.subcommand.*;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Command Manage
 *
 * @author Zoyn
 * @since 2018-05-18
 */
public class CommandHandler implements CommandExecutor {

    private static Map<String, SubCommand> commandMap = Maps.newHashMap();

    /**
     * Initialize all sub commands
     */
    public CommandHandler() {
        registerCommand("help", new HelpCommand());
        registerCommand("reload", new ReloadCommand());
        registerCommand("join", new JoinCommand());
        registerCommand("run", new RunCommand());
        registerCommand("leave", new LeaveCommand());
    }

    private void registerCommand(String commandName, SubCommand subCommand) {
        if (commandMap.containsKey(commandName)) {
            Bukkit.getLogger().warning("duplicate add command!");
        }
        commandMap.put(commandName, subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            commandMap.get("help").execute(sender, args);
            return true;
        }
        if (!commandMap.containsKey(args[0])) {
            sender.sendMessage(I18n.UNKNOWN_COMMAND.getMessage());
            return true;
        }

        // the first args is args[0], that is the sub command
        SubCommand subCommand = commandMap.get(args[0]);
        subCommand.execute(sender, args);
        return true;
    }
}
