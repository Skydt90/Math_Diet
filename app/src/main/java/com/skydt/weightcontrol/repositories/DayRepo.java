package com.skydt.weightcontrol.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.skydt.weightcontrol.database.AppDatabase;
import com.skydt.weightcontrol.database.DBContract;
import com.skydt.weightcontrol.models.Day;

import java.util.ArrayList;
import java.util.List;

public class DayRepo
{
    private static final String TAG = "DayRepo";
    private AppDatabase appDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public void postDays(List<Day> days, Context context, int dietID)
    {
        Log.d(TAG, "postDays: Called");
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();
            for (Day day : days)
            {
                values.put(DBContract.DayEntries.DAY_ID, day.getSqlDate());
                values.put(DBContract.DayEntries.DIET_ID, dietID);
                values.put(DBContract.DayEntries.GOAL_WEIGHT, day.getGoalWeight());
                values.put(DBContract.DayEntries.MORNING_WEIGHT, day.getMorningWeight());
                values.put(DBContract.DayEntries.ALLOWED_FOOD_INTAKE, 0);
                values.put(DBContract.DayEntries.LIKE_DISLIKE, false);

                database.insertOrThrow(DBContract.DayEntries.TABLE_NAME, null, values);
            }
        }
        catch (SQLiteException sqle)
        {
            Log.e(TAG, "postDays: SQLite Exception Thrown!", sqle);
        }
        catch (SQLException sqle)
        {
            Log.e(TAG, "postDays: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
            Log.d(TAG, "postDays: Finished");
        }
    }

