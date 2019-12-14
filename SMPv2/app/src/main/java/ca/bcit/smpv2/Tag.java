package ca.bcit.smpv2;

public class Tag {

    private int tagID;
    private int businessID;
    private String tag;

    public Tag(int businessID, String tag) {
        this.tagID = -1;
        this.businessID = businessID;
        this.tag = tag;
    }

    public Tag(int tagID, int businessID, String tag) {
        this.tagID = tagID;
        this.businessID = businessID;
        this.tag = tag;
    }

    public Tag(String[] result){

        this.tagID = Integer.parseInt(result[0]);
        this.businessID = Integer.parseInt(result[1]);
        this.tag = result[2];
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
