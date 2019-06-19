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

    public List<BodyWeighIn> readAllBodyWeighInsFromDay(Day day, Context context)
    {
        Log.d(TAG, "readAllBodyWeighInsFromDay: Called");
        return bodyWeighInRepo.readAllBodyWeighInsFromDay(day, context);
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

    public void deleteBodyWeighInByID(int bodyWeighInID, Context context)
    {
        Log.d(TAG, "deleteBodyWeighInByID: Called");
        bodyWeighInRepo.deleteBodyWeighInByID(bodyWeighInID, context);
        Log.d(TAG, "deleteBodyWeighInByID: Finished");
    }

    /*
    BUSINESS LOGIC
     */
}