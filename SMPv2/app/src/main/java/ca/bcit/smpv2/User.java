package ca.bcit.smpv2;

import java.util.TreeMap;

public class User {
    private int userID;
    private int businessID;
    private String password;
    private String username;
    private TreeMap<Integer, UserSetting> settings = new TreeMap<>();

    public User(int userID, int businessID, String password, String username) {
        this.userID = userID;
        this.businessID = businessID;
        this.password = password;
        this.username = username;
    }

    // Parses a string from the php query to the MySQL database.
    // First value is the userID, second value is the username, third value is the integer representing the setting
    // No other values are sent
    public User(String[] result) {
        
        this.userID = Integer.parseInt(result[0]);
        if(result[1].equalsIgnoreCase("NULL"))
            this.businessID = -1;
        else
            this.businessID = Integer.parseInt(result[1]);
        this.username = result[2];
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addSetting(UserSetting setting){
        settings.put(setting.getSetting().getSettingID(), setting);
    }

    public UserSetting getSetting(int i){
        return settings.get(i);
    }
}
