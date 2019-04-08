package ca.bcit.smpv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PromotionsAdapter extends ArrayAdapter<Promotions> {

    public PromotionsAdapter (Context context, ArrayList<Promotions> promotions)
    {
        super(context, 0, promotions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data for this position
        Promotions promotion = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_promotions, parent, false);
        }
        // Lookup view for data population
        TextView promotionBusinessName = (TextView) convertView.findViewById(R.id.promotionBusinessName);
        TextView promotionDetails = (TextView) convertView.findViewById(R.id.promotionDetails);
        TextView promotionMinimumPoints = (TextView) convertView.findViewById(R.id.promotionMinimumPoints);
        // Populate the data into the template view using the data object
        promotionBusinessName.setText(promotion.getBusinessName());
        promotionDetails.setText(promotion.getDetails());
        promotionMinimumPoints.setText(String.valueOf(promotion.getMinimumPoints()));
        // Return the completed view to render on screen
        return convertView;
    }


}