    public List<Day> loadAllCompletedDaysFromDiet(int dietID, String currentDate, Context context)
    {
        Log.d(TAG, "loadAllCompletedDaysFromDiet: Called");
        List<Day> days = new ArrayList<>();
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getReadableDatabase();

            String query = "SELECT * FROM " + DBContract.DayEntries.TABLE_NAME
                                            + " WHERE " + DBContract.DayEntries.DIET_ID
                                            + " = " + dietID + " AND " + DBContract.DayEntries.DAY_ID
                                            + " <= DATE('" + currentDate + "')";
            cursor = database.rawQuery(query,null, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    Day day = new Day();
                    day.setDayIDByString(cursor.getString(0));
                    day.setDietID(cursor.getInt(1));
                    day.setGoalWeight(cursor.getDouble(2));
                    day.setMorningWeight(cursor.getDouble(3));
                    day.setAllowedFoodIntake(cursor.getDouble(4));
                    day.setLike((Boolean.parseBoolean(cursor.getString(5))));
                    days.add(day);
                    cursor.moveToNext();
                }
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "loadAllCompletedDaysFromDiet: SQLiteException " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "loadAllCompletedDaysFromDiet: SQLException " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "loadAllCompletedDaysFromDiet: Finished");
        }
        return  days;
    }

    public List<Day> loadAllNonCompletedDaysFromDiet(int dietID, String currentDate, Context context)
    {
        Log.d(TAG, "loadAllNonCompletedDaysFromDiet: Called");
        List<Day> days = new ArrayList<>();
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getReadableDatabase();

            String query = "SELECT * FROM " + DBContract.DayEntries.TABLE_NAME
                    + " WHERE " + DBContract.DayEntries.DIET_ID
                    + " = " + dietID + " AND " + DBContract.DayEntries.DAY_ID
                    + " >= DATE('" + currentDate + "')";
            cursor = database.rawQuery(query,null, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    Day day = new Day();
                    day.setDayIDByString(cursor.getString(0));
                    day.setDietID(cursor.getInt(1));
                    day.setGoalWeight(cursor.getDouble(2));
                    day.setMorningWeight(cursor.getDouble(3));
                    day.setAllowedFoodIntake(cursor.getDouble(4));
                    day.setLike((Boolean.parseBoolean(cursor.getString(5))));
                    days.add(day);
                    cursor.moveToNext();
                }
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "loadAllCompletedDaysFromDiet: SQLiteException " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "loadAllCompletedDaysFromDiet: SQLException " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "loadAllCompletedDaysFromDiet: Finished");
        }
        return  days;
    }

    public List<Day> loadAllDaysFromDiet(int dietID, Context context)
    {
        Log.d(TAG, "loadAllDaysFromDiet: Called");
        List<Day> days = new ArrayList<>();
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getReadableDatabase();

            String query = "SELECT * FROM " + DBContract.DayEntries.TABLE_NAME
                    + " WHERE " + DBContract.DayEntries.DIET_ID
                    + " = " + dietID;
            cursor = database.rawQuery(query,null, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    Day day = new Day();
                    day.setDayIDByString(cursor.getString(0));
                    day.setDietID(cursor.getInt(1));
                    day.setGoalWeight(cursor.getDouble(2));
                    day.setMorningWeight(cursor.getDouble(3));
                    day.setAllowedFoodIntake(cursor.getDouble(4));
                    day.setLike((Boolean.parseBoolean(cursor.getString(5))));
                    days.add(day);
                    cursor.moveToNext();
                }
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "loadAllDaysFromDiet: SQLiteException " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "loadAllDaysFromDiet: SQLException " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "loadAllDaysFromDiet: Finished");
        }
        return  days;
    }

    public List<String> loadFirstAndLastDate(int dietID, Context context)
    {
        Log.d(TAG, "loadFirstAndLastDate: Called" );
        appDatabase = AppDatabase.getInstance(context);
        List<String> dates = new ArrayList<>();

        try
        {
            database = appDatabase.getReadableDatabase();

            String query = "SELECT MIN(" + DBContract.DayEntries.DAY_ID + ")," +
                                 " MAX(" + DBContract.DayEntries.DAY_ID + ") FROM " +
                                   DBContract.DayEntries.TABLE_NAME + " WHERE " +
                                   DBContract.DayEntries.DIET_ID + " = " + dietID + ";";

            cursor = database.rawQuery(query, null, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                dates.add(cursor.getString(0));
                dates.add(cursor.getString(1));
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "loadFirstAndLastDate: SQLiteException " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "loadFirstAndLastDate: SQLException " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "loadFirstAndLastDate: Finished");
        }
        return dates;
    }

    public Day loadDayByPrimaryKey(String dayID, int dietID, Context context)
    {
        Log.d(TAG, "loadDayByPrimaryKey: Called");
        appDatabase = AppDatabase.getInstance(context);
        Day day = new Day();

        try
        {
            database = appDatabase.getReadableDatabase();

            String[] projection = {
                                DBContract.DayEntries.DAY_ID,
                                DBContract.DayEntries.DIET_ID,
                                DBContract.DayEntries.GOAL_WEIGHT,
                                DBContract.DayEntries.MORNING_WEIGHT,
                                DBContract.DayEntries.ALLOWED_FOOD_INTAKE,
                                DBContract.DayEntries.LIKE_DISLIKE };

            String selection = DBContract.DayEntries.DAY_ID + " = ? AND " + DBContract.DayEntries.DIET_ID + " = ?";
            String[] selectionArgs = {dayID, String.valueOf(dietID)};

            cursor = database.query(DBContract.DayEntries.TABLE_NAME, projection, selection, selectionArgs, null,null,null);

            if (cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();

                day.setDayIDByString(cursor.getString(0));
                day.setDietID(cursor.getInt(1));
                day.setGoalWeight(cursor.getDouble(2));
                day.setMorningWeight(cursor.getDouble(3));
                day.setAllowedFoodIntake(cursor.getDouble(4));
                day.setLike((Boolean.parseBoolean(cursor.getString(5))));

                Log.d(TAG, "loadDayByPrimaryKey: " + day.getAllowedFoodIntake());
                return day;
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "loadDayByPrimaryKey: " + sqle);
        }
        catch(SQLException sqle)
        {
            Log.d(TAG, "loadDayByPrimaryKey: " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "loadDayByPrimaryKey: Finished");
        }
        return day;
    }

    public void updateMorningWeight(Day day, double morningWeight, Context context)
    {
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DBContract.DayEntries.MORNING_WEIGHT, morningWeight);

            String selection = DBContract.DayEntries.DAY_ID + " = ? AND " + DBContract.DayEntries.DIET_ID + "  = ?";
            String[] selectionArgs = {day.getSqlDate(), String.valueOf(day.getDietID())};

            database.update(DBContract.DayEntries.TABLE_NAME, values, selection, selectionArgs);
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "updateMorningWeight SQLiteException: " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "updateMorningWeight SQLException: " + sqle);
        }
        finally
        {
            database.close();
            Log.d(TAG, "updateMorningWeight: Finished");
        }
    }

    public void updateAllowedFoodIntakeBasedOnWeighIn(Day day, double dailyAllowedFoodIntake, Context context)
    {
        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getWritableDatabase();

            String query =  "UPDATE " + DBContract.DayEntries.TABLE_NAME +
                            " SET " + DBContract.DayEntries.ALLOWED_FOOD_INTAKE + " = " + dailyAllowedFoodIntake +
                            " WHERE " + DBContract.DayEntries.DAY_ID + " = " + "\"" + day.getSqlDate() + "\"" +
                            " AND " + DBContract.DayEntries.DIET_ID + " = " + day.getDietID();
            cursor = database.rawQuery(query, null, null);
            cursor.moveToFirst();
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "updateAllowedFoodIntakeBasedOnWeighIn: " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "updateAllowedFoodIntakeBasedOnWeighIn: " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "updateAllowedFoodIntakeBasedOnWeighIn: Finished");
        }
    }

    public void updateLikeOnDay(Day day, Context context, boolean like)
    {
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getWritableDatabase();

            String query =  "UPDATE " + DBContract.DayEntries.TABLE_NAME +
                            " SET " + DBContract.DayEntries.LIKE_DISLIKE + " = " + "\"" + like + "\"" +
                            " WHERE " + DBContract.DayEntries.DAY_ID + " = " + "\"" + day.getSqlDate() + "\"" +
                            " AND " + DBContract.DayEntries.DIET_ID + " = " + day.getDietID();
            cursor = database.rawQuery(query, null, null);
            cursor.moveToFirst();
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "updateLikeOnDay SQLiteException: " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "updateLikeOnDay SQLException: " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "updateLikeOnDay: Finished");
        }
    }
}
