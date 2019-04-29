package ca.bcit.smpv2;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.bcit.smpv2.Analytics;
import ca.bcit.smpv2.Points;

public class PointsAdapter extends ArrayAdapter<Pair<Business, Points>> {

    public PointsAdapter(Context context, ArrayList<Pair<Business, Points>> pointsArrayList) {

        super(context, 0, pointsArrayList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data for this position
        Pair<Business, Points> userPoints = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_promotions, parent, false);

        // Lookup view for data population
        TextView promotionBusinessName = (TextView) convertView.findViewById(R.id.promotionBusinessName);
        // Populate the data into the template view using the data object
        promotionBusinessName.setText(userPoints.first.getBusinessName() + ": " + userPoints.second.getTier() + " Tier");
        // Return the completed view to render on screen
        return convertView;
    }


}