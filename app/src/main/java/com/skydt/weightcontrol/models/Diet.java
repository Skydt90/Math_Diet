package com.skydt.weightcontrol.models;

import java.util.ArrayList;
import java.util.List;

public class Diet
{
    private int dietID;
    private String dietName;
    private double startWeight;
    private double desiredWeight;
    private int numberOfDays;
    private double height;
    private List<Day> days;
    private boolean paused;

    /*
    Constructors
     */
    public Diet()
    {
        this.days = new ArrayList<>();
    }

    public Diet(String dietName, double startWeight, double desiredWeight, int numberOfDays, double height, boolean paused)
    {
        this.dietName = dietName;
        this.startWeight = startWeight;
        this.desiredWeight = desiredWeight;
        this.numberOfDays = numberOfDays;
        this.height = height;
        this.paused = paused;
        this.days = new ArrayList<>();
    }


    /*
    Getters
     */
    public List<Day> getDays()
    {
        if (this.days == null)
        {
            return new ArrayList<>();
        }
        return days;
    }
    public int getDietID()
    {
        return dietID;
    }
    public String getDietName()
    {
        return dietName;
    }
    public double getStartWeight()
    {
        return startWeight;
    }
    public double getDesiredWeight()
    {
        return desiredWeight;
    }
    public int getNumberOfDays()
    {
        return numberOfDays;
    }
    public double getHeight()
    {
        return height;
    }
    public boolean getPaused() { return paused; }


    /*
    Setters
     */
    public void setDietID(int dietID)
    {
        this.dietID = dietID;
    }
    public void setDietName(String dietName)
    {
        this.dietName = dietName;
    }
    public void setStartWeight(double startWeight)
    {
        this.startWeight = startWeight;
    }
    public void setDesiredWeight(double desiredWeight)
    {
        this.desiredWeight = desiredWeight;
    }
    public void setDays(List<Day> days)
    {
        this.days = days;
    }
    public void setNumberOfDays(int numberOfDays)
    {
        this.numberOfDays = numberOfDays;
    }
    public void setHeight(double height)
    {
        this.height = height;
    }
    public void setPaused(boolean paused) { this.paused = paused; }
    public void addDay(Day day)
    {
        days.add(day);
    }

    @Override
    public String toString()
    {
        return "Diet{" +
                "dietID=" + dietID +
                ", dietName='" + dietName + '\'' +
                ", startWeight=" + startWeight +
                ", desiredWeight=" + desiredWeight +
                ", numberOfDays=" + numberOfDays +
                ", height=" + height +
                '}';
    }




}
