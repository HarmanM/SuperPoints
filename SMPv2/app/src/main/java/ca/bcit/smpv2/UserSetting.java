package ca.bcit.smpv2;

public class UserSetting {

    private int userID;
    private Setting setting;
    private String value;

    public UserSetting(int userID, Setting businessSetting, String value) {
        this.userID = userID;
        this.setting = businessSetting;
        this.value = value;
    }

    public UserSetting(String sqlResult){
        String[] result = sqlResult.split("~s");
        this.userID = Integer.parseInt(result[0]);
        this.setting = new Setting(result[1] + "~s" + result[2] + "~s" + result[3]);
        this.value = result[4];
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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
