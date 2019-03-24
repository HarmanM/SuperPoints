package ca.bcit.smpv2;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public Visit(int visitID, int userID, int businessID, int duration, Calendar date){
        this.visitID = visitID;
        this.userID = userID;
        this.businessID = businessID;
        this.duration = duration;
        this.date = date;
    }

    public Visit(String sqlResult){
        String[] result = sqlResult.split(" ");
        this.visitID = Integer.parseInt(result[0]);
        this.userID = Integer.parseInt(result[1]);
        this.businessID = Integer.parseInt(result[2]);
        this.duration = Integer.parseInt(result[3]);
        try {
            this.date = Calendar.getInstance();
            this.date.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(result[4]));
        }catch(Exception e){

        }
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
