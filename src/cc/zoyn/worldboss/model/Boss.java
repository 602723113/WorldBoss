package cc.zoyn.worldboss.model;

import java.util.List;
import java.util.Map;

/**
 * 用于表示一个Boss
 */
public class Boss {

    private String name;
    private String bossDisplayName;
    private int maxPlayer;
    private String permission;
    private String executeTime;
    private String bossIsSpawn;
    private long timeToKill;
    private String timeTips;
    private Map<Long, Kit> reward;
    private Map<Integer, Kit> rankReward;
    private List<String> enterCommand;
    private List<String> leaveCommand;
    private FakeLocation spawnLocation;
    // 副本执行器
    private BossCalculator calculator;

    public Boss(String name) {
        this.name = name;
    }

    public Boss name(String name) {
        this.name = name;
        return this;
    }

    public Boss permission(String permission) {
        this.permission = permission;
        return this;
    }

    public Boss maxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
        return this;
    }

    public Boss rankReward(Map<Integer, Kit> rankReward) {
        this.rankReward = rankReward;
        return this;
    }

    public Boss reward(Map<Long, Kit> reward) {
        this.reward = reward;
        return this;
    }

    public Boss calculator(BossCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    public Boss bossIsSpawn(String bossIsSpawn) {
        this.bossIsSpawn = bossIsSpawn;
        return this;
    }

    public Boss bossDisplayName(String bossDisplayName) {
        this.bossDisplayName = bossDisplayName;
        return this;
    }

    public Boss executeTime(String executeTime) {
        this.executeTime = executeTime;
        return this;
    }

    public Boss timeToKill(long timeToKill) {
        this.timeToKill = timeToKill;
        return this;
    }

    public Boss timeTips(String timeTips) {
        this.timeTips = timeTips;
        return this;
    }

    public Boss enterCommand(List<String> enterCommand) {
        this.enterCommand = enterCommand;
        return this;
    }

    public Boss leaveCommand(List<String> leaveCommand) {
        this.leaveCommand = leaveCommand;
        return this;
    }

    public Boss spawnLocation(FakeLocation spawnLocation) {
        this.spawnLocation = spawnLocation;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getBossDisplayName() {
        return bossDisplayName;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public long getTimeToKill() {
        return timeToKill;
    }

    public String getTimeTips() {
        return timeTips;
    }

    public List<String> getEnterCommand() {
        return enterCommand;
    }

    public List<String> getLeaveCommand() {
        return leaveCommand;
    }

    public FakeLocation getSpawnLocation() {
        return spawnLocation;
    }

    public BossCalculator getCalculator() {
        return calculator;
    }

    public String getBossIsSpawn() {
        return bossIsSpawn;
    }

    public Map<Long, Kit> getReward() {
        return reward;
    }

    public Map<Integer, Kit> getRankReward() {
        return rankReward;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public String getPermission() {
        return permission;
    }
}