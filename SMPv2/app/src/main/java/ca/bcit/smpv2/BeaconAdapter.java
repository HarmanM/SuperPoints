package ca.bcit.smpv2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BeaconAdapter extends ArrayAdapter<Beacon> {


    public BeaconAdapter (Context context, ArrayList<Beacon> beacons)
    {
        super(context, 0, beacons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data for this position
        Beacon beacon = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_beacons, parent, false);
        }
        // Lookup view for data population
        TextView beaconName = (TextView) convertView.findViewById(R.id.beaconName);
        TextView beaconMinor = (TextView) convertView.findViewById(R.id.beaconMinor);
        TextView beaconMajor = (TextView) convertView.findViewById(R.id.beaconMajor);
        // Populate the data into the template view using the data object
        beaconName.setText(beacon.getBeaconID());
        beaconMinor.setText(Integer.toString(beacon.getMinor()));
        beaconMajor.setText(Integer.toString(beacon.getMajor()));
        // Return the completed view to render on screen
        return convertView;
    }



}
