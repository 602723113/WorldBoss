package cc.zoyn.worldboss.model;

import cc.zoyn.worldboss.I18n;
import cc.zoyn.worldboss.WorldBoss;
import cc.zoyn.worldboss.manager.BossManager;
import cc.zoyn.worldboss.util.ActionBarUtils;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 此类用于计算玩家们在副本中的一切操作
 */
public class BossCalculator {

    private Boss boss;

    // 以下为运行时所使用的

    private Map<String, FakeLocation> playerJoinLocation;
    private boolean canEnter;
    // 原子操作 计算剩余时间
    private AtomicLong atomicLong;
    // 数据统计表 玩家名 -> 数据表
    private Map<String, Statistics> statisticsMap;
    private boolean mobIsDead;
    private boolean isSetLeaveTime;

    public BossCalculator(Boss boss) {
        this.boss = boss;

        playerJoinLocation = Maps.newConcurrentMap();
        statisticsMap = Maps.newHashMap();

        atomicLong = new AtomicLong(boss.getTimeToKill());

        canEnter = false;
        mobIsDead = false;
        isSetLeaveTime = false;
    }

    public void start() {
        boss.getEnterCommand().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));

        WorldBoss.getInstance().sendServerMessage(boss.getBossIsSpawn()
                .replace("%display_name%", boss.getBossDisplayName())
                .replace("%name%", boss.getName()));

        atomicLong.set(boss.getTimeToKill());
        playerJoinLocation.clear();
        statisticsMap.clear();
        canEnter = true;
        mobIsDead = false;
        isSetLeaveTime = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                // 发送剩余时间
                for (String s : getJoinedPlayers()) {
                    Player player = Bukkit.getPlayerExact(s);
                    ActionBarUtils.sendBar(player, boss.getTimeTips().replace("%time%", "" + atomicLong.get()));
                }

                // boss死后倒计时缩短为30s
                if (mobIsDead) {
                    if (!isSetLeaveTime) {
                        atomicLong.set(30);
                        isSetLeaveTime = true;
                    }
                }

                atomicLong.set(atomicLong.get() - 1);

                // 够钟了...
                if (atomicLong.get() == 0) {
                    for (String s : getJoinedPlayers()) {
                        leavePlayer(s);
                    }
                    // 判断boss是否死亡
                    if (!mobIsDead) {
                        sendMessageToJoinedPlayers(I18n.BOSS_DOES_NOT_DEAD.getMessage().replace("%display_name%", boss.getBossDisplayName()));
                    }

                    canEnter = false;
                    mobIsDead = false;
                    boss.getLeaveCommand().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));
                    cancel();
                }
            }
        }.runTaskTimer(WorldBoss.getInstance(), 0L, 20L);
    }

    // 怪物死亡后执行的动作
    public void mobDeath() {
        canEnter = false;
        mobIsDead = true;

        LinkedHashMap<String, Statistics> result = sort(statisticsMap);

        double allDamage = getAllDamage();
        I18n.SHOW_STATISTICS_HEAD.getAsStringList().forEach(s -> getJoinedPlayers().forEach(playerName -> {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null) {
                return;
            }
            player.sendMessage(s
                    .replace("%displayName%", boss.getBossDisplayName())
                    .replace("%all_damage%", "" + Math.round(allDamage))
                    .replace("%my_damage%", "" + Math.round(statisticsMap.get(playerName).getDamage())));
        }));


        int i = 1;
        for (Map.Entry<String, Statistics> entry : result.entrySet()) {
            sendMessageToJoinedPlayers(I18n.SHOW_STATISTICS_FORMAT.getMessage()
                    .replace("%count%", "" + i)
                    .replace("%player_name%", entry.getKey())
                    .replace("%death%", "" + entry.getValue().getDeath())
                    .replace("%damage%", "" + Math.round(entry.getValue().getDamage()))
                    .replace("%percent%", Math.round(entry.getValue().getDamage() / allDamage * 100) + "%"));
//            sendMessageToJoinedPlayers("§e第" + i + "名: §c" + entry.getKey() + " §e死亡次数: §c" + entry.getValue().getDeath() + " §e总计伤害: §c" + entry.getValue().getDamage());
            i++;
            if (i == 11) {
                break;
            }
        }

        I18n.SHOW_STATISTICS_FOOT.getAsStringList().forEach(s -> getJoinedPlayers().forEach(playerName -> {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null) {
                return;
            }
            player.sendMessage(s
                    .replace("%displayName%", boss.getBossDisplayName())
                    .replace("%all_damage%", "" + Math.round(allDamage))
                    .replace("%my_damage%", "" + Math.round(statisticsMap.get(playerName).getDamage())));
        }));

        // 奖励发放
        sendReward();
        boss.getLeaveCommand().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));

    }

    private double getAllDamage() {
        AtomicReference<Double> damage = new AtomicReference<>((double) 0);
        statisticsMap.forEach((s, statistics) -> damage.updateAndGet(v -> ((double) (v + statistics.getDamage()))));

        return damage.get();
    }

    /**
     * 排序
     *
     * @param map 传入map
     * @return {@link LinkedHashMap}
     */
    private LinkedHashMap<String, Statistics> sort(Map<String, Statistics> map) {
        LinkedHashMap<String, Statistics> result = Maps.newLinkedHashMap();

        map.entrySet().stream()
                .sorted(Map.Entry.<String, Statistics>comparingByValue().reversed())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        return result;
    }

    public void joinPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        // 储存玩家进来时的位置
        playerJoinLocation.put(player.getName(), new FakeLocation(player.getLocation()));

        player.teleport(boss.getSpawnLocation().toLocation());
        BossManager.setPlayerBoss(player.getName(), boss);
        statisticsMap.put(player.getName(), new Statistics());
    }

    public void leavePlayer(String playerName) {
        if (playerName == null || playerName.isEmpty() || playerName.equalsIgnoreCase("")) {
            return;
        }
        Player player = Bukkit.getPlayerExact(playerName);
        // 判断是否是玩家掉线
        if (player != null && player.isOnline()) {
            player.teleport(playerJoinLocation.get(playerName).toLocation());
        }

        // 防止leave之后还在提示玩家
        playerJoinLocation.remove(playerName);
        // 清空玩家所挑战的世界boss
        BossManager.setPlayerBoss(playerName, null);
        // 清空玩家数据
        statisticsMap.remove(playerName);
    }

    public void sendReward() {
        for (Map.Entry<String, Statistics> entry : statisticsMap.entrySet()) {
            for (Map.Entry<Long, Kit> kitEntry : boss.getReward().entrySet()) {
                // 判断伤害
                if (entry.getValue().getDamage() >= kitEntry.getKey()) {
                    // 指令发放
                    kitEntry.getValue().getCommands().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player_name%", entry.getKey())));

                    Player player = Bukkit.getPlayerExact(entry.getKey());
                    if (player != null) {
                        player.sendMessage(kitEntry.getValue().getMessage()
                                .replace("%damage%", "" + Math.round(entry.getValue().getDamage()))
                                .replace("%player_name%", entry.getKey()));
                    }
                    break;
                }
            }
        }

        LinkedHashMap<String, Statistics> result = sort(statisticsMap);
        int i = 1;
        for (Map.Entry<String, Statistics> entry : result.entrySet()) {
            Player player = Bukkit.getPlayerExact(entry.getKey()); // 获取第 i 个幸运观众
            if (player != null) {
                Kit kit = boss.getRankReward().get(i);

                // 指令发放
                kit.getCommands().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player_name%", entry.getKey())));
                player.sendMessage(kit.getMessage()
                        .replace("%damage%", "" + Math.round(entry.getValue().getDamage()))
                        .replace("%player_name%", entry.getKey()));
            }
            i++;
            if (i == boss.getRankReward().size()) {
                break;
            }
        }
    }

    public Set<String> getJoinedPlayers() {
        return playerJoinLocation.keySet();
    }

    public void sendMessageToJoinedPlayers(String message) {
        getJoinedPlayers().forEach(s -> {
            Player player = Bukkit.getPlayerExact(s);
            if (player == null) {
                return;
            }
            player.sendMessage(message);
        });
    }

    public boolean isCanEnter() {
        return canEnter;
    }

    public Statistics getStatisticsByName(String playerName) {
        return statisticsMap.getOrDefault(playerName, null);
    }

    public Map<String, Statistics> getStatisticsMap() {
        return statisticsMap;
    }
}
