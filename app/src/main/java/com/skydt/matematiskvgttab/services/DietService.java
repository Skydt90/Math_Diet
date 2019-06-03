package com.skydt.matematiskvgttab.services;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.skydt.matematiskvgttab.models.Diet;
import com.skydt.matematiskvgttab.repositories.DietRepo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DietService
{
    private static final String TAG = "DietService";
    private DietRepo dietRepo;

    public DietService()
    {
        this.dietRepo = new DietRepo();
    }


    /*
    DB LOGIC
     */
    public long postDiet(Diet diet, Context context)
    {
        Log.d(TAG, "postDiet: Called");
        long ID = dietRepo.postDiet(diet, context);
        Log.d(TAG, "postDiet: Finished");
        return ID;
    }

    public Diet loadDietByID(int id, Context context)
    {
        Log.d(TAG, "loadDiet: Called");
        return dietRepo.loadDietByID(id, context);
    }

    public List<Diet> loadAllDiets(Context context)
    {
        Log.d(TAG, "loadAllDiets: Called");
        return dietRepo.loadAllDiets(context);
    }

    public void deleteAll(Context context)
    {
        Log.d(TAG, "deleteAllDiets: Called");
        dietRepo.deleteAll(context);
        Log.d(TAG, "deleteAllDiets: Finished");
    }

    public void deleteDietByID(int dietID, Context context)
    {
        Log.d(TAG, "deleteDietByID: Called");
        dietRepo.deleteDietByID(dietID, context);
        Log.d(TAG, "deleteDietByID: Finished");
    }


    /*
    BUSINESS LOGIC
     */
    public double calculateDailyWeightLoss(Diet diet)
    {
        return (diet.getStartWeight() - diet.getDesiredWeight()) / diet.getNumberOfDays();
    }

    public double calculateDailyWeightLoss(double startWeight, double desiredWeight, int daysInDiet)
    {
        return  (startWeight - desiredWeight) / daysInDiet;
    }

    public List<Date> createDietDates(Diet diet)
    {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Date> dates = new ArrayList<>();
        String endDate = "";
        Date date1 = null;
        Date date2 = null;
        Calendar c1;
        Calendar c2;

        //Increment Days
        c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_MONTH, diet.getNumberOfDays());
        endDate = formatter.format(c1.getTime());

        try
        {
            date1 = formatter.parse(formatter.format(Calendar.getInstance().getTime()));
            date2 = formatter.parse(endDate);
        }
        catch (ParseException pe)
        {
            Log.d(TAG, "createDietDates: ParseException thrown: ", pe);
        }

        c1 = Calendar.getInstance();
        c1.setTime(date1);

        c2 = Calendar.getInstance();
        c2.setTime(date2);

        while(!c1.after(c2))
        {
            dates.add(c1.getTime());
            c1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public double calculateBMI(double weight, double height)
    {
        return weight / height / height * 10000;
    }

    public boolean isEmpty(EditText editText)
    {
        String input = editText.getText().toString().trim();
        return input.length() == 0;
    }

    public void setError(EditText editText, String errorString)
    {
        editText.setError(errorString);
    }

    public double getTotalWeightLoss(Diet diet)
    {
        double startWeight = diet.getStartWeight();
        double morningWeight = diet.getDays().get(diet.getDays().size() -1).getMorningWeight();
        return startWeight - morningWeight;
    }
}
