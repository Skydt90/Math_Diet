package com.skydt.weightcontrol.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FoodWeighIn
{
    private Date dayID;
    private int dietID;
    private double foodWeighIn;
    private String mealType;
    private int foodWeighInID;

    /*
    Constructors
     */
    public FoodWeighIn(Date dayID, int dietID, double weighIn, String mealType)
    {
        this.dayID = dayID;
        this.dietID = dietID;
        this.foodWeighIn = weighIn;
        this.mealType = mealType;
    }

    public FoodWeighIn(Date dayID, int dietID, double foodWeighIn, String mealType, int foodWeighInID) {
        this.dayID = dayID;
        this.dietID = dietID;
        this.foodWeighIn = foodWeighIn;
        this.mealType = mealType;
        this.foodWeighInID = foodWeighInID;
    }

    /*
        Getters
         */
    public String getSQLDayID()
    {
        DateFormat correctFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return correctFormat.format(this.dayID);
    }
    public int getDietID() { return dietID; }
    public String getMealType()
    {
        return mealType;
    }
    public double getFoodWeighIn()
    {
        return foodWeighIn;
    }
    public int getFoodWeighInID() { return foodWeighInID; }

    /*
    Setters
     */
    public void setDietID(int dietID) { this.dietID = dietID; }
    public void setFoodWeighIn(double foodWeighIn)
    {
        this.foodWeighIn = foodWeighIn;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.getDefault(), "%.0f", foodWeighIn * 1000) + " g  -  " + mealType;
    }
}
