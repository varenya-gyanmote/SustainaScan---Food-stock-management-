package com.example.ss;




import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;




public class SalesReportActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);




        stDatabaseHelper databaseHelper = new stDatabaseHelper(this);
        Cursor cursor = null;




        try {
            cursor = databaseHelper.getAllData();




            if (cursor != null && cursor.moveToFirst()) {
                TableLayout tableLayout = findViewById(R.id.tableLayout);




                TableRow columnNamesRow = new TableRow(this);
                String[] columnNames = {"Stock Id", "Stock Name", "Stock Sold", "Stock Remaining"};
                for (String columnName : columnNames) {
                    TextView column = new TextView(this);
                    //column.setText(columnName);
                    column.setGravity(android.view.Gravity.CENTER);
                    columnNamesRow.addView(column);
                }
                tableLayout.addView(columnNamesRow);




                do {
                    int idIndex = cursor.getColumnIndex(stDatabaseHelper.COL_1);
                    int nameIndex = cursor.getColumnIndex(stDatabaseHelper.COL_2);
                    int totalStockIndex = cursor.getColumnIndex(stDatabaseHelper.COL_3);
                    int remainingStockIndex = cursor.getColumnIndex(stDatabaseHelper.COL_4);




                    if (idIndex != -1 && nameIndex != -1 && totalStockIndex != -1 && remainingStockIndex != -1) {
                        String stockId = cursor.getString(idIndex);
                        String stockName = cursor.getString(nameIndex);
                        int totalStock = cursor.getInt(totalStockIndex);
                        int remainingStock = cursor.getInt(remainingStockIndex);




                        if (totalStock >= 0 && remainingStock >= 0) {
                            int stockSold = totalStock - remainingStock;




                            TableRow dataRow = new TableRow(this);




                            TextView tvStockId = new TextView(this);
                            tvStockId.setText(stockId);
                            tvStockId.setGravity(android.view.Gravity.CENTER);
                            dataRow.addView(tvStockId);




                            TextView tvStockName = new TextView(this);
                            tvStockName.setText(stockName);
                            tvStockName.setGravity(android.view.Gravity.CENTER);
                            dataRow.addView(tvStockName);




                            TextView tvStockSold = new TextView(this);
                            tvStockSold.setText(stockSold + " sold");
                            tvStockSold.setGravity(android.view.Gravity.CENTER);
                            dataRow.addView(tvStockSold);








                            TextView tvRemainingStock = new TextView(this);
                            tvRemainingStock.setText(remainingStock + " left");
                            tvRemainingStock.setGravity(android.view.Gravity.CENTER);
                            dataRow.addView(tvRemainingStock);




                            tableLayout.addView(dataRow);
                        } else {
                            Toast.makeText(this, "Invalid stock data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Invalid column indices", Toast.LENGTH_SHORT).show();
                    }
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
