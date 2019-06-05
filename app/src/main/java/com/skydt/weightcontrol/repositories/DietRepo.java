package com.skydt.weightcontrol.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.skydt.weightcontrol.models.Diet;
import com.skydt.weightcontrol.database.AppDatabase;
import com.skydt.weightcontrol.database.DBContract;

import java.util.ArrayList;
import java.util.List;

public class DietRepo
{
    private static final String TAG = "DietRepo";
    private AppDatabase appDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public long postDiet(Diet diet, Context context)
    {
        Log.d(TAG, "postDiet: Called");
        appDatabase = AppDatabase.getInstance(context);
        long ID = 0;
        try
        {
            database = appDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DBContract.DietEntries.DIET_NAME, diet.getDietName());
            values.put(DBContract.DietEntries.START_WEIGHT, diet.getStartWeight());
            values.put(DBContract.DietEntries.DESIRED_WEIGHT, diet.getDesiredWeight());
            values.put(DBContract.DietEntries.NUMBER_OF_DAYS, diet.getNumberOfDays());
            values.put(DBContract.DietEntries.HEIGHT, diet.getHeight());

            ID = database.insertOrThrow(DBContract.DietEntries.TABLE_NAME, null, values);
        }
        catch (SQLiteException sqle)
        {
            Log.e(TAG, "postDiet: SQLite Exception Thrown!", sqle);
        }
        catch (SQLException sqle)
        {
            Log.e(TAG, "postDiet: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
            Log.d(TAG, "postDiet: Finished");
        }
        return ID;
    }

    public Diet loadDietByID(int id, Context context)
    {
        Log.d(TAG, "loadDiet: Called");
        appDatabase = AppDatabase.getInstance(context);
        Diet diet = new Diet();
        try
        {
            database = appDatabase.getReadableDatabase();

            String[] projection = {DBContract.DietEntries.DIET_ID, DBContract.DietEntries.DIET_NAME,
                                   DBContract.DietEntries.START_WEIGHT, DBContract.DietEntries.DESIRED_WEIGHT,
                                   DBContract.DietEntries.NUMBER_OF_DAYS, DBContract.DietEntries.HEIGHT};

            String selection = DBContract.DietEntries.DIET_ID + " = ?";
            String[] selectionArgs = {Integer.toString(id)};

            cursor = database.query(DBContract.DietEntries.TABLE_NAME, projection, selection,
                            selectionArgs, null, null, null);

            if (cursor != null)
            {
                cursor.moveToFirst();

                diet.setDietID(cursor.getInt(0));
                diet.setDietName(cursor.getString(1));
                diet.setStartWeight(cursor.getDouble(2));
                diet.setDesiredWeight(cursor.getDouble(3));
                diet.setNumberOfDays(cursor.getInt(4));
                diet.setHeight(cursor.getDouble(5));

                return diet;
            }
        }
        catch(SQLiteException sqle)
        {
            Log.e(TAG, "loadDietByID: SQLite Exception Thrown!", sqle);
        }
        catch(SQLException sqle)
        {
            Log.e(TAG, "loadDietByID: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
            cursor.close();
        }
        return diet;
    }

    public List<Diet> loadAllDiets(Context context)
    {
        Log.d(TAG, "loadAllDiets: Called");
        appDatabase = AppDatabase.getInstance(context);
        List<Diet> diets = new ArrayList<>();
        try
        {
            database = appDatabase.getReadableDatabase();
            String query = "SELECT * FROM " + DBContract.DietEntries.TABLE_NAME;

            cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst())
            {
                do
                {
                    Diet diet = new Diet();
                    diet.setDietID(cursor.getInt(0));
                    diet.setDietName(cursor.getString(1));
                    diet.setStartWeight(cursor.getDouble(2));
                    diet.setDesiredWeight(cursor.getDouble(3));
                    diet.setNumberOfDays(cursor.getInt(4));
                    diet.setHeight(cursor.getDouble(5));

                    diets.add(diet);
                } while ((cursor.moveToNext()));
                return diets;
            }
        }
        catch(SQLiteException sqle)
        {
            Log.e(TAG, "loadAllDiets: SQLite Exception Thrown!", sqle);
        }
        catch(SQLException sqle)
        {
            Log.e(TAG, "loadAllDiets: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
            cursor.close();
        }
        return diets;
    }

    public void deleteDietByID(int dietID, Context context)
    {
        Log.d(TAG, "deleteDietByID: Called");
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getWritableDatabase();

            String selection = DBContract.DietEntries.DIET_ID + " = ?";
            String[] selectionArgs = {Integer.toString(dietID)};

            int deletedRows = database.delete(DBContract.DietEntries.TABLE_NAME, selection, selectionArgs);
            Log.d(TAG, "deleteDietByID: finished with: " + deletedRows + " deleted rows");
        }
        catch(SQLiteException sqle)
        {
            Log.e(TAG, "deleteDietByID: SQLite Exception Thrown!", sqle);
        }
        catch(SQLException sqle)
        {
            Log.e(TAG, "deleteDietByID: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
        }
    }

    public void deleteAll(Context context)
    {
        Log.d(TAG, "deleteAll: Called");
        appDatabase = AppDatabase.getInstance(context);

        try
        {
            database = appDatabase.getWritableDatabase();

            String selection = DBContract.DietEntries.DIET_ID + " >= ?";
            String[] selectionArgs = {"1"};

            int deletedRows = database.delete(DBContract.DietEntries.TABLE_NAME, selection, selectionArgs);
            Log.d(TAG, "deleteAll: finished with: " + deletedRows + " deleted rows");
        }
        catch(SQLiteException sqle)
        {
            Log.e(TAG, "deleteAll: SQLite Exception Thrown!", sqle);
        }
        catch(SQLException sqle)
        {
            Log.e(TAG, "deleteAll: SQL Exception Thrown!", sqle);
        }
        finally
        {
            database.close();
        }
    }
}
