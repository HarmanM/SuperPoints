package ca.bcit.smpv2;

public class PointTiers {

    private int tierID;
    private int minPoints;
    private String name;

    public PointTiers(int tierID, int minPoints, String name) {
        this.tierID = tierID;
        this.minPoints = minPoints;
        this.name = name;
    }

    public PointTiers(String[] result){
        
        this.tierID = Integer.parseInt(result[0]);
        this.minPoints = Integer.parseInt(result[1]);
        this.name = result[2];
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
