package cc.zoyn.worldboss.model;

public class Statistics implements Comparable<Statistics> {

    private int death;
    private double damage;

    public int getDeath() {
        return death;
    }

    public double getDamage() {
        return damage;
    }

    public void addDeath(int death) {
        this.death += death;
    }

    public void addDamage(double damage) {
        this.damage += damage;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "death=" + death +
                ", damage=" + damage +
                '}';
    }

    @Override
    public int compareTo(Statistics o) {
        return Double.compare(this.damage, o.getDamage());
    }
}
