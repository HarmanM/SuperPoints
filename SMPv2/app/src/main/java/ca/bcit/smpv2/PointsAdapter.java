package ca.bcit.smpv2;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_userpoints, parent, false);

        TextView points = (TextView) convertView.findViewById(R.id.name_pointsPairTextView);
        points.setText(userPoints.first.getBusinessName() + ": " + userPoints.second.getTier() + " Tier");

        int curTier = 0;
        for(int i = 0; i < UserPointsActivity.tiers.size(); ++i)
            if(UserPointsActivity.tiers.get(i).getName().equals(userPoints.second.getTier()))
                curTier = i;
        SeekBar seekBar = convertView.findViewById(R.id.tierProgress);

        if(curTier != UserPointsActivity.tiers.size() - 1) {
            seekBar.setProgress((int) ((float) (userPoints.second.getPoints() - UserPointsActivity.tiers.get(curTier).getMinPoints())
                    / (UserPointsActivity.tiers.get(curTier + 1).getMinPoints() - UserPointsActivity.tiers.get(curTier).getMinPoints()) * 100));

            ((TextView) convertView.findViewById(R.id.curTier)).setText(UserPointsActivity.tiers.get(curTier).getName() + " (" + UserPointsActivity.tiers.get(curTier).getMinPoints() + ")");
            ((TextView) convertView.findViewById(R.id.nextTier)).setText(UserPointsActivity.tiers.get(curTier + 1).getName() + " (" + UserPointsActivity.tiers.get(curTier + 1).getMinPoints() + ")");
        }else{
            seekBar.setProgress(100);

            ((TextView) convertView.findViewById(R.id.curTier)).setText(UserPointsActivity.tiers.get(curTier).getName() + " (" + UserPointsActivity.tiers.get(curTier).getMinPoints() + ")");
            ((TextView) convertView.findViewById(R.id.nextTier)).setText("");
        }
        return convertView;
    }


}