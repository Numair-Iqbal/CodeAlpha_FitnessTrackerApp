package com.numair.fitnesstrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fitness.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "fitness_entries";
    private static final String COL_ID = "id";
    private static final String COL_DATE = "date";
    private static final String COL_EXERCISE = "exercise_type";
    private static final String COL_STEPS = "steps";
    private static final String COL_CALORIES = "calories";
    private static final String COL_MINUTES = "workout_minutes";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " TEXT, " +
                COL_EXERCISE + " TEXT, " +
                COL_STEPS + " INTEGER, " +
                COL_CALORIES + " INTEGER, " +
                COL_MINUTES + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // Entry add karo
    public void addEntry(String date, String exerciseType, int steps, int calories, int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATE, date);
        values.put(COL_EXERCISE, exerciseType);
        values.put(COL_STEPS, steps);
        values.put(COL_CALORIES, calories);
        values.put(COL_MINUTES, minutes);
        db.insert(TABLE, null, values);
        db.close();
    }

    // Sab entries lo
    public List<FitnessEntry> getAllEntries() {
        List<FitnessEntry> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " ORDER BY " + COL_ID + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new FitnessEntry(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Aaj ki entries lo
    public List<FitnessEntry> getTodayEntries(String today) {
        List<FitnessEntry> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE " + COL_DATE + "=?", new String[]{today});
        if (cursor.moveToFirst()) {
            do {
                list.add(new FitnessEntry(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Total steps, calories, minutes lo kisi date ke liye
    public int[] getTotalsForDate(String date) {
        int[] totals = {0, 0, 0};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_STEPS + "), SUM(" + COL_CALORIES + "), SUM(" + COL_MINUTES + ") FROM " + TABLE + " WHERE " + COL_DATE + "=?", new String[]{date});
        if (cursor.moveToFirst()) {
            totals[0] = cursor.getInt(0);
            totals[1] = cursor.getInt(1);
            totals[2] = cursor.getInt(2);
        }
        cursor.close();
        db.close();
        return totals;
    }

    // Entry update karo
    public void updateEntry(int id, String exerciseType, int steps, int calories, int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EXERCISE, exerciseType);
        values.put(COL_STEPS, steps);
        values.put(COL_CALORIES, calories);
        values.put(COL_MINUTES, minutes);
        db.update(TABLE, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Entry delete karo
    public void deleteEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}