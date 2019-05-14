package ca.bcit.smpv2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class Analytics extends AppCompatActivity {

    PieChart pieChart;
    LineChart lineChart;
    BarChart barChart;

    ArrayList<View> charts = new ArrayList<>();

    int chartDefaultFontSize = 18;
    float lineChartLineSize = 3.0f;
    int chartAxisDefaultFontSize = 12;
    Typeface chartDefaultFont;
    Typeface chartDefaultTitleFont;

    //Gesture left and right variables
    float x1,x2;
    static final int MIN_DISTANCE = 150;
    static int currentView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chartDefaultFont = Typeface.createFromAsset(getAssets(),"champagnebold.ttf");
        chartDefaultTitleFont = Typeface.createFromAsset(getAssets(), "theboldfont.ttf");
        //This order of function calls determines order of appearance by swiping as well
        generateLineData();
        generatePieData();
        generateBarData();
        setContentView(R.layout.activity_analytics);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    charts.get(currentView).setVisibility(View.GONE);
                    if (x2 > x1)
                    {
                        currentView++;
                        if(currentView > charts.size() - 1)
                            currentView = 0;
                    }
                    // Right to left swipe action
                    else
                    {
                        currentView--;
                        if(currentView < 0)
                            currentView = charts.size() - 1;
                    }
                    charts.get(currentView).setVisibility(1);
                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private void generatePieData()
    {
        ArrayList<DataPoint> pieDataPoints = new ArrayList<>();
        ArrayList<Object> pieData = new ArrayList<>();
        ArrayList<Object> pieDataValues = new ArrayList<>();
        new DatabaseObj(Analytics.this).calcNewOldUsers(BusinessDashboard.business.getBusinessID(), (ArrayList<Object> objects) ->
        {
            for(Object o : objects)
                pieDataPoints.add((DataPoint) o);
            for(int i = 0; i < pieDataPoints.size(); ++i)
            {
                for(int k = 0; k < pieDataPoints.get(i).getData().size(); ++k) {
                    if (pieDataPoints.get(i).getData().get(k).equals("new") || pieDataPoints.get(i).getData().get(k).equals("old"))
                    {
                        pieData.add(pieDataPoints.get(i).getData().get(k));
                    } else {
                        pieDataValues.add(pieDataPoints.get(i).getData().get(k));
                    }
                }
            }
            setupPieChart(pieData, pieDataValues, "New and Old Visitors");
        });
    }

    private void generateLineData()
    {
        ArrayList<DataPoint> lineDataPoints = new ArrayList<>();
        ArrayList<Object> lineData = new ArrayList<>();
        ArrayList<Object> lineDataValues = new ArrayList<>();
        new DatabaseObj(Analytics.this).calcMonthlyVisits(BusinessDashboard.business.getBusinessID(), (ArrayList<Object> objects) ->
        {
            for(Object o : objects)
                lineDataPoints.add((DataPoint) o);
            for(int i = 0; i < lineDataPoints.size(); ++i)
            {
                for(int k = 0; k < lineDataPoints.get(i).getData().size(); ++k)
                {
                    if(k == 0)
                    {
                        lineData.add(lineDataPoints.get(i).getData().get(k));
                    }
                    else
                    {
                        lineDataValues.add(lineDataPoints.get(i).getData().get(k));
                    }
                }
            }
            setUpLineChart(lineData, lineDataValues, "Last 12 Months of Visits");
            charts.get(currentView).setVisibility(1);
        });
    }

    private void generateBarData()
    {
        ArrayList<DataPoint> barDataPoints = new ArrayList<>();
        ArrayList<Object> barData = new ArrayList<>();
        ArrayList<Object> barDataValues = new ArrayList<>();
        new DatabaseObj(Analytics.this).calcVisitorsPerTier(BusinessDashboard.business.getBusinessID(), (ArrayList<Object> objects) ->
        {
            for(int l = 0; l < BusinessDashboard.tiers.size(); ++l)
            {
                ArrayList<String> al = new ArrayList<>();
                al.add(Integer.toString(l));
                al.add("0");
                barDataPoints.add(new DataPoint(al));
            }
            for(Object o : objects)
            {
                DataPoint currentDP = (DataPoint) o;
                barDataPoints.get(Integer.parseInt(currentDP.getData().get(0))).setData(currentDP.getData());
            }
            for(int i = 0; i < barDataPoints.size(); ++i)
            {
                barData.add(barDataPoints.get(i).getData().get(0));
                barDataValues.add(barDataPoints.get(i).getData().get(1));
            }
            setUpBarChart(barData, barDataValues, "Visitors Per Tier");
        });
    }

    private void setupPieChart(ArrayList<Object> pieData, ArrayList<Object> pieDataValues, String pieChartName)
    {
        //Assumes equally
        List<PieEntry> pieEntries;
        PieDataSet dataSet;
        PieData data;

        pieEntries = new ArrayList<>();
        for(int i = 0; i < pieData.size(); ++i)
        {
            String pieDataLabel = (String) pieData.get(i);
            pieEntries.add(new PieEntry(Float.parseFloat((String)pieDataValues.get(i)), (pieDataLabel.substring(0,1).toUpperCase() + pieDataLabel.substring(1, pieDataLabel.length()))));
        }

        dataSet = new PieDataSet(pieEntries, pieChartName);
        data = new PieData(dataSet);
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTypeface(chartDefaultTitleFont);
        dataSet.setValueTextSize(chartDefaultFontSize);
        dataSet.setValueTextColor(Color.WHITE);
        data.setValueTypeface(chartDefaultFont);
        data.setValueTextSize(chartDefaultFontSize);
        data.setValueTextColor(Color.WHITE);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.setTouchEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        //setUpLegend(pieChart);
        charts.add(pieChart);
    }

    private void setUpLineChart(ArrayList<Object> lineData, ArrayList<Object> lineDataValues, String lineChartName)
    {
        lineChart = (LineChart) findViewById(R.id.lineChart);
        XAxis lineChartXAxis;
        ArrayList<Entry> lineEntries;
        LineDataSet lineDataSet;
        ArrayList<ILineDataSet> dataSets;
        LineData data;
        TreeMap<Integer, Float> defaultMonths;
        Calendar cal;
        SimpleDateFormat monthDate;

        lineChartXAxis = lineChart.getXAxis();
        lineChartXAxis.setValueFormatter(new MonthValueFormatter());
        lineChartXAxis.setLabelCount(12, true);

        lineEntries = new ArrayList<>();
        defaultMonths = new TreeMap<>();
        monthDate = new SimpleDateFormat("MM-yyyy");
        cal = Calendar.getInstance();
        cal.setTime(new Date());

        for (int i = 1; i <= 12; i++) {
            String month = monthDate.format(cal.getTime()).replace('-','.');
            int month_converted = Integer.parseInt(month.substring(5,7)) * 12 + Integer.parseInt(month.substring(0,2));
            defaultMonths.put(month_converted, 0.0f);
            cal.add(Calendar.MONTH, -1);
        }
        for(int k = 0; k < lineData.size(); ++k)
        {
            int dataMonth = Integer.parseInt(lineData.get(k).toString().substring(2, 4)) * 12  + Integer.parseInt(lineData.get(k).toString().substring(5, 7));
            defaultMonths.put(dataMonth, Float.parseFloat((String)lineDataValues.get(k)));
        }
        for(TreeMap.Entry<Integer, Float> entry : defaultMonths.entrySet()) {
            lineEntries.add(new Entry(entry.getKey(), entry.getValue()));
        }

        lineDataSet = new LineDataSet(lineEntries, lineChartName);

        lineDataSet.setColor(R.color.darkBlue);
        lineDataSet.setLineWidth(lineChartLineSize);
        lineDataSet.setValueTextSize(chartDefaultFontSize);
        lineDataSet.setValueTypeface(chartDefaultFont);


        dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);


        data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.setTouchEnabled(false);
        setUpLegend(lineChart);
        setUpFonts(lineChart);
        charts.add(lineChart);
    }

    void setUpBarChart(ArrayList<Object> barData, ArrayList<Object> barDataValues, String barChartName)
    {
        //Collections.sort((ArrayList<DataPoint>) (ArrayList<?>) barData,(DataPoint d1, DataPoint d2) -> Integer.compare(Integer.parseInt(d1.getData().get(0)), Integer.parseInt(d2.getData().get(0))));
        barChart = (BarChart) findViewById(R.id.barchart);
        XAxis barChartXAxis = barChart.getXAxis();
        barChartXAxis.setValueFormatter(new TierValueFormatter());

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < barData.size(); ++i)
        {
            barEntries.add(new BarEntry (Float.parseFloat((String) barData.get(i)), Float.parseFloat((String)barDataValues.get(i))));
        }

        BarDataSet barDataSet  = new BarDataSet(barEntries, barChartName);
        barDataSet.setValueTextSize(chartDefaultFontSize);
        barDataSet.setValueTypeface(chartDefaultTitleFont);
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setValueTextSize(chartDefaultFontSize);
        data.setValueTypeface(chartDefaultFont);

        barChart.setData(data);

        barChart.setTouchEnabled(false);
        setUpLegend(barChart);
        setUpFonts(barChart);
        charts.add(barChart);
    }

    public void setUpLegend(Chart chart)
    {
        Legend chartLegend = chart.getLegend();
        chartLegend.setXOffset(20f);
        chart.getDescription().setEnabled(false);
    }

    public void setUpFonts(Chart chart)
    {
        chart.getXAxis().setTypeface(chartDefaultFont);
        chart.getXAxis().setTextSize(chartAxisDefaultFontSize);
        chart.getLegend().setTextSize(chartDefaultFontSize);
        chart.getLegend().setTypeface(chartDefaultTitleFont);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_business, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() != R.id.analytics)
            finish();
        switch (item.getItemId()) {
            case R.id.dashboard:
                Intent i = new Intent(getBaseContext(), BusinessDashboard.class);
                startActivity(i);
                return true;
            case R.id.analytics:
                return true;
            case R.id.settings:
                Intent j = new Intent(getBaseContext(), BusinessSettingsActivity.class);
                startActivity(j);
                return true;
            case R.id.logOut:
                Intent l = new Intent(getBaseContext(), LandingActivity.class);
                l.putExtra("logout", true);
                startActivity(l);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
