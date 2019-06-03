package com.skydt.matematiskvgttab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skydt.matematiskvgttab.R;
import com.skydt.matematiskvgttab.adapters.DayAdapter;
import com.skydt.matematiskvgttab.models.Day;
import com.skydt.matematiskvgttab.models.Diet;
import com.skydt.matematiskvgttab.services.DayService;
import com.skydt.matematiskvgttab.services.DietService;

import java.util.Locale;

public class DietActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private TextView tvName;
    private TextView tvStartWeight;
    private TextView tvDesiredWeight;
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
        dayService = new DayService();

        loadInterface();
        extractIntent();
        populateInterface();
        populateListView();
    }

    private void loadInterface()
    {
        tvName = findViewById(R.id.tvName);
        tvStartWeight = findViewById(R.id.tvStartWeight);
        tvDesiredWeight = findViewById(R.id.tvDesiredWeight);
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
        double totalWeightLoss = dietService.getTotalWeightLoss(diet);

        tvName.setText(diet.getDietName());
        tvStartWeight.setText(String.format(Locale.getDefault(), "%.1f", diet.getStartWeight()));
        tvStartWeight.append(" kg");
        tvDesiredWeight.setText(String.format(Locale.getDefault(), "%.1f", diet.getDesiredWeight()));
        tvDesiredWeight.append(" kg");
        tvNumberOfDays.setText(String.format(Locale.getDefault(), "%1d", diet.getDays().size()));
        tvNumberOfDays.append( " / " + String.format(Locale.getDefault(), "%1d", diet.getNumberOfDays()));
        tvTotalWeightLoss.setText(String.format(Locale.getDefault(), "%.1f", totalWeightLoss));
        tvTotalWeightLoss.append(" kg");
        lvDays.setOnItemClickListener(this);
        lvDays.setAdapter(dayAdapter);
    }

    private void populateListView()
    {
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
