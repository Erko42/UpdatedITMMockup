package com.example.eriko.updateditmmockup.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String EVENT_TABLE = "Event_table";
    public static String USER_TABLE = "User_table";

    public DatabaseHelper(Context context) {
        super(context, "db", null, 2);
    }
     
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EVENT_TABLE + " (" +
                "ID INTEGER PRIMARY KEY, " +
                "AppName TEXT, " +
                "CustomerID INTEGER, " +
                "ProjectDuration TEXT, " +
                "BackgroundImg TEXT, " +
                "HideInMultiApp INTEGER, " +
                "ProjectID INTEGER)");

        db.execSQL("create table " + USER_TABLE + " (" +
                "ID INTEGER PRIMARY KEY, " +
                "Email TEXT, " +
                "Password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public void insertData(String table, HashMap<String, String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        for (set.iterator(); iterator.hasNext();) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("key is: " + mentry.getKey() + " & Value is: ");
            System.out.println(mentry.getValue());
            contentValues.put(mentry.getKey().toString(), mentry.getValue().toString());
        }
        db.insert(table, null, contentValues);
    }

    public Cursor getAllData(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + table, null);
    }

    public boolean updateData(String table, String id, HashMap<String, String> hashMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        for (set.iterator(); iterator.hasNext();) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("key is: " + mentry.getKey() + " & Value is: ");
            System.out.println(mentry.getValue());
            contentValues.put(mentry.getKey().toString(), mentry.getValue().toString());
        }
        db.update(table, contentValues, "ID = ?", new String[] { id });
        return true;
    }

    public void deleteData(String table, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + table + " where ID = '" + id + "'");
        db.execSQL("UPDATE " + table + " set ID = (ID - 1) WHERE ID > " + id);
    }

    public void deleteAllData(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + table);
    }
}