package ca.bcit.smpv2;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class PromotionsAdapter extends ArrayAdapter<Promotions> {

    public PromotionsAdapter (Context context, ArrayList<Promotions> promotions)
    {
        super(context, 0, promotions);
    }


}
