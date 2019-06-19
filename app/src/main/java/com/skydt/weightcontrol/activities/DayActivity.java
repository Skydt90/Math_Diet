package com.skydt.weightcontrol.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.models.BodyWeighIn;
import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.models.FoodWeighIn;
import com.skydt.weightcontrol.services.BodyWeighInService;
import com.skydt.weightcontrol.services.ChartService;
import com.skydt.weightcontrol.services.DayService;
import com.skydt.weightcontrol.services.FoodWeighInService;

import java.util.List;
import java.util.Locale;

public class DayActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener
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
    private FoodWeighInService foodWeighInService;
    private BodyWeighInService bodyWeighInService;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        lvBody.setOnItemLongClickListener(this);
        lvFood.setOnItemLongClickListener(this);

        foodWeighInService = new FoodWeighInService();
        bodyWeighInService = new BodyWeighInService();
        chartService = new ChartService();
        dayService = new DayService();
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
            day = dayService.loadDayByPrimaryKey(dayID, dietID, this);
            day.setFoodWeighIns(foodWeighInService.readAllFoodWeighInsFromDay(day, this));
            day.setBodyWeighIns(bodyWeighInService.readAllBodyWeighInsFromDay(day, this));

            tvDate.setText(day.getDateAsDanishDisplayText());
            tvGoalWeight.setText(String.format(Locale.getDefault(), "%.1f", day.getGoalWeight()));
            tvGoalWeight.append(" kg");
            tvMorningWeight.setText(String.format(Locale.getDefault(), "%.1f", day.getMorningWeight()));
            tvMorningWeight.append(" kg");

            if (day.getMorningWeight() == 0)
            {
                tvAllowedFood.setText(R.string.error);
            }
            else
            {
                tvAllowedFood.setText(String.format(Locale.getDefault(), "%.0f", dayService.calculateAllowedFoodIntake(day)));
                tvAllowedFood.append(" g");
            }
            tvTotalFoodEaten.setText(String.format(Locale.getDefault(), "%.0f", dayService.calculateTotalFoodIntakeForDay(day)));
            tvTotalFoodEaten.append(" g");
            cbLikeDay.setChecked(day.getLike());
            populateAdapters();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
    {
        if (!day.getSqlDate().equals(dayService.getCurrentDateAsString()))
        {
            Toast.makeText(this, "Ugyldig sletning!", Toast.LENGTH_LONG).show();
        }
        else
        {
            AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
            switch (parent.getId())
            {
                case R.id.lvBody:
                    final BodyWeighIn bodyWeighIn = (BodyWeighIn) parent.getItemAtPosition(position);
                    if (day.getBodyWeighIns().indexOf(bodyWeighIn) != 0)
                    {
                        Toast.makeText(DayActivity.this, "Kan kun slette seneste vejning!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        alertBox.setMessage("Ønsker du at slette vægten?").setCancelable(false).setPositiveButton(R.string.ja, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                bodyWeighInService.deleteBodyWeighInByID(bodyWeighIn.getBodyWeighInID(), DayActivity.this);
                                day.getBodyWeighIns().remove(bodyWeighIn);
                                if (!day.getBodyWeighIns().isEmpty())
                                {
                                    dayService.updateAllowedFoodIntakeBasedOnBodyWeighIn(day, day.getBodyWeighIns().get(0).getBodyWeight(), DayActivity.this);
                                    populateAdapters();
                                }
                                else
                                {
                                    day.setMorningWeight(0);
                                    dayService.updateMorningWeight(day, day.getMorningWeight(), DayActivity.this);
                                    populateInterface();
                                }
                                Toast.makeText(DayActivity.this, "Vægt slettet", Toast.LENGTH_LONG).show();
                            }
                        })
                                .setNegativeButton(R.string.nej, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertBox.create();
                        alertDialog.setTitle(R.string.advarsel);
                        alertDialog.show();
                    }
                    break;
                case R.id.lvFood:
                    final FoodWeighIn foodWeighIn = (FoodWeighIn) parent.getItemAtPosition(position);
                    alertBox.setMessage("Ønsker du at slette madindtastningen?").setCancelable(false)
                            .setPositiveButton(R.string.ja, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    foodWeighInService.deleteFoodWeighInByID(foodWeighIn.getFoodWeighInID(), DayActivity.this);
                                    dayService.updateAllowedFoodIntakeBasedOnFoodWeighIn(day, -foodWeighIn.getFoodWeighIn(), DayActivity.this);
                                    day.getFoodWeighIns().remove(foodWeighIn);
                                    Toast.makeText(DayActivity.this, "Indtastning slettet", Toast.LENGTH_LONG).show();
                                    populateInterface();
                                }
                            })
                            .setNegativeButton(R.string.nej, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertBox.create();
                    alertDialog.setTitle(R.string.advarsel);
                    alertDialog.show();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public void onCheckboxClicked(View view)
    {
        boolean checked = cbLikeDay.isChecked();
        dayService.updateLikeOnDay(day, this, checked);
    }

    private void populateAdapters()
    {
        ArrayAdapter<FoodWeighIn> foodAdapter = new ArrayAdapter<>(this, R.layout.custom_text_view_for_lists, day.getFoodWeighIns());
        lvFood.setAdapter(foodAdapter);
        ArrayAdapter<BodyWeighIn> bodyAdapter = new ArrayAdapter<>(this, R.layout.custom_text_view_for_lists, day.getBodyWeighIns());
        lvBody.setAdapter(bodyAdapter);
    }
}
