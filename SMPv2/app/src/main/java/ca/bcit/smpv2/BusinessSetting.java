package ca.bcit.smpv2;

public class BusinessSetting {

    private int businessID;
    private Setting setting;
    private String value;

    public BusinessSetting(int businessID, Setting setting, String value) {
        this.businessID = businessID;
        this.setting = setting;
        this.value = value;
    }

    public BusinessSetting(String[] result){
        
        this.businessID = Integer.parseInt(result[0]);
        this.setting = new Setting(new String[]{result[1], result[2], result[3]});
        this.value = result[4];
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
