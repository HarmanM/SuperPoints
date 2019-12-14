package ca.bcit.smpv2;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class TierValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value)
    {
        String tier = "";
        for(int i = 0; i < BusinessDashboard.tiers.size(); ++i)
        {
            if(value == BusinessDashboard.tiers.get(i).getTierID())
                tier = BusinessDashboard.tiers.get(i).getName();
        }
        return tier;
    }
}
