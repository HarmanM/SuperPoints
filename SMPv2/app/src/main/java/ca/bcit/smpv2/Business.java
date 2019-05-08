package ca.bcit.smpv2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class Business implements Serializable {
    private int businessID;
    private String businessName;
    private double latitude;
    private double longitude;
    private String region;
    private TreeMap<Integer, BusinessSetting> settings = new TreeMap<>();

    public Business(int businessID, String businessName, double latitude, double longitude, String region){
        this.businessID = businessID;
        this.businessName = businessName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }

    public Business(String[] result){
        
        this.businessID = Integer.parseInt(result[0]);
        this.businessName = result[1];
        this.latitude = Double.parseDouble(result[2]);
        this.longitude = Double.parseDouble(result[3]);
        this.region = result[4];
    }

    public String getRegion(){return region;}

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void addSetting(BusinessSetting setting){
        settings.put(setting.getSetting().getSettingID(), setting);
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BusinessSetting getSetting(int i){
        return settings.get(i);
    }

    public String getBusinessAddress(Context context)
    {
        Geocoder geocoder;
        List<Address> addresses;
        String businessAddressName = "";

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(this.getLatitude()
                    , this.getLongitude(), 1);
            if (addresses.size() > 0 && addresses != null) {
                String SubThoroughFare = addresses.get(0).getSubThoroughfare();
                String ThoroughFare = addresses.get(0).getThoroughfare();
                businessAddressName += ((SubThoroughFare == null) ? "" : SubThoroughFare + ", ");
                businessAddressName = ((ThoroughFare == null) ? businessAddressName : businessAddressName + ThoroughFare);
            }
        } catch (IOException e) {
            Log.e("EXCEPTION: GEOCODER MAPSACTIVITY GENERATEBUSINESS MARKERS", e.toString());
        }
        return businessAddressName;
    }

}
