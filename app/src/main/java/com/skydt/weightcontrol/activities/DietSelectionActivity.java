package com.skydt.weightcontrol.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.adapters.DietAdapter;
import com.skydt.weightcontrol.models.Diet;
import com.skydt.weightcontrol.services.DayService;
import com.skydt.weightcontrol.services.DietService;

import java.util.List;

public class DietSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private DietService dietService;
    DayService dayService;
    private Intent intent;
    private int dietID;
    private List<Diet> diets;
    private AlertDialog.Builder alertBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_selection_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dietService = new DietService();
        dayService = new DayService();
        loadInterface();
    }

    private void loadInterface()
    {
        ListView listView = findViewById(R.id.lvDiets);

        diets = dietService.loadAllDiets(this);
        List<String> dates = dayService.loadAllStartAndEndDates(diets, this);

        DietAdapter dietAdapter = new DietAdapter(this, R.layout.custom_diet_list_cell, diets, dates);
        listView.setAdapter(dietAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Diet diet = (Diet) parent.getItemAtPosition(position);
        intent = new Intent(this, MainMenuActivity.class);
        dietID = diet.getDietID();
        saveIDInSharedPreferences();
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        final Diet diet = (Diet) parent.getItemAtPosition(position);

        alertBox = new AlertDialog.Builder(this);

        alertBox.setMessage(R.string.slet_diæt).setCancelable(false)
                .setPositiveButton(R.string.ja, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dietService.deleteDietByID(diet.getDietID(), DietSelectionActivity.this);
                        Toast.makeText(DietSelectionActivity.this, R.string.slet_diæet_bekræft, Toast.LENGTH_LONG).show();
                        loadInterface();

                        if (diets.size() > 0)
                        {
                            dietID = diets.get(0).getDietID();
                        } else
                        {
                            dietID = 0;
                        }
                        saveIDInSharedPreferences();
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
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.reset_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        alertBox = new AlertDialog.Builder(this);

        alertBox.setMessage(R.string.slet_diæter).setCancelable(false)
                .setPositiveButton(R.string.ja, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dietService.deleteAll(DietSelectionActivity.this);
                        dietID = 0;
                        saveIDInSharedPreferences();
                        Toast.makeText(DietSelectionActivity.this, R.string.slet_diæter_bekræft, Toast.LENGTH_LONG).show();
                        intent = new Intent(DietSelectionActivity.this, DietCreationActivity.class);
                        startActivity(intent);
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
        return true;
    }

    private void saveIDInSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("menu_values", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("dietID", dietID);
        editor.apply();
    }
}
