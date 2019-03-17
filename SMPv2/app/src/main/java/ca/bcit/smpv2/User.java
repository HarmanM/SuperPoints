package ca.bcit.smpv2;

public class User {
    private int userID;
    private int businessID;
    private String password;
    private String username;
    private boolean setting;

    public User(int userID, int businessID, String password, String username, boolean setting) {
        this.userID = userID;
        this.businessID = businessID;
        this.password = password;
        this.username = username;
        this.setting = setting;
    }

    // Parses a string from the php query to the MySQL database.
    // First value is the userID, second value is the username, third value is the integer representing the setting
    // No other values are sent
    public User(String sqlResult) {
        String[] result = sqlResult.split(" ");
        this.userID = Integer.parseInt(result[0]);
        this.username = result[1];
        this.setting = (result[2].equals("0") ? false : true);
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

    public boolean isSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }
}