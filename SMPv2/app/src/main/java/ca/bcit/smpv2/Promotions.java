package ca.bcit.smpv2;

public class Promotions {
    private int promotionID;
    private int businessID;
    private int minimumPoints;
    private int tierID;
    private String businessName;
    private String details;
    private int clicks;

    public Promotions(int promotionID, int businessID, int minimumPoints, String details, int clicks, String businessName) {
        this.promotionID = promotionID;
        this.businessID = businessID;
        this.minimumPoints = minimumPoints;
        this.details = details;
        this.clicks = clicks;
        this.businessName = businessName;
    }

    public Promotions(String sqlResult) {
        //TODO delimiter needs to change
        String[] result = sqlResult.split("~s");
        this.promotionID = Integer.parseInt(result[0]);
        this.businessID = Integer.parseInt(result[1]);
        this.minimumPoints = Integer.parseInt(result[2]);
        this.details = result[3];
        this.clicks = Integer.parseInt(result[4]);
        this.businessName = result[5];
    }

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public int getMinimumPoints() {
        return minimumPoints;
    }

    public void setMinimumPoints(int minimumPoints) {
        this.minimumPoints = minimumPoints;
    }

    public int getTierID() {return tierID;}

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
