package com.skydt.weightcontrol.models;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Day
{
    private static final String TAG = "Day Model";

    private Date dayID;
    private int dietID;
    private double goalWeight;
    private double morningWeight;
    private double allowedFoodIntake;
    private boolean like;
    private List<BodyWeighIn> bodyWeighIns;
    private List<FoodWeighIn> foodWeighIns;

    /*
    Constructors
     */
    public Day()
    {
        bodyWeighIns = new ArrayList<>();
        foodWeighIns = new ArrayList<>();
    }

    public Day(Date dayID, int dietID, double goalWeight, double morningWeight)
    {
        this.dayID = dayID;
        this.dietID = dietID;
        this.goalWeight = goalWeight;
        this.morningWeight = morningWeight;
        bodyWeighIns = new ArrayList<>();
        foodWeighIns = new ArrayList<>();
    }


    /*
    Getters
     */
    public Date getDayID()
    {
        return this.dayID;
    }
    public String getSqlDate()
    {
        DateFormat correctFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return correctFormat.format(this.dayID);
    }
    public int getDietID()
    {
        return dietID;
    }
    public double getAllowedFoodIntake()
    {
        return allowedFoodIntake;
    }
    public double getMorningWeight()
    {
        return morningWeight;
    }
    public boolean getLike()
    {
        return like;
    }
    public double getGoalWeight()
    {
        return goalWeight;
    }
    public List<FoodWeighIn> getFoodWeighIns()
    {
        return this.foodWeighIns;
    }
    public List<BodyWeighIn> getBodyWeighIns() {return this.bodyWeighIns;}


    /*
    Setters
     */
    public void setDietID(int dietID)
    {
        this.dietID = dietID;
    }
    public void setGoalWeight(double goalWeight)
    {
        this.goalWeight = goalWeight;
    }
    public void setMorningWeight(double morningWeight)
    {
        this.morningWeight = morningWeight;
    }
    public void setAllowedFoodIntake(double allowedFoodIntake)
    {
        this.allowedFoodIntake = allowedFoodIntake;
    }
    public void setFoodWeighIns(List<FoodWeighIn> foodWeighIns)
    {
        this.foodWeighIns = foodWeighIns;
    }
    public void setBodyWeighIns(List<BodyWeighIn> bodyWeighIns)
    {
        this.bodyWeighIns = bodyWeighIns;
    }
    public void setDayIDByString(String date)
    {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try
        {
            this.dayID = formatter.parse(date);
        }
        catch (ParseException pe)
        {
            Log.e(TAG, "setDayIDByString: " + pe);
        }
    }
    public void setLike(boolean like)
    {
        this.like = like;
    }


    /*
    Custom
     */
    public void addFoodWeighIn(FoodWeighIn foodWeighIn)
    {
        foodWeighIns.add(foodWeighIn);
    }

    public void addBodyWeighIn(BodyWeighIn bodyWeighIn)
    {
        bodyWeighIns.add(bodyWeighIn);
    }

    public String getDateAsDanishDisplayText()
    {
        DateFormat correctFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return correctFormat.format(this.dayID);
    }

    @Override
    public String toString()
    {
        return "Day{" +
                "dayID=" + dayID +
                ", dietID=" + dietID +
                ", goalWeight=" + goalWeight +
                ", morningWeight=" + morningWeight +
                ", allowedFoodIntake=" + allowedFoodIntake +
                ", like=" + like +
                '}';
    }
}
