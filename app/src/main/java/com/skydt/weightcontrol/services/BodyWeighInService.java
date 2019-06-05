package com.skydt.weightcontrol.services;

import android.content.Context;
import android.util.Log;

import com.skydt.weightcontrol.models.BodyWeighIn;
import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.repositories.BodyWeighInRepo;

import java.util.List;

public class BodyWeighInService
{
    private static final String TAG = "BodyWeighInService";
    private BodyWeighInRepo bodyWeighInRepo;

    /*
    DB LOGIC
     */
    public BodyWeighInService()
    {
        bodyWeighInRepo = new BodyWeighInRepo();
    }

    public void createWeighIn(BodyWeighIn bodyWeighIn, Context context)
    {
        Log.d(TAG, "createWeighIn: Called");
        bodyWeighInRepo.createBodyWeighIn(bodyWeighIn, context);
        Log.d(TAG, "createWeighIn: Finished");
    }

    public List<BodyWeighIn> readAllBodyWeighIns(Day day, Context context)
    {
        Log.d(TAG, "readAllBodyWeighIns: Called");
        List<BodyWeighIn> bodyWeighIns = bodyWeighInRepo.readAllBodyWeighInsFromDay(day, context);
        addBodyWeighInsToDay(day, bodyWeighIns);
        return  bodyWeighIns;
    }

    public List<BodyWeighIn> readLastBodyWeighInFromCompletedDaysInDiet(List<Day> completedDays, Context context)
    {
        Log.d(TAG, "readLastBodyWeighInFromCompletedDaysInDiet: Called");
        return bodyWeighInRepo.readLastBodyWeighInFromCompletedDaysInDiet(completedDays, context);
    }

    public BodyWeighIn readLastBodyWeighInFromDay(Day day, Context context)
    {
        Log.d(TAG, "readLastBodyWeighInFromDay: Called");
        return bodyWeighInRepo.readLastBodyWeighInFromDay(day, context);
    }


    /*
    BUSINESS LOGIC
     */
    private void addBodyWeighInsToDay(Day day, List<BodyWeighIn> foodWeighIns)
    {
        for (BodyWeighIn bodyWeighIn: foodWeighIns)
        {
            day.addBodyWeighIn(bodyWeighIn);
        }
    }
}