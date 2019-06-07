package com.skydt.weightcontrol.services;

import android.content.Context;
import android.util.Log;

import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.models.Diet;
import com.skydt.weightcontrol.models.FoodWeighIn;
import com.skydt.weightcontrol.repositories.DayRepo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayService
{
    public static final String TAG = "DayService";
    private DayRepo dayRepo;

    public DayService()
    {
        this.dayRepo = new DayRepo();
    }

    /*
    DB LOGIC
     */
    private void postDays(List<Day> days, Context context, int dietID)
    {
        Log.d(TAG, "postDays: Called");
        dayRepo.postDays(days, context, dietID);
        Log.d(TAG, "postDays: Finished");
    }

    public List<Day> loadAllCompletedDaysFromDiet(int dietID, String currentDate, Context context)
    {
        Log.d(TAG, "loadAllCompletedDaysFromDiet: Called");
        return dayRepo.loadAllCompletedDaysFromDiet(dietID, currentDate, context);
    }

    public Day loadDayByPrimaryKey(String dayID, int dietID, Context context)
    {
        Log.d(TAG, "loadDayByPrimaryKey: Called");
        Day day = dayRepo.loadDayByPrimaryKey(dayID, dietID, context);
        Log.d(TAG, "loadDayByPrimaryKey: Finished");
        return day;
    }

    public String loadFirstAndLastDateInDanishFormat(int dietID, Context context)
    {
        List<String> dates = dayRepo.loadFirstAndLastDate(dietID, context);
        DateFormat correctFormat;

        if (dates.size() > 0)
        {
            String first = dates.get(0);
            String last = dates.get(1);
            try
            {
                correctFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date1 = correctFormat.parse(first);
                Date date2 = correctFormat.parse(last);
                correctFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                first = correctFormat.format(date1);
                last = correctFormat.format(date2);
            }
            catch (ParseException pe)
            {
                Log.d(TAG, "loadFirstAndLastDateInDanishFormat: Error parsing date "+ pe);
            }
            return first.substring(0, first.length() - 5) + " - " + last;
        }
        return "";
    }

    public List<String> loadAllStartAndEndDates(List<Diet> diets, Context context)
    {
        List<String> dates = new ArrayList<>();
        for (Diet diet: diets)
        {
            dates.add(loadFirstAndLastDateInDanishFormat(diet.getDietID(), context));
        }
        return dates;
    }

    // Called after morning weigh in to calculate the total amount for the day
    private void updateAllowedFoodIntakeBasedOnMorningWeight(Day day, Context context)
    {
        Log.d(TAG, "updateAllowedFoodIntakeBasedOnWeighIn: Called");
        dayRepo.updateAllowedFoodIntakeBasedOnWeighIn(day, calculateFirstAllowedFoodIntake(day), context);
        Log.d(TAG, "updateAllowedFoodIntakeBasedOnWeighIn: Finished");
    }

    // Updates the morning weight for a specific day
    public void updateMorningWeight(Day day, double morningWeight, Context context)
    {
        Log.d(TAG, "updateMorningWeight: Called");
        dayRepo.updateMorningWeight(day, morningWeight, context);
        updateAllowedFoodIntakeBasedOnMorningWeight(day, context);
        Log.d(TAG, "updateMorningWeight: Finished");
    }

    public void updateAllowedFoodIntakeBasedOnFoodWeighIn(Day day, double enteredWeight, Context context)
    {
        Log.d(TAG, "updateRemainingFoodIntake: Called");
        dayRepo.updateAllowedFoodIntakeBasedOnWeighIn(day, calculateRemainingFoodIntakeBasedOnFoodWeighIn(day, enteredWeight), context);
        Log.d(TAG, "updateRemainingFoodIntake: Finished");
    }

    public void updateAllowedFoodIntakeBasedOnBodyWeighIn(Day day, double enteredWeight, Context context)
    {
        dayRepo.updateAllowedFoodIntakeBasedOnWeighIn(day, calculateRemainingFoodIntakeBasedOnBodyWeighIn(day, enteredWeight), context);
    }

    public void updateLikeOnDay(Day day, Context context, boolean like)
    {
        Log.d(TAG, "updateLikeOnDay: Called");
        dayRepo.updateLikeOnDay(day, context, like);
        Log.d(TAG, "updateLikeOnDay: Finished");
    }


    /*
    BUSINESS LOGIC
     */
    public void createDays(Diet diet, List<Date> dates, double dailyWeightLoss, Context context)
    {
        Log.d(TAG, "createDays: Called");
        for (Date date: dates)
        {
            Day day = new Day(date, diet.getDietID(), diet.getStartWeight(), 0.0);
            diet.addDay(day);
        }
        calculateDailyGoalWeight(diet, dailyWeightLoss);
        postDays(diet.getDays(), context, diet.getDietID());
    }

    private void calculateDailyGoalWeight(Diet diet, double dailyWeightLoss)
    {
        for (int i = 1; i <= diet.getDays().size()-1; i++)
        {
            double goalWeight = diet.getDays().get(i).getGoalWeight();
            diet.getDays().get(i).setGoalWeight(goalWeight - dailyWeightLoss * i);
        }
    }

    public double calculateAllowedFoodIntake(Day day)
    {
        return (day.getGoalWeight() - day.getMorningWeight()) * 1000;
    }

    // first time a morning weight is entered each day
    private double calculateFirstAllowedFoodIntake(Day day)
    {
        return day.getGoalWeight() - day.getMorningWeight();
    }

    public double calculateTotalFoodIntakeForDay(Day day)
    {
        double totalIntake = 0.0;
        if (day.getFoodWeighIns().size() > 0)
        {
            for (FoodWeighIn foodWeighIn: day.getFoodWeighIns())
            {
                totalIntake += foodWeighIn.getFoodWeighIn();
            }
            return totalIntake * 1000;
        }
        return totalIntake * 1000;
    }

    private double calculateRemainingFoodIntakeBasedOnFoodWeighIn(Day day, double enteredWeight)
    {
        return day.getAllowedFoodIntake() - enteredWeight;
    }

    private double calculateRemainingFoodIntakeBasedOnBodyWeighIn(Day day, double enteredWeight)
    {
        return day.getGoalWeight() - enteredWeight;
    }

    public double[] getRecommendedFoodDistribution(double allowedFoodIntake)
    {
        double[] foodDistribution = new double[3];
        foodDistribution[0] = (allowedFoodIntake / 100 * 25);
        foodDistribution[1] = (allowedFoodIntake / 100 * 30);
        foodDistribution[2] = (allowedFoodIntake / 100 * 45);
        return  foodDistribution;
    }

    public double convertRemainingFoodToGram(double allowedFoodIntake)
    {
        return allowedFoodIntake *= 1000;
    }

    public String getCurrentDateAsString()
    {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }
}
