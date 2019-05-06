package ca.bcit.smpv2;

import java.util.ArrayList;

public class Beacon {
    private int beaconID;
    private int businessID;
    private int major;
    private int minor;
    private int txPower;

    public Beacon(int beaconID, int businessID, int major, int minor, int txPower){
        this.beaconID = beaconID;
        this.businessID = businessID;
        this.major = major;
        this.minor = minor;
        this.txPower = txPower;
    }

    public Beacon(String[] result){

        this.beaconID = Integer.parseInt(result[0]);
        this.businessID = Integer.parseInt(result[1]);
        this.major = Integer.parseInt(result[2]);
        this.minor = Integer.parseInt(result[3]);
        this.txPower = Integer.parseInt(result[4]);
    }

    public int getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(int beaconID) {
        this.beaconID = beaconID;
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }
}
