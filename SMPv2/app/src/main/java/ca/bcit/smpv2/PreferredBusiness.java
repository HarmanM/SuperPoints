package ca.bcit.smpv2;

public class PreferredBusiness {

    private int userID;
    private int businessID;

    public PreferredBusiness(int userID, int businessID){
        this.userID = userID;
        this.businessID = businessID;
    }

    public PreferredBusiness(String sqlResult){
        String[] result = sqlResult.split("~s");
        this.userID = Integer.parseInt(result[0]);
        this.businessID = Integer.parseInt(result[1]);
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

}
