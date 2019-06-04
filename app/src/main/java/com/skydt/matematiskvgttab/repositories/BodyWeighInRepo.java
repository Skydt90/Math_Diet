package com.skydt.matematiskvgttab.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.skydt.matematiskvgttab.database.AppDatabase;
import com.skydt.matematiskvgttab.database.DBContract;
import com.skydt.matematiskvgttab.models.BodyWeighIn;
import com.skydt.matematiskvgttab.models.Day;

import java.util.ArrayList;
import java.util.List;

public class BodyWeighInRepo
{
    private static final String TAG = "BodyWeighInRepo";
    private AppDatabase appDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public void createBodyWeighIn(BodyWeighIn bodyWeighIn, Context context)
    {
        Log.d(TAG, "createBodyWeighIn: called");
        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DBContract.BodyWeightEntries.DAY_ID, bodyWeighIn.getSQLDayID());
            values.put(DBContract.FoodWeightEntries.DIET_ID, bodyWeighIn.getDietID());
            values.put(DBContract.BodyWeightEntries.WEIGHT, bodyWeighIn.getBodyWeight());

            database.insertOrThrow(DBContract.BodyWeightEntries.TABLE_NAME, null, values);
        }
        catch (SQLiteException sqle)
        {
            Log.e(TAG, "createBodyWeighIn: SQLite Exception Thrown!", sqle);
        }
        catch (SQLException sqle)
        {
            Log.e(TAG, "createBodyWeighIn: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
            Log.d(TAG, "createBodyWeighIn: Finished");
        }
    }

    public List<BodyWeighIn> readAllBodyWeighInsFromDay(Day day, Context context)
    {
        Log.d(TAG, "readAllBodyWeighIns: Called");
        List<BodyWeighIn> bodyWeighIns = new ArrayList<>();

        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getReadableDatabase();

            String query = "SELECT * FROM " + DBContract.BodyWeightEntries.TABLE_NAME + " WHERE "
                                            + DBContract.BodyWeightEntries.DAY_ID + " = " + "\"" + day.getSqlDate() + "\""
                                            + " AND "
                                            + day.getDietID() + " = " + DBContract.BodyWeightEntries.DIET_ID;
            cursor = database.rawQuery(query, null, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    BodyWeighIn bodyWeighIn = new BodyWeighIn(cursor.getInt(0), day.getDayID(), day.getDietID(), cursor.getDouble(3) );
                    bodyWeighIns.add(bodyWeighIn);
                    cursor.moveToNext();
                }
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "readAllBodyWeighIns SQLite exception thrown: " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "readAllBodyWeighIns SQL exception thrown: " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "readAllBodyWeighInsFromDay: Finished");
        }
        return bodyWeighIns;
    }

    public List<BodyWeighIn> readLastBodyWeighInFromCompletedDaysInDiet(List<Day> completedDays, Context context)
    {
        Log.d(TAG, "readLastBodyWeighInFromCompletedDaysInDiet: Called");
        List<BodyWeighIn> bodyWeighIns = new ArrayList<>();

        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getReadableDatabase();

            for (Day day : completedDays)
            {
                String query = "SELECT * FROM " + DBContract.BodyWeightEntries.TABLE_NAME
                        + " WHERE " + DBContract.BodyWeightEntries.BODY_ID + " = "
                        + "(SELECT max(" + DBContract.BodyWeightEntries.BODY_ID + ")"
                        + " FROM " + DBContract.BodyWeightEntries.TABLE_NAME
                        + " WHERE " + DBContract.BodyWeightEntries.DIET_ID + " = " + day.getDietID()
                        + " AND " + DBContract.BodyWeightEntries.DAY_ID + " = " + "\"" + day.getSqlDate() + "\");";
                cursor = database.rawQuery(query, null, null);

                if (cursor != null && cursor.getCount() > 0)
                {
                    cursor.moveToFirst();
                    bodyWeighIns.add(new BodyWeighIn(cursor.getInt(0), day.getDayID(), day.getDietID(), cursor.getDouble(3)));
                }
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "readLastBodyWeighInFromCompletedDaysInDiet SQLite exception thrown: " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "readLastBodyWeighInFromCompletedDaysInDiet SQL exception thrown: " + sqle);
        }
        finally
        {
            database.close();
            if (cursor != null)
            {
                cursor.close();
            }
            Log.d(TAG, "readLastBodyWeighInFromCompletedDaysInDiet: Finished");
        }
        return bodyWeighIns;
    }

    public BodyWeighIn readLastBodyWeighInFromDay(Day day, Context context)
    {
        Log.d(TAG, "readLastBodyWeighInFromDay: Called");
        BodyWeighIn bodyWeighIn = new BodyWeighIn();

        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getReadableDatabase();

                String query = "SELECT " + DBContract.BodyWeightEntries.BODY_ID + ", " + DBContract.BodyWeightEntries.WEIGHT
                        + " FROM " + DBContract.BodyWeightEntries.TABLE_NAME
                        + " WHERE " + DBContract.BodyWeightEntries.BODY_ID + " = "
                        + "(SELECT max(" + DBContract.BodyWeightEntries.BODY_ID + ")"
                        + " FROM " + DBContract.BodyWeightEntries.TABLE_NAME
                        + " WHERE " + DBContract.BodyWeightEntries.DIET_ID + " = " + day.getDietID()
                        + " AND " + DBContract.BodyWeightEntries.DAY_ID + " = " + "\"" + day.getSqlDate() + "\");";
                cursor = database.rawQuery(query, null, null);

                if (cursor != null && cursor.getCount() > 0)
                {
                    cursor.moveToFirst();
                    bodyWeighIn.setBodyWeighInID(cursor.getInt(0));
                    bodyWeighIn.setDayID(day.getDayID());
                    bodyWeighIn.setDietID(day.getDietID());
                    bodyWeighIn.setBodyWeight(cursor.getDouble(1));
                    return bodyWeighIn;
                }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "readLastBodyWeighInFromDay SQLite exception thrown: " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "readLastBodyWeighInFromDay SQL exception thrown: " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "readLastBodyWeighInFromDay: Finished");
        }
        return bodyWeighIn;
    }
}
