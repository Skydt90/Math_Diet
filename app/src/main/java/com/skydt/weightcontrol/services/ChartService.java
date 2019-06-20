package com.skydt.weightcontrol.services;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.skydt.weightcontrol.models.BodyWeighIn;
import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.models.Diet;

import java.util.ArrayList;
import java.util.List;

public class ChartService
{
    public LineDataSet getDietWeightRange(Diet diet)
    {
        LineDataSet lineDataSet; // contains values to 1 graph
        List<Entry> entries = new ArrayList<>(); // the x, y values of the graph

        int x = 1;
        for (Day day : diet.getDays())
        {
            entries.add(new Entry(x, (float)day.getGoalWeight()));
            x ++;
        }

        lineDataSet = new LineDataSet(entries, "Målvægt");

        styleLineDataSet(lineDataSet, false, Color.parseColor("#ff3300"), 1);
        return lineDataSet;
    }

    public LineDataSet getLastWeighInOfDays(List<BodyWeighIn> bodyWeighIns)
    {
        LineDataSet lineDataSet;
        List<Entry> entries = new ArrayList<>();

        if (!bodyWeighIns.isEmpty())
        {
            int x = 1;
            String currentDate = bodyWeighIns.get(0).getSQLDate();

            for (BodyWeighIn weight: bodyWeighIns)
            {
                if (weight.getSQLDate().equals(currentDate))
                {
                    entries.add(new Entry(x, (float)weight.getBodyWeight()));
                }
                else
                {
                    x++;
                    currentDate = weight.getSQLDate();
                    entries.add(new Entry(x, (float)weight.getBodyWeight()));
                }
            }
        }
        lineDataSet = new LineDataSet(entries, "Aktuel Vægt");

        styleLineDataSet(lineDataSet, true, Color.parseColor("#0000EE"), 1);
        return lineDataSet;
    }

    public PieData getFoodDistribution(Day day)
    {
        PieData pieData;

        double breakfast = 0;
        double lunch = 0;
        double dinner = 0;
        double snack = 0;

        for (int i = 0; i < day.getFoodWeighIns().size(); i++)
        {
            switch (day.getFoodWeighIns().get(i).getMealType())
            {
                case "Morgen":
                    breakfast += day.getFoodWeighIns().get(i).getFoodWeighIn();
                    break;
                case "Middag":
                    lunch += day.getFoodWeighIns().get(i).getFoodWeighIn();
                    break;
                case "Aften":
                    dinner += day.getFoodWeighIns().get(i).getFoodWeighIn();
                    break;
                case "Snack":
                    snack += day.getFoodWeighIns().get(i).getFoodWeighIn();
                    break;
            }
        }

        List<PieEntry> entries = new ArrayList<>(); // The pie chart equivilent of entry

        if (breakfast > 0.00f)
        {
            entries.add(new PieEntry((float) breakfast * 1000, "Morgen"));
        }
        if (lunch > 0.00f)
        {
            entries.add(new PieEntry((float) lunch * 1000, "Middag"));
        }
        if (dinner > 0.00f)
        {
            entries.add(new PieEntry((float) dinner * 1000, "Aften"));
        }
        if (snack > 0.00f)
        {
            entries.add(new PieEntry((float) snack * 1000, "Snack"));
        }

        PieDataSet set = new PieDataSet(entries, "");

        setPieDataSetStyle(set, 15, ColorTemplate.COLORFUL_COLORS);

        pieData = new PieData(set);
        return pieData;
    }

    public PieData getRecommendedFoodDistribution(double[] distribution)
    {
        PieData pieData;

        List<PieEntry> entries = new ArrayList<>();

        if (distribution[0] <= 0)
        {
            for (int i = 0; i < distribution.length; i++)
            {
                distribution[i] = 0;
            }
        }
        entries.add(new PieEntry((float)distribution[0], "Morgen"));
        entries.add(new PieEntry((float)distribution[1], "Middag"));
        entries.add(new PieEntry((float)distribution[2], "Aften"));
        entries.add(new PieEntry((float)distribution[3], "Snack"));

        PieDataSet set = new PieDataSet(entries, "");

        setPieDataSetStyle(set, 13, ColorTemplate.COLORFUL_COLORS);

        pieData = new PieData(set);

        return pieData;
    }

    private void styleLineDataSet(LineDataSet dataSet, boolean drawCircles, int color, float lineWidth)
    {
        dataSet.setDrawCircles(drawCircles);
        dataSet.setCircleRadius(2f);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setLineWidth(lineWidth);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(true);
    }

    public void setLineChartStyle(LineChart lineChart)
    {
        lineChart.getDescription().setEnabled(false);
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawLabels(false);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(true);
        yAxis.setDrawLabels(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);

        lineChart.setDrawGridBackground(false);
        lineChart.setBorderWidth(0.5f);
        lineChart.setBorderColor(Color.GRAY);
        lineChart.setDrawBorders(true);
    }

    public void setPieDataSetStyle(PieChart pieChart, String centerText)
    {
        pieChart.setCenterText(centerText);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getLegend().setEnabled(false); // some label
        pieChart.setTransparentCircleRadius(0);
        pieChart.setHoleRadius(40);
    }

    private void setPieDataSetStyle(PieDataSet set, float textSize, int[] colorTemplate)
    {
        set.setValueTextSize(textSize);
        set.setColors(colorTemplate);
    }
}
