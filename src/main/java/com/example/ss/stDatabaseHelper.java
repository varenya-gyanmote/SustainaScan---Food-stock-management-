package com.example.ss;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class stDatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "stock.db";
    public static final String TABLE_NAME = "stock_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "STOCK_NAME";
    public static final String COL_3 = "TOTAL_STOCK";
    public static final String COL_4 = "REMAINING_STOCK";
    public static final String COL_5 = "NEW_TOTAL_STOCK";


    public stDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID TEXT PRIMARY KEY, STOCK_NAME TEXT, TOTAL_STOCK INTEGER, REMAINING_STOCK INTEGER, NEW_TOTAL_STOCK INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String id, String stockName, String totalStock, String remainingStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, stockName);
        contentValues.put(COL_3, totalStock);
        contentValues.put(COL_4, remainingStock);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }


    public boolean updateData(String id, String remainingStock, String newTotalStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, newTotalStock);
        contentValues.put(COL_4, remainingStock);
        int updatedRows = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return updatedRows > 0;
    }


    public boolean isStockIdExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + "=?", new String[]{id});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
