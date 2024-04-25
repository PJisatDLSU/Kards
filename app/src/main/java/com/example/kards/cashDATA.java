package com.example.kards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class cashDATA extends SQLiteOpenHelper {
    public cashDATA(Context context) {
        super(context, "cashDATA.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("cashDATA","onCreate called");
        db.execSQL("CREATE TABLE income(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL)");
        db.execSQL("CREATE TABLE expenses(id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL)");
    }

    public double getIncomeSum() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM income", null);
        double sum = 0;
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        cursor.close();
        return sum;
    }

    public double getExpenseSum() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM expenses", null);
        double sum = 0;
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        cursor.close();
        return sum;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cashDATA");
        onCreate(db);
    }

}