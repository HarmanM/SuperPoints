package ca.bcit.smpv2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class BeaconsAdapter extends ArrayAdapter<Beacon> {

    public BeaconsAdapter (Context context, ArrayList<Beacon> beacons)
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
        TextView beaconID = (TextView) convertView.findViewById(R.id.promotionBusinessName);
        TextView businessID = (TextView) convertView.findViewById(R.id.shortPromotionDetails);
        TextView major = (TextView) convertView.findViewById(R.id.promotionMinimumPoints);
        TextView minor = convertView.findViewById(R.id.iconImageView);
        TextView txPower = convertView.findViewById(R.id.promoImageView);
        // Populate the data into the template view using the data object
        promotionBusinessName.setText(promotion.getBusinessName());
        shortPromotionDetails.setText(promotion.getShortDescription());
        promotionMinimumPoints.setText(String.valueOf(promotion.getMinTier().getName()));
        try {
            Picasso.get().load("https://s3.amazonaws.com/superpoints-userfiles-mobilehub-467637819/promo/" + promotion.getPromotionID() + ".jpg")
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(promotionIcon);
            Picasso.get().load("https://s3.amazonaws.com/superpoints-userfiles-mobilehub-467637819/promo/" + promotion.getPromotionID() + ".jpg")
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(promotionUploadImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the completed view to render on screen

        return convertView;
    }



}
