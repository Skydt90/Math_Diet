package com.skydt.weightcontrol.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BodyWeighIn
{
    private int bodyWeighInID;
    private Date dayID;
    private int dietID;
    private double bodyWeight;

    /*
    Constructors
     */
    public BodyWeighIn()
    {
    }

    public BodyWeighIn(int bodyWeighInID, Date dayID, int dietID, double bodyWeight)
    {
        this.bodyWeighInID = bodyWeighInID;
        this.dayID = dayID;
        this.dietID = dietID;
        this.bodyWeight = bodyWeight;
    }

    public BodyWeighIn(Date dayID, int dietID, double bodyWeight)
    {
        this.dayID = dayID;
        this.dietID = dietID;
        this.bodyWeight = bodyWeight;
    }


    /*
    Getters
     */
    public int getDietID() { return dietID; }
    public String getSQLDayID()
    {
        DateFormat correctFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return correctFormat.format(this.dayID);
    }
    public double getBodyWeight()
    {
        return bodyWeight;
    }


    /*
    Setters
     */
    public void setBodyWeight(double bodyWeight)
    {
        this.bodyWeight = bodyWeight;
    }
    public void setDayID(Date dayID) { this.dayID = dayID; }
    public void setDietID(int dietID) { this.dietID = dietID; }
    public void setBodyWeighInID(int bodyWeighInID)
    {
        this.bodyWeighInID = bodyWeighInID;
    }


    @Override
    public String toString()
    {
        return bodyWeight + " kg";
    }
}


