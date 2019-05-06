package ca.bcit.smpv2;

public class Setting {

    private int settingID;
    private String settingName;
    private String valueType;

    public Setting(int settingID, String settingName, String valueType){
        this.settingID = settingID;
        this.settingName = settingName;
        this.valueType = valueType;
    }

    public Setting(String[] result){
        
        this.settingID = Integer.parseInt(result[0]);
        this.settingName = result[1];
        this.valueType = result[2];
    }

    public int getSettingID() {
        return settingID;
    }

    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
