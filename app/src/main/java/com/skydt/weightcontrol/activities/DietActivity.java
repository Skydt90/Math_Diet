package com.skydt.weightcontrol.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.adapters.DayAdapter;
import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.models.Diet;
import com.skydt.weightcontrol.services.DayService;
import com.skydt.weightcontrol.services.DietService;

import java.util.Locale;

public class DietActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private TextView tvName;
    private TextView tvPeriod;
    private TextView tvStartWeight;
    private TextView tvDesiredWeight;
    private TextView tvDailyWeightLoss;
    private TextView tvNumberOfDays;
    private TextView tvTotalWeightLoss;
    private ListView lvDays;
    private DayAdapter dayAdapter;
    private DayService dayService;

    private int dietID;
    private Diet diet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dayService = new DayService();

        loadInterface();
        extractIntent();
        populateInterface();
        populateListView();
    }

    private void loadInterface()
    {
        tvName = findViewById(R.id.tvName);
        tvPeriod = findViewById(R.id.tvPeriod);
        tvStartWeight = findViewById(R.id.tvStartWeight);
        tvDesiredWeight = findViewById(R.id.tvDesiredWeight);
        tvDailyWeightLoss = findViewById(R.id.tvDailyWeightLoss);
        tvNumberOfDays = findViewById(R.id.tvNumberOfDays);
        tvTotalWeightLoss = findViewById(R.id.tvTotalWeightLoss);
        lvDays = findViewById(R.id.lvDays);
        lvDays.setOnItemClickListener(this);
    }

    private void extractIntent()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            dietID = bundle.getInt("dietID");
        }
        else finish();
    }

    private void populateInterface()
    {
        DietService dietService = new DietService();
        diet = dietService.loadDietByID(dietID, this);

        populateListView();
        double totalWeightLoss = dietService.calculateTotalWeightLoss(diet);

        tvName.setText(diet.getDietName());
        tvPeriod.setText(dayService.loadFirstAndLastDateInDanishFormat(dietID, this));
        tvStartWeight.setText(String.format(Locale.getDefault(), "%.1f", diet.getStartWeight()));
        tvStartWeight.append(" kg");
        tvDesiredWeight.setText(String.format(Locale.getDefault(), "%.1f", diet.getDesiredWeight()));
        tvDesiredWeight.append(" kg");
        tvDailyWeightLoss.setText(String.format(Locale.getDefault(), "%.0f", dietService.calculateDailyWeightLossInGram(diet)));
        tvDailyWeightLoss.append(" g");
        tvNumberOfDays.setText(dietService.calculateDietProgress(diet));
        tvTotalWeightLoss.setText(String.format(Locale.getDefault(), "%.1f", totalWeightLoss));
        tvTotalWeightLoss.append(" kg");
        lvDays.setOnItemClickListener(this);
        lvDays.setAdapter(dayAdapter);
    }

    private void populateListView()
    {
        int test = 0;
        diet.setDays(dayService.loadAllCompletedDaysFromDiet(dietID, dayService.getCurrentDateAsString(),this));
        dayAdapter = new DayAdapter(this, R.layout.custom_day_list_cell, diet.getDays());
        lvDays.setAdapter(dayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Day day = (Day) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, DayActivity.class);
        intent.putExtra("dietID", diet.getDietID());
        intent.putExtra("dayID", day.getSqlDate());
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        populateListView();
    }
}
