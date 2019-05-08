package ca.bcit.smpv2;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Beacon implements Serializable {
    private String beaconID;
    private int businessID;
    private int major;
    private int minor;
    private int txPower;

    public Beacon(String beaconID, int businessID, int major, int minor, int txPower){
        this.beaconID = beaconID;
        this.businessID = businessID;
        this.major = major;
        this.minor = minor;
        this.txPower = txPower;
    }

    public Beacon(String[] result){

        this.beaconID = result[0];
        this.businessID = Integer.parseInt(result[1]);
        this.major = Integer.parseInt(result[2]);
        this.minor = Integer.parseInt(result[3]);
        this.txPower = Integer.parseInt(result[4]);
    }

    public String getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(String beaconID) {
        this.beaconID = beaconID;
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }
}
