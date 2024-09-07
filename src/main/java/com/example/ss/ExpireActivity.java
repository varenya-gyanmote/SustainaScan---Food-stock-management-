package com.example.ss;




import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;




public class ExpireActivity extends AppCompatActivity {


    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire);


        databaseHelper = new DatabaseHelper(this);


        displayExpiringProducts();
    }


    private void displayExpiringProducts() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);


        Cursor cursor = databaseHelper.getAllData();


        if (cursor != null && cursor.moveToFirst()) {
            int productIdIndex = cursor.getColumnIndex(databaseHelper.getProductIdColumnName());
            int productNameIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_NAME);
            int daysLeftIndex = cursor.getColumnIndex(DatabaseHelper.COL_DAYS_LEFT);


            if (productIdIndex != -1 && productNameIndex != -1 && daysLeftIndex != -1) {
                do {
                    String productId = cursor.getString(productIdIndex);
                    String productName = cursor.getString(productNameIndex);
                    int daysLeft = cursor.getInt(daysLeftIndex);
                    // Check if daysLeft is less than 3
                    if (daysLeft <= 3) {
                        TableRow row = new TableRow(this);

                        // Add TextView for Product ID
                        TextView tvProductId = new TextView(this);
                        tvProductId.setText("   "+productId);
                        row.addView(tvProductId);

                        // Add TextView for Product Name
                        TextView tvProductName = new TextView(this);
                        tvProductName.setText("   "+productName);
                        row.addView(tvProductName);
                        // Add TextView for Days Left
                        TextView tvDaysLeft = new TextView(this);
                        tvDaysLeft.setText(String.valueOf("    "+daysLeft));
                        row.addView(tvDaysLeft);
                        tableLayout.addView(row);
                    }
                } while (cursor.moveToNext());
            }


            cursor.close();
        }
    }
}
