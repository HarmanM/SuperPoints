package ca.bcit.smpv2;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class TierValueFormatter extends ValueFormatter {

    public String[] tierIDs = {};

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String yearMonth = Float.toString(value);
        String yearMonthFormat = yearMonth.substring(0, 4) + "-" + yearMonth.substring(5, 7);
        return yearMonthFormat;
    }
}
