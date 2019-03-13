package ca.bcit.smpv2;

import java.util.Calendar;
import java.util.Date;

public class Visit {
    private int visitID;
    private int userID;
    private int businessID;
    private int duration;
    private Calendar date;

    public Visit(int userID, int businessID, Calendar date){
        this.userID = userID;
        this.businessID = businessID;
        this.date = date;
    }

    public int getVisitID() {
        return visitID;
    }

    public void setVisitID(int visitID) {
        this.visitID = visitID;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
