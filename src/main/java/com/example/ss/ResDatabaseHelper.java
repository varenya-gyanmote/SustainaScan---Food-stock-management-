package com.example.ss;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import java.util.concurrent.Callable;

public class ResDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PRODUCT_RECORD";
    private static final String TABLE_NAME = "PRODUCT_DATA";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "QUANTITY";
    private static final String COL_4 = "ITEM1";
    private static final String COL_5 = "ITEM2";
    private static final String COL_6 = "ITEM3";
    private static final String COL_7 = "PHONE";




    public ResDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, NAME TEXT, QUANTITY TEXT, ITEM1 TEXT, ITEM2 TEXT, ITEM3 TEXT, PHONE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id, String name, String quan, String item1, String item2, String item3, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // ID already exists, return false
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put(COL_2, name);
        values.put(COL_3, quan);
        values.put(COL_4, item1);
        values.put(COL_5, item2);
        values.put(COL_6, item3);
        values.put(COL_7, phoneNumber);
        long var = db.insert(TABLE_NAME, null, values);
        if (var == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " +TABLE_NAME+ " WHERE ID='" +id+"'";
        Cursor cursor = db.rawQuery(query , null);
        return cursor;
    }


    public boolean updateData(String id , String name ,String quan ,String item1 ,String item2 ,String item3, String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1 , id);
        contentValues.put(COL_2 , name);
        contentValues.put(COL_3 , quan);
        contentValues.put(COL_4 , item1);
        contentValues.put(COL_5 , item2);
        contentValues.put(COL_6 , item3);
        contentValues.put(COL_7 , phoneNumber);
        db.update(TABLE_NAME , contentValues , "ID=?" , new  String[]{id});
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME , "ID=?" , new String[]{id});
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        return cursor;
    }

    public Integer deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME , null , null);
    }
}
