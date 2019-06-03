package com.skydt.matematiskvgttab.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.skydt.matematiskvgttab.R;
import com.skydt.matematiskvgttab.models.BodyWeighIn;
import com.skydt.matematiskvgttab.models.Day;
import com.skydt.matematiskvgttab.models.FoodWeighIn;
import com.skydt.matematiskvgttab.services.BodyWeighInService;
import com.skydt.matematiskvgttab.services.ChartService;
import com.skydt.matematiskvgttab.services.DayService;
import com.skydt.matematiskvgttab.services.FoodWeighInService;

import java.util.List;
import java.util.Locale;

public class DayActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "DayActivity";
    private TextView tvDate;
    private TextView tvGoalWeight;
    private TextView tvMorningWeight;
    private TextView tvAllowedFood;
    private TextView tvTotalFoodEaten;
    private ListView lvBody;
    private ListView lvFood;
    private PieChart pieChart;
    private ChartService chartService;
    private CheckBox cbLikeDay;
    private DayService dayService;

    private int dietID;
    private String dayID;

    private Day day;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);
        dayService = new DayService();
        loadInterface();
        extractIntent();
        populateInterface();
    }

    private void loadInterface()
    {
        tvDate = findViewById(R.id.tvDate);
        tvGoalWeight = findViewById(R.id.tvGoalWeight);
        tvMorningWeight = findViewById(R.id.tvMorningWeight);
        tvAllowedFood = findViewById(R.id.tvAllowedFood);
        tvTotalFoodEaten = findViewById(R.id.tvFoodEaten);
        lvBody = findViewById(R.id.lvBody);
        lvFood = findViewById(R.id.lvFood);
        pieChart = findViewById(R.id.piechart);
        cbLikeDay = findViewById(R.id.cbLikeDay);
        Button btnFoodDistribution = findViewById(R.id.btnPieChart);
        btnFoodDistribution.setOnClickListener(this);
    }

    private void extractIntent()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            dietID = bundle.getInt("dietID");
            dayID = bundle.getString("dayID");
        }
        else
        {
            dietID = 0;
            dayID = "";
        }
    }

    private void populateInterface()
    {
        if (dietID != 0 && !dayID.equals(""))
        {
            DayService dayService = new DayService();
            FoodWeighInService foodWeighInService = new FoodWeighInService();
            day = dayService.loadDayByPrimaryKey(dayID, dietID, this);
            day.setFoodWeighIns(foodWeighInService.readAllFoodWeighInsFromDay(day, this));
            tvDate.setText(day.getSqlDate());
            tvGoalWeight.setText(String.format(Locale.getDefault(), "%.1f", day.getGoalWeight()));
            tvGoalWeight.append(" kg");
            tvMorningWeight.setText(String.format(Locale.getDefault(), "%.1f", day.getMorningWeight()));
            tvMorningWeight.append(" kg");
            tvAllowedFood.setText(String.format(Locale.getDefault(), "%.3f", dayService.calculateAllowedFoodIntake(day)));
            tvAllowedFood.append(" kg");
            tvTotalFoodEaten.setText(String.format(Locale.getDefault(), "%.3f", dayService.calculateTotalFoodIntakeForDay(day)));
            tvTotalFoodEaten.append(" kg");
            cbLikeDay.setChecked(day.getLike());

            List<FoodWeighIn> food = foodWeighInService.readAllFoodWeighInsFromDay(day, this);
            ArrayAdapter<FoodWeighIn> foodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, food);
            lvFood.setAdapter(foodAdapter);

            BodyWeighInService bodyWeighInService = new BodyWeighInService();
            List<BodyWeighIn> body = bodyWeighInService.readAllBodyWeighIns(day, this);
            ArrayAdapter<BodyWeighIn> bodyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, body);
            lvBody.setAdapter(bodyAdapter);

            chartService = new ChartService();
            pieChart.setData(chartService.getFoodDistribution(day));
            chartService.setPieDataSetStyle(pieChart, "Aktuel");
            pieChart.invalidate();
        }
    }

    @Override
    public void onClick(View v)
    {
        Button button = (Button) v;

        switch (button.getText().toString())
        {
            case "Anbefalet":
                double allowedFoodIntake = dayService.calculateAllowedFoodIntake(day);
                pieChart.setData(chartService.getRecommendedFoodDistribution(dayService.getRecommendedFoodDistribution(allowedFoodIntake)));
                chartService.setPieDataSetStyle(pieChart, button.getText().toString());
                button.setText(R.string.aktuel_madfordeling);
                break;
            case "Aktuel":
                Log.d(TAG, "DayActivity: Called " + button.getText());
                pieChart.setData(chartService.getFoodDistribution(day));
                chartService.setPieDataSetStyle(pieChart, button.getText().toString());
                button.setText(R.string.anbefalet_madfordeling);
                break;
            default:
                break;
        }
        pieChart.invalidate();
    }

    public void onCheckboxClicked(View view)
    {
        boolean checked = cbLikeDay.isChecked();
        dayService.updateLikeOnDay(day, this, checked);
    }
}
