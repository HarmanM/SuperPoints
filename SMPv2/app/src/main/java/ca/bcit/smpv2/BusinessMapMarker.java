package ca.bcit.smpv2;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusinessMapMarker
{
    private Business business;
    private Marker marker;
    private MarkerOptions options;
    private boolean preferred;

    public BusinessMapMarker(Business business, Marker marker, MarkerOptions options, boolean preferred) {
        this.business = business;
        this.marker = marker;
        this.options = options;
        this.preferred = preferred;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public MarkerOptions getOptions() {
        return options;
    }

    public void setOptions(MarkerOptions options) {
        this.options = options;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

}
