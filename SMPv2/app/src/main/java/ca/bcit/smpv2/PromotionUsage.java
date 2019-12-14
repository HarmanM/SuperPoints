package ca.bcit.smpv2;

public class PromotionUsage {

    private int promotionUsageID;
    private int promotionID;
    private int userID;

    public PromotionUsage(int promotionUsageID, int promotionID, int userID) {
        this.promotionUsageID = promotionUsageID;
        this.promotionID = promotionID;
        this.userID = userID;
    }

    public PromotionUsage(String[] result) {

        this.promotionUsageID = Integer.parseInt(result[0]);
        this.promotionID = Integer.parseInt(result[1]);
        this.userID = Integer.parseInt(result[2]);
    }

    public int getPromotionUsageID() {
        return promotionUsageID;
    }

    public void setPromotionUsageID(int promotionUsageID) {
        this.promotionUsageID = promotionUsageID;
    }

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
