package ca.bcit.smpv2;

public class Points {
    private int userID;
    private int businessID;
    private int points;
    private String tier;

    public Points(int userID, int businessID, int points){
        this.userID = userID;
        this.businessID = businessID;
        this.points = points;
    }

    public Points(String[] result){
        
        this.userID = Integer.parseInt(result[0]);
        this.businessID = Integer.parseInt(result[1]);
        this.points = Integer.parseInt(result[2]);
        this.tier = result[3];
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTier() {
        return tier;
    }
}
