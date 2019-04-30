package ca.bcit.smpv2;

public class Promotions {
    private int promotionID;
    private int businessID;
    private PointTiers minTier;
    private String details;
    private int clicks;
    private String businessName;

    public Promotions(int promotionID, int businessID, PointTiers minTier, String details, int clicks, String businessName) {
        this.promotionID = promotionID;
        this.businessID = businessID;
        this.minTier = minTier;
        this.details = details;
        this.clicks = clicks;
        this.businessName = businessName;
    }

    public Promotions(String sqlResult) {
        String[] result = sqlResult.split("~s");
        this.promotionID = Integer.parseInt(result[0]);
        this.businessID = Integer.parseInt(result[1]);
        this.minTier = new PointTiers(result[2] + "~s" + result[3] + "~s" + result[4]);
        this.details = result[5];
        this.clicks = Integer.parseInt(result[6]);
        this.businessName = result[7];
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

    public PointTiers getMinTier() {
        return minTier;
    }

    public void setMinTier(PointTiers minTier) {
        this.minTier = minTier;
    }

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
