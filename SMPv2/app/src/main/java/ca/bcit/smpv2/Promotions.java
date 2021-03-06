package ca.bcit.smpv2;

import java.util.ArrayList;

public class Promotions {
    private int promotionID;
    private int businessID;
    private PointTiers minTier;
    private String details;
    private int clicks;
    private String shortDescription;
    private String businessName;
    private ArrayList<Tag> promotionTags = new ArrayList<>();

    public Promotions(int promotionID, int businessID, PointTiers minTier, String details, int clicks, String businessName, String shortDescription) {
        this.promotionID = promotionID;
        this.businessID = businessID;
        this.minTier = minTier;
        this.details = details;
        this.clicks = clicks;
        this.businessName = businessName;
        this.shortDescription = shortDescription;
    }

    public Promotions(String[] result) {
        this.promotionID = Integer.parseInt(result[0]);
        this.businessID = Integer.parseInt(result[1]);
        this.minTier = new PointTiers(new String[]{result[2], result[3], result[4]});
        this.details = result[5];
        this.clicks = Integer.parseInt(result[6]);
        this.businessName = result[7];
        this.shortDescription = result[8];
        DatabaseObj.parseData((ArrayList<Object>)(ArrayList<?>)promotionTags, result[9], Tag::new);
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public ArrayList<Tag> getPromotionTags(){ return promotionTags; }

    public void addPromotionTag(Tag t){ promotionTags.add(t); }

    public void addPromotionTags(ArrayList<Tag> newTags)
    {
        promotionTags = newTags;
    }

}
