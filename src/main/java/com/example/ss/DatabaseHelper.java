package com.example.ss;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {




    private static final String DATABASE_NAME = "product_db";
    private static final String TABLE_NAME = "products";
    private static final String COL_ID = "id";
    private static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    private static final String COL_MANUFACTURE_DATE = "manufacture_date";
    public static final String COL_EXPIRY_DATE = "expiry_date";
    public static final String COL_DAYS_LEFT = "days_left";


    public String getProductIdColumnName() {
        return COL_PRODUCT_ID;
    }
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PRODUCT_ID + " TEXT, "
                + COL_PRODUCT_NAME + " TEXT, "
                + COL_MANUFACTURE_DATE + " TEXT, "
                + COL_EXPIRY_DATE + " TEXT, "
                + COL_DAYS_LEFT + " INTEGER)";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String productId, String productName, String manufactureDate, String expiryDate, long daysLeft) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PRODUCT_ID, productId);
        contentValues.put(COL_PRODUCT_NAME, productName);
        contentValues.put(COL_MANUFACTURE_DATE, manufactureDate);
        contentValues.put(COL_EXPIRY_DATE, expiryDate);
        contentValues.put(COL_DAYS_LEFT, daysLeft);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    public int deleteData(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_PRODUCT_ID + " = ?", new String[]{productId});
    }


    public Cursor getExpiringData(String currentDate, String threeDaysFromNow) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + COL_PRODUCT_NAME +
                        " FROM " + TABLE_NAME +
                        " WHERE " + COL_EXPIRY_DATE + " BETWEEN ? AND ?",
                new String[]{currentDate, threeDaysFromNow});
    }
    public Cursor getData(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_PRODUCT_ID + " = ?",
                new String[]{productId});
    }


}


