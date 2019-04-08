package ca.bcit.smpv2;

import android.content.Context;
import android.util.Pair;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ca.bcit.smpv2.Analytics;
import ca.bcit.smpv2.Points;

public class PointsAdapter extends ArrayAdapter<Pair<Business, Points>> {

    public PointsAdapter(Context context, ArrayList<Pair<Business, Points>> pointsArrayList) {

        super(context, 0, pointsArrayList);

    }


}