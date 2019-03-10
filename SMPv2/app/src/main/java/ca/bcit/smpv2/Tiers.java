package ca.bcit.smpv2;

public class Tiers {
    private int tierID;
    private int minVists;
    private int minDuration;

    public int getTierID() {
        return tierID;
    }

    public void setTierID(int tierID) {
        this.tierID = tierID;
    }

    public int getMinVists() {
        return minVists;
    }

    public void setMinVists(int minVists) {
        this.minVists = minVists;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }
}
