package cc.zoyn.worldboss.manager;

import cc.zoyn.worldboss.WorldBoss;
import cc.zoyn.worldboss.model.Boss;
import cc.zoyn.worldboss.model.BossCalculator;
import cc.zoyn.worldboss.model.FakeLocation;
import cc.zoyn.worldboss.model.Kit;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BossManager {

    // 内部名 -> boss
    private static Map<String, Boss> bossMap = Maps.newHashMap();
    // 玩家名 -> boss
    private static Map<String, Boss> playerMap = Maps.newHashMap();

    // 防止意外构建
    private BossManager() {
    }

    /**
     * 利用内部名获取对应boss对象
     *
     * @param name 内部名
     * @return {@link Boss}
     */
    public static Boss getBossByName(String name) {
        return bossMap.getOrDefault(name, null);
    }

    public static Boss getBossByDisplayName(String displayName) {
        for (Boss boss : getBosses()) {
            if (displayName.equalsIgnoreCase(boss.getBossDisplayName())) {
                return boss;
            }
        }
        return null;
    }

    public static Boss getBossByPlayerName(String playerName) {
        return playerMap.getOrDefault(playerName, null);
    }

    public static void setPlayerBoss(String playerName, Boss boss) {
        if (playerName == null) {
            return;
        }
        if (boss == null) {
            playerMap.remove(playerName);
            return;
        }
        playerMap.put(playerName, boss);
    }

    /**
     * 获取所有Boss
     *
     * @return {@link Collection<Boss>}
     */
    public static Collection<Boss> getBosses() {
        return bossMap.values();
    }

    public static void loadBosses() {
        bossMap.clear();

        FileConfiguration config = WorldBoss.getInstance().getConfig();
        ConfigurationSection boss = config.getConfigurationSection("boss");
        boss.getKeys(false).forEach(s -> {
            String name = boss.getString(s + ".name");
            String bossDisplayName = boss.getString(s + ".bossDisplayName").replace("&", "§");
            String executeTime = boss.getString(s + ".executeTime");
            String permission = boss.getString(s + ".permission");
            int maxPlayer = boss.getInt(s + ".maxPlayer");
            String bossIsSpawn = boss.getString(s + ".bossIsSpawn").replace("&", "§");
            long timeToKill = boss.getLong(s + ".timeToKill");
            String timeTips = boss.getString(s + ".timeTips").replace("&", "§");
            List<String> enterCommand = boss.getStringList(s + ".enterCommand");
            List<String> leaveCommand = boss.getStringList(s + ".leaveCommand");

            //奖励读取
            Map<Long, Kit> reward = new LinkedHashMap<>();
            boss.getConfigurationSection(s + ".reward").getKeys(false).forEach(damageAmount -> {
                List<String> commands = boss.getStringList(s + ".reward." + damageAmount + ".commands");
                String message = boss.getString(s + ".reward." + damageAmount + ".message").replace("&", "§");

                reward.put(Long.valueOf(damageAmount), new Kit(commands, message));
            });

            Map<Integer, Kit> rankReward = new LinkedHashMap<>();
            boss.getConfigurationSection(s + ".rankReward").getKeys(false).forEach(rank -> {
                List<String> commands = boss.getStringList(s + ".rankReward." + rank + ".commands");
                String message = boss.getString(s + ".rankReward." + rank + ".message").replace("&", "§");

                rankReward.put(Integer.valueOf(rank), new Kit(commands, message));
            });

            // 进入位置
            FakeLocation fakeLocation = new FakeLocation(boss.getString(s + ".spawnLocation.name"),
                    boss.getDouble(s + ".spawnLocation.x"),
                    boss.getDouble(s + ".spawnLocation.y"),
                    boss.getDouble(s + ".spawnLocation.z"),
                    boss.getDouble(s + ".spawnLocation.yaw"),
                    boss.getDouble(s + ".spawnLocation.pitch"));


            Boss bossObject = new Boss(name);
            bossObject.bossDisplayName(bossDisplayName)
                    .permission(permission)
                    .maxPlayer(maxPlayer)
                    .rankReward(rankReward)
                    .reward(reward)
                    .bossIsSpawn(bossIsSpawn)
                    .executeTime(executeTime)
                    .timeToKill(timeToKill)
                    .timeTips(timeTips)
                    .enterCommand(enterCommand)
                    .leaveCommand(leaveCommand)
                    .spawnLocation(fakeLocation)
                    .calculator(new BossCalculator(bossObject));

            bossMap.put(name, bossObject);
        });

        WorldBoss.getInstance().sendConsoleMessage("§f成功读取共 §a§l" + bossMap.size() + " §f个世界Boss副本");
    }

    public static Map<String, Boss> getBossMap() {
        return bossMap;
    }
}
