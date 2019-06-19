package com.skydt.weightcontrol.services;

import android.content.Context;
import android.util.Log;

import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.models.FoodWeighIn;
import com.skydt.weightcontrol.repositories.FoodWeighInRepo;

import java.util.List;

public class FoodWeighInService
{
    private static final String TAG = "FoodWeighInService";
    private FoodWeighInRepo foodWeighInRepo;

    public FoodWeighInService()
    {
        foodWeighInRepo = new FoodWeighInRepo();
    }

    /*
    DB LOGIC
     */
    public void createWeighIn(FoodWeighIn foodWeighIn, Context context)
    {
        Log.d(TAG, "createWeighIn: Called");
        foodWeighIn.setFoodWeighIn(convertGramToKG(foodWeighIn.getFoodWeighIn()));
        foodWeighInRepo.createFoodWeighIn(foodWeighIn, context);
        Log.d(TAG, "createWeighIn: Finished");
    }

    public List<FoodWeighIn> readAllFoodWeighInsFromDay(Day day, Context context)
    {
        Log.d(TAG, "readAllFoodWeighInsFromDay: Called");
        return foodWeighInRepo.readAllFoodWeighInsFromDay(day, context);
    }

    public void deleteFoodWeighInByID(int foodWeighInID, Context context)
    {
        Log.d(TAG, "deleteFoodWeighInByID: called");
        foodWeighInRepo.deleteFoodWeighInByID(foodWeighInID, context);
        Log.d(TAG, "deleteFoodWeighInByID: finished");
    }

    /*
    BUSINESS LOGIC
     */
    private double convertGramToKG(double input)
    {
        return input /= 1000;
    }
}
