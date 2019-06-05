package com.skydt.weightcontrol.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.models.BodyWeighIn;
import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.models.Diet;
import com.skydt.weightcontrol.services.BodyWeighInService;
import com.skydt.weightcontrol.services.ChartService;
import com.skydt.weightcontrol.services.DayService;
import com.skydt.weightcontrol.services.DietService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btnBodyWeighIn;
    private Button btnFood;
    private TextView tvCurrentDiet;
    private TextView tvDate;
    private TextView tvDailyWeightLoss;
    private TextView tvGoalWeight;
    private TextView tvMorningWeight;
    private TextView tvAllowedFood;
    private TextView tvBMI;
    private LineChart lineChart;

    private Intent intent;
    private int dietID;
    private Day day;
    private Diet diet;

    private DayService dayService;
    private DietService dietService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        loadInterface();
        setupInterfaceBasedOnSharedPreferences();
    }

    private void loadInterface()
    {
        Button btnNewDiet = findViewById(R.id.btnNewDiet);
        btnNewDiet.setOnClickListener(this);
        Button btnAllDiets = findViewById(R.id.btnAllDiets);
        btnAllDiets.setOnClickListener(this);
        btnBodyWeighIn = findViewById(R.id.btnBodyWeighIn);
        btnBodyWeighIn.setOnClickListener(this);
        btnFood = findViewById(R.id.btnFood);
        btnFood.setOnClickListener(this);

        tvCurrentDiet = findViewById(R.id.tvCurrentDiet);
        //tvCurrentDiet.getPaint().setUnderlineText(true);
        tvDate = findViewById(R.id.tvDate);
        tvDailyWeightLoss = findViewById(R.id.tvDailyWeightLoss);
        //tvDate.getPaint().setUnderlineText(true);
        tvGoalWeight = findViewById(R.id.tvGoalWeight);
        tvMorningWeight = findViewById(R.id.tvMorningWeight);
        tvAllowedFood = findViewById(R.id.tvAllowedFood);
        tvBMI = findViewById(R.id.tvBMI);
        setTextViewClickListener();

        lineChart = findViewById(R.id.linechart);
        lineChart.getDescription().setEnabled(false);
        lineChart.invalidate();
    }

    private void setupInterfaceBasedOnSharedPreferences()
    {
        checkSharedPreferences();
        if (dietID == 0)
        {
            intent = new Intent(this, DietCreationActivity.class);
            startActivity(intent);
            finish();
        } else
        {
            populateInterface();
        }
    }

    private void checkSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("menu_values", MODE_PRIVATE);
        dietID = sharedPreferences.getInt("dietID", 0);
    }

    private void populateInterface()
    {
        dietService = new DietService();
        BodyWeighInService bodyWeighInService = new BodyWeighInService();
        dayService = new DayService();
        day = dayService.loadDayByPrimaryKey(dayService.getCurrentDateAsString(), dietID, this);
        diet = dietService.loadDietByID(dietID, this);

        tvCurrentDiet.setText(diet.getDietName());
        tvDailyWeightLoss.setText(String.format(Locale.getDefault(), "%.0f", dietService.calculateDailyWeightLoss(diet)));
        tvDailyWeightLoss.append(" g");

        if (day.getDayID() != null)
        {
            resetColorAndBtnText();
            tvDate.setText(day.getSqlDate());
            tvGoalWeight.setText(String.format(Locale.getDefault(), "%.1f", day.getGoalWeight()));
            tvGoalWeight.append(" kg");
            tvMorningWeight.setText(String.format(Locale.getDefault(), "%.1f", day.getMorningWeight()));
            tvMorningWeight.append(" kg");
            tvAllowedFood.setText(String.format(Locale.getDefault(), "%.0f", dayService.convertRemainingFoodToGram(day.getAllowedFoodIntake())));
            tvAllowedFood.append(" g");
            tvBMI.setText(String.format(Locale.getDefault(), "%.1f", dietService.calculateBMI(bodyWeighInService.readLastBodyWeighInFromDay(day, this).getBodyWeight(), diet.getHeight())));
            setTextViewClickListener();
            setAllowedFoodColor();
        }
        else
        {
            dietComplete();
        }
        if (day.getMorningWeight() == 0 && day.getDayID() != null)
        {
            hideBtnAndAdjustText();
        }
        setChartData();
    }

    private void resetColorAndBtnText()
    {
        tvMorningWeight.setTextColor(Color.GRAY);
        tvAllowedFood.setTextColor(Color.GRAY);
        tvBMI.setTextColor(Color.GRAY);
        btnBodyWeighIn.setText(R.string.krop);
        btnFood.setClickable(true);
        btnFood.setAlpha(1);
    }

    private void hideBtnAndAdjustText()
    {
        tvMorningWeight.setTextColor(Color.RED);
        tvMorningWeight.setText(R.string.morgenvægt_fejl);
        tvAllowedFood.setTextColor(Color.RED);
        tvAllowedFood.setText(R.string.morgenvægt_fejl);
        tvBMI.setText(R.string.morgenvægt_fejl);
        tvBMI.setTextColor(Color.RED);
        btnBodyWeighIn.setText(R.string.morgenvægt);
        btnFood.setClickable(false);
        btnFood.setAlpha(0.2f);
    }

    private void dietComplete()
    {
        tvDate.setTextColor((Color.parseColor("#53C557")));
        tvDate.setText(R.string.done);
        tvDate.setClickable(false);
        tvDate.getPaint().setUnderlineText(false);
        tvGoalWeight.setText(String.format(Locale.getDefault(), "%.1f", diet.getDesiredWeight()));
        tvGoalWeight.append(" kg");
        tvMorningWeight.setTextColor((Color.parseColor("#53C557")));
        tvMorningWeight.setText(R.string.done);
        tvAllowedFood.setTextColor((Color.parseColor("#53C557")));
        tvAllowedFood.setText(R.string.done);
        tvBMI.setText(String.format(Locale.getDefault(), "%.1f", dietService.calculateBMI(diet.getDesiredWeight(), diet.getHeight())));

        btnBodyWeighIn.setClickable(false);
        btnBodyWeighIn.setAlpha(0.2f);
        btnFood.setClickable(false);
        btnFood.setAlpha(0.2f);
    }

    private void setAllowedFoodColor()
    {
        double allowedFood = day.getAllowedFoodIntake();

        if (allowedFood > 0)
        {
            tvAllowedFood.setTextColor(Color.parseColor("#53C557"));
        }
        else if ((int)allowedFood <= 0)
        {
            tvAllowedFood.setTextColor(Color.RED);
        }
    }

    private void setChartData()
    {
        ChartService chartService = new ChartService();
        BodyWeighInService bodyWeighInService = new BodyWeighInService();
        List<ILineDataSet> iLineDataSets = new ArrayList<>();   // Contains multiple LineDataSets
        String currentDate = dayService.getCurrentDateAsString();

        List<Day> completedDays = dayService.loadAllCompletedDaysFromDiet(dietID, currentDate, this);
        List<BodyWeighIn> bodyWeighIns = bodyWeighInService.readLastBodyWeighInFromCompletedDaysInDiet(completedDays, this);

        iLineDataSets.add(chartService.getDietWeightRange(diet));
        iLineDataSets.add(chartService.getLastWeighInOfDays(bodyWeighIns)); // Add weight plots to chart

        chartService.setLineChartStyle(lineChart); // style chart

        LineData lineData = new LineData(iLineDataSets); // molds the data
        lineChart.setData(lineData);    // display the data in graph
    }

    @Override
    public void onClick(View v)
    {
        Button button = (Button) v;
        int buttonID = button.getId();

        switch (buttonID)
        {
            case R.id.btnNewDiet:
                intent = new Intent(this, DietCreationActivity.class);
                startActivity(intent);
                break;

            case R.id.btnAllDiets:
                intent = new Intent(this, DietSelectionActivity.class);
                startActivity(intent);
                break;

            case R.id.btnBodyWeighIn:
                intent = new Intent(this, RegisterWeightActivity.class);
                intent.putExtra("btnText", btnBodyWeighIn.getText().toString());
                intent.putExtra("dietID", dietID);
                startActivity(intent);
                break;

            case R.id.btnFood:
                intent = new Intent(this, RegisterWeightActivity.class);
                intent.putExtra("btnText", btnFood.getText().toString());
                intent.putExtra("dietID", dietID);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void setTextViewClickListener()
    {
        View.OnClickListener textOnClick = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView tv = (TextView) v;
                int id = tv.getId();

                switch (id)
                {
                    case R.id.tvCurrentDiet:
                        intent = new Intent(MainMenuActivity.this, DietActivity.class);
                        intent.putExtra("dietID", diet.getDietID());
                        startActivity(intent);
                        break;

                    case R.id.tvDate:
                        intent = new Intent(MainMenuActivity.this, DayActivity.class);
                        intent.putExtra("dietID", diet.getDietID());
                        intent.putExtra("dayID", day.getSqlDate());
                        startActivity(intent);
                        break;
                }
            }
        };
        tvCurrentDiet.setOnClickListener(textOnClick);
        tvDate.setOnClickListener(textOnClick);
    }

    @Override
    protected void onRestart()
    {
        setupInterfaceBasedOnSharedPreferences();
        super.onRestart();
    }
}
