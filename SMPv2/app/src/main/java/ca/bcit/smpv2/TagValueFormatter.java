package ca.bcit.smpv2;

public class TagValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        String yearMonth = Float.toString(value);
        yearMonth = String.format("%-7s", yearMonth ).replace(' ', '0');
        String yearMonthFormat = (int)(value / 12) + "-"  + (int)(value % 12);
        return yearMonthFormat;
    }
}
