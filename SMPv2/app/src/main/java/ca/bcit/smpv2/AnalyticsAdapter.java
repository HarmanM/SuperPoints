package ca.bcit.smpv2;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class AnalyticsAdapter extends ArrayAdapter<Analytics> {

    public AnalyticsAdapter(Context context, ArrayList<Analytics> analytics) {

        super(context, 0, analytics);

    }


}
