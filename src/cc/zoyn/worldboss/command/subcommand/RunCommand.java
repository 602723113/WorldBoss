package cc.zoyn.worldboss.command.subcommand;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.command.SubCommand;
import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.model.Boss;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class RunCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(I18n.NO_PERMISSION.getMessage());
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

//        Location location = boss.getSpawnLocation().toLocation();
//        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
//        zombie.setCustomName("§6§lBoss");
        boss.getCalculator().start();

        sender.sendMessage("§a开启副本 " + name + " §a成功...");
    }
}
