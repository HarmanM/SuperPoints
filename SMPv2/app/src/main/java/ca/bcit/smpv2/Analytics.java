package ca.bcit.smpv2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Analytics extends AppCompatActivity {

    PieChart pieChart;
    LineChart lineChart;
    BarChart barChart;

    int pieChartFontSize = 20;
    int lineChartFontSize = 20;
    float lineChartLineSize = 3.0f;
    int openingAnimationDuration = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));

        //generatePieData();
        //generateLineData();
        generateBarData();

        //setUpLineChart();
        //setUpBarChart();

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
        });
    }

    private void generateBarData()
    {
        ArrayList<DataPoint> barDataPoints = new ArrayList<>();
        ArrayList<Object> barData = new ArrayList<>();
        ArrayList<Object> barDataValues = new ArrayList<>();
        new DatabaseObj(Analytics.this).calcVisitorsPerTier(BusinessDashboard.business.getBusinessID(), (ArrayList<Object> objects) ->
        {
            for(Object o : objects)
                barDataPoints.add((DataPoint) o);
            for(int i = 0; i < barDataPoints.size(); ++i)
            {
                for(int k = 0; k < barDataPoints.get(i).getData().size(); ++k)
                {
                    if(k == 0)
                    {
                        barData.add(barDataPoints.get(i).getData().get(k));
                    }
                    else
                    {
                        barDataValues.add(barDataPoints.get(i).getData().get(k));
                    }

                }
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
            pieEntries.add(new PieEntry(Float.parseFloat((String)pieDataValues.get(i)), (String) pieData.get(i)));
        }
        dataSet = new PieDataSet(pieEntries, pieChartName);
        data = new PieData(dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(pieChartFontSize);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setData(data);
        pieChart.animateY(openingAnimationDuration);
        pieChart.invalidate();
    }

    private void setUpLineChart(ArrayList<Object> lineData, ArrayList<Object> lineDataValues, String lineChartName)
    {
        lineChart = (LineChart) findViewById(R.id.lineChart);
        XAxis lineChartXAxis;
        ArrayList<Entry> lineEntries;
        LineDataSet lineDataSet;
        ArrayList<ILineDataSet> dataSets;
        LineData data;

        lineChartXAxis = lineChart.getXAxis();
        lineChartXAxis.setValueFormatter(new MonthValueFormatter());

        lineEntries = new ArrayList<>();
        for(int i = 0; i < lineData.size(); ++i)
        {
            lineEntries.add(new Entry (
                    Integer.parseInt(lineData.get(i).toString().substring(0,4)) * 12
                            + Integer.parseInt(lineData.get(i).toString().substring(5,7)), Float.parseFloat((String)lineDataValues.get(i))));
        }

        lineDataSet = new LineDataSet(lineEntries, lineChartName);

        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(lineChartLineSize);
        lineDataSet.setValueTextSize(lineChartFontSize);

        dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        data = new LineData(dataSets);


        lineChart.setData(data);
    }

    void setUpBarChart(ArrayList<Object> barData, ArrayList<Object> barDataValues, String barChartName)
    {

        Collections.sort((ArrayList<DataPoint>) (ArrayList<?>) barData,
                (DataPoint d1, DataPoint d2) -> Integer.compare(Integer.parseInt(d1.getData().get(0)), Integer.parseInt(d2.getData().get(0))));
        barChart = (BarChart) findViewById(R.id.barchart);
        XAxis barChartXAxis = barChart.getXAxis();
        barChartXAxis.setValueFormatter(new TierValueFormatter());

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < barData.size(); ++i)
        {
            barEntries.add(new BarEntry (Float.parseFloat((String) barData.get(i)), Float.parseFloat((String)barDataValues.get(i))));
        }

        BarDataSet barDataSet  = new BarDataSet(barEntries, barChartName);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(1000);
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

            case R.id.settings:
                Intent j = new Intent(getBaseContext(), BusinessSettingsActivity.class);
                startActivity(j);
                return true;
            case R.id.logOut:
                LoginActivity.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
