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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Analytics extends AppCompatActivity {

    PieChart pieChart;
    LineChart lineChart;
    BarChart barChart;

    int pieChartFontSize = 20;
    int openingAnimationDuration = 1000;

    float rainfall[] = {98.8f, 123.8f};
    String monthNames[] = {"Jan", "Feb"};
    //, "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    //, 161.6f, 24.2f, 52f, 58.2f, 35.4f, 13.8f, 78.4f, 203.4f, 240.2f, 159.7f

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));

        //setupPieChart();
        //setUpLineChart();
        //setUpBarChart();

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
            pieEntries.add(new PieEntry(Float.parseFloat((String) pieDataValues.get(i)), (String) pieData.get(i)));
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


        /*lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);*/

        ArrayList<Entry> lineEntries = new ArrayList<>();
        for(int i = 0; i < lineData.size(); ++i)
        {
            //lineEntries.add(new Entry (lineData.get(i), Float.parseFloat((String) lineDataValues.get(i))));
        }
        LineDataSet set1 = new LineDataSet(lineEntries, "Data Set 1");

        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3.0f);
        set1.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
    }

    void setUpBarChart()
    {
        barChart = (BarChart) findViewById(R.id.barchart);
        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));

        BarDataSet barDataSet  = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Dec");
        labels.add("Nov");
        labels.add("Oct");

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
