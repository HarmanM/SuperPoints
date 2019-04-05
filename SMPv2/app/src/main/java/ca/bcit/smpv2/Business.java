package ca.bcit.smpv2;

public class Business {
    private int businessID;
    private String businessName;
    private double latitude;
    private double longitude;
    private String region;

    public Business(int businessID, String businessName, double latitude, double longitude, String region){
        this.businessID = businessID;
        this.businessName = businessName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }

    public Business(String sqlResult){
        String[] result = sqlResult.split(" ");
        this.businessID = Integer.parseInt(result[0]);
        this.businessName = result[1];
        this.latitude = Double.parseDouble(result[2]);
        this.longitude = Double.parseDouble(result[3]);
        this.region = result[4];
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
