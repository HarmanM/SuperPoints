package ca.bcit.smpv2;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MonthValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        String yearMonth = Float.toString(value);
        yearMonth = String.format("%-7s", yearMonth ).replace(' ', '0');
        String yearMonthFormat = (int)(value / 12) + "-"  + (int)(value % 12);
        return yearMonthFormat;
    }
}
