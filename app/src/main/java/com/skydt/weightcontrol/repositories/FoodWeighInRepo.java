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
import com.skydt.weightcontrol.models.FoodWeighIn;

import java.util.ArrayList;
import java.util.List;

public class FoodWeighInRepo
{
    private static final String TAG ="FoodWeighInRepo";
    private AppDatabase appDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public void createFoodWeighIn(FoodWeighIn foodWeighIn, Context context)
    {
        Log.d(TAG, "createFoodWeighIn: Called");
        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DBContract.FoodWeightEntries.DAY_ID, foodWeighIn.getSQLDayID());
            values.put(DBContract.FoodWeightEntries.WEIGHT, foodWeighIn.getFoodWeighIn());
            values.put(DBContract.FoodWeightEntries.DIET_ID, foodWeighIn.getDietID());
            values.put(DBContract.FoodWeightEntries.MEAL_TYPE, foodWeighIn.getMealType());

            database.insertOrThrow(DBContract.FoodWeightEntries.TABLE_NAME, null, values);
        }
        catch (SQLiteException sqle)
        {
            Log.e(TAG, "createFoodWeighIn: SQLite Exception Thrown!", sqle);
        }
        catch (SQLException sqle)
        {
            Log.e(TAG, "createFoodWeighIn: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
            Log.d(TAG, "createFoodWeighIn: Finished");
        }
    }

    public List<FoodWeighIn> readAllFoodWeighInsFromDay(Day day, Context context)
    {
        Log.d(TAG, "readAllFoodWeighInsFromDay: Called");
        List<FoodWeighIn> foodWeighIns = new ArrayList<>();
        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getReadableDatabase();

            String query = "SELECT * FROM " + DBContract.FoodWeightEntries.TABLE_NAME
                                            + " WHERE " + DBContract.FoodWeightEntries.DAY_ID + " = " + "\"" + day.getSqlDate() + "\""
                                            + " AND "
                                            + DBContract.FoodWeightEntries.DIET_ID + " = " + day.getDietID()
                                            + " ORDER BY " + DBContract.FoodWeightEntries.FOOD_ID + " DESC";
            cursor = database.rawQuery(query,null,null);

            if (cursor != null)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    FoodWeighIn foodWeighIn = new FoodWeighIn(day.getDayID(), day.getDietID(), cursor.getDouble(3), cursor.getString(4), cursor.getInt(0));
                    foodWeighIns.add(foodWeighIn);
                    cursor.moveToNext();
                }
            }
        }
        catch (SQLiteException sqle)
        {
            Log.d(TAG, "readAllFoodWeighInsFromDay SQLite exception: " + sqle);
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "readAllFoodWeighInsFromDay SQL Exception: " + sqle);
        }
        finally
        {
            database.close();
            cursor.close();
            Log.d(TAG, "readAllFoodWeighInsFromDay: Finished");
        }
        return foodWeighIns;
    }

    public void deleteFoodWeighInByID(int foodWeighInID, Context context)
    {
        appDatabase = AppDatabase.getInstance(context);
        try
        {
            database = appDatabase.getWritableDatabase();

            String selection = DBContract.FoodWeightEntries.FOOD_ID + " = ?";
            String[] selectionArgs = {(Integer.toString(foodWeighInID))};
            int deleteRows = database.delete(DBContract.FoodWeightEntries.TABLE_NAME, selection, selectionArgs);

            Log.d(TAG, "deleteFoodWeighInByID: finished with: " + deleteRows + " deleted rows");
        }
        catch(SQLiteException sqle)
        {
            Log.d(TAG, "deleteFoodWeighInByID SQLiteException thrown: " + sqle );
        }
        catch (SQLException sqle)
        {
            Log.d(TAG, "deleteFoodWeighInByID SQLException thrown: " + sqle);
        }
        finally
        {
            database.close();
        }
    }
}
