package com.skydt.matematiskvgttab.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDatabase extends SQLiteOpenHelper
{
    private static final String TAG = "AppDatabase";
    private static final String DB_NAME = "weight_control.db";
    private static final int DB_VERSION = 1;
    private static AppDatabase instance = null;

    private AppDatabase(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "AppDatabase: Constructor called");
    }

    public static AppDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            Log.d(TAG, "getInstance: Creating a new instance of DB");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "onCreate: Called");

        String createDietTable = "CREATE TABLE "
                + DBContract.DietEntries.TABLE_NAME + "("
                + DBContract.DietEntries.DIET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DBContract.DietEntries.DIET_NAME + " VARCHAR(20), "
                + DBContract.DietEntries.START_WEIGHT + " DOUBLE, "
                + DBContract.DietEntries.DESIRED_WEIGHT + " DOUBLE, "
                + DBContract.DietEntries.NUMBER_OF_DAYS + " INTEGER, "
                + DBContract.DietEntries.HEIGHT + " DOUBLE);";
        db.execSQL(createDietTable);

        String createDayTable = "CREATE TABLE "
                + DBContract.DayEntries.TABLE_NAME + "("
                + DBContract.DayEntries.DAY_ID + " DATE NOT NULL, "
                + DBContract.DayEntries.DIET_ID + " INTEGER, "
                + DBContract.DayEntries.GOAL_WEIGHT + " DOUBLE, "
                + DBContract.DayEntries.MORNING_WEIGHT + " DOUBLE, "
                + DBContract.DayEntries.ALLOWED_FOOD_INTAKE + " DOUBLE, "
                + DBContract.DayEntries.LIKE_DISLIKE + " BOOL, "
                + "PRIMARY KEY(" + DBContract.DayEntries.DAY_ID + ", " + DBContract.DayEntries.DIET_ID + "),"
                + "FOREIGN KEY(" + DBContract.DayEntries.DIET_ID + ") "
                + "REFERENCES " + DBContract.DietEntries.TABLE_NAME + "(" + DBContract.DietEntries.DIET_ID + ") "
                + "ON DELETE CASCADE);";
        db.execSQL(createDayTable);

        String createFoodWeighInsTable = "CREATE TABLE "
                + DBContract.FoodWeightEntries.TABLE_NAME + "("
                + DBContract.FoodWeightEntries.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DBContract.FoodWeightEntries.DAY_ID + " DATE NOT NULL, "
                + DBContract.FoodWeightEntries.DIET_ID + " INTEGER NOT NULL, "
                + DBContract.FoodWeightEntries.WEIGHT + " DOUBLE NOT NULL, "
                + DBContract.FoodWeightEntries.MEAL_TYPE + " VARCHAR(9) NOT NULL, "
                + "FOREIGN KEY(" + DBContract.FoodWeightEntries.DAY_ID + ", " + DBContract.FoodWeightEntries.DIET_ID + ") "
                + "REFERENCES " + DBContract.DayEntries.TABLE_NAME + "(" + DBContract.DayEntries.DAY_ID + ", " + DBContract.DayEntries.DIET_ID + ") "
                + "ON DELETE CASCADE);";
        db.execSQL(createFoodWeighInsTable);

        String createBodyWeighInsTable = "CREATE TABLE "
                + DBContract.BodyWeightEntries.TABLE_NAME + "("
                + DBContract.BodyWeightEntries.BODY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DBContract.BodyWeightEntries.DAY_ID + " DATE NOT NULL, "
                + DBContract.BodyWeightEntries.DIET_ID + " INTEGER NOT NULL, "
                + DBContract.BodyWeightEntries.WEIGHT + " DOUBLE NOT NULL, "
                + "FOREIGN KEY(" + DBContract.BodyWeightEntries.DAY_ID + ", " + DBContract.BodyWeightEntries.DIET_ID + ") "
                + "REFERENCES " + DBContract.DayEntries.TABLE_NAME + "(" + DBContract.DayEntries.DAY_ID + ", " + DBContract.DayEntries.DIET_ID + ") "
                + "ON DELETE CASCADE);";
        db.execSQL(createBodyWeighInsTable);

        Log.d(TAG, "onCreate: Finished");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(TAG, "onUpgrade: Called");
        Log.d(TAG, "onUpgrade: Finished");
    }
}
