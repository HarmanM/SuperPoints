package ca.bcit.smpv2;

public class Tiers {
    private int tierID;
    private int minPoints;

    public Tiers(int tierID, int minPoints){
        this.tierID = tierID;
        this.minPoints = minPoints;
    }

    public Tiers(String sqlResult){
        String[] result = sqlResult.split("~s");
        this.tierID = Integer.parseInt(result[0]);
        this.minPoints = Integer.parseInt(result[1]);
    }

    public int getTierID() {
        return tierID;
    }

    public void setTierID(int tierID) {
        this.tierID = tierID;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }
}
