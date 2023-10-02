package cc.zoyn.worldboss.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * 用于表示一个Location, 不会保存world对象
 */
public class FakeLocation {

    private String worldName;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public FakeLocation(Location location) {
        if (location == null) {
            throw new NullPointerException("传入的Location为空");
        }
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public FakeLocation(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FakeLocation(String worldName, double x, double y, double z, double yaw, double pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);
    }

    public String getWorldName() {
        return worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    @Override
    public String toString() {
        return "FakeLocation{" +
                "worldName='" + worldName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }
}
