package ca.bcit.smpv2;

public class UserBusinessTier {
    private int userID;
    private int businessID;
    private int tiersID;

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

    public int getTiersID() {
        return tiersID;
    }

    public void setTiersID(int tiersID) {
        this.tiersID = tiersID;
    }
}
