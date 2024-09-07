package com.example.ss;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class StockActivity extends AppCompatActivity {


    EditText editStockId, editStockName, editTotalStock, editRemainingStock, editDeleteStockId, editUpdateStockId, editUpdateRemainingStock, editNewTotalStock; // Add editNewTotalStock
    Button btnAddData, btnShowAllData, btnDeleteData, btnUpdateData;
    stDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


        dbHelper = new stDatabaseHelper(StockActivity.this);


        editStockId = findViewById(R.id.edit_stock_id);
        editStockName = findViewById(R.id.edit_stock_name); // *
        editTotalStock = findViewById(R.id.edit_total_stock); // *
        editRemainingStock = findViewById(R.id.edit_remaining_stock); // *
        editDeleteStockId = findViewById(R.id.edit_delete_stock_id);
        editUpdateStockId = findViewById(R.id.edit_update_stock_id);
        editUpdateRemainingStock = findViewById(R.id.edit_update_remaining_stock);
        editNewTotalStock = findViewById(R.id.edit_new_total_stock); // Initialize editNewTotalStock


        btnAddData = findViewById(R.id.btn_add_data);
        btnShowAllData = findViewById(R.id.btn_show_all_data);
        btnDeleteData = findViewById(R.id.btn_delete_data);
        btnUpdateData = findViewById(R.id.btn_update_data);


        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editStockId.getText().toString();
                String stockName = editStockName.getText().toString();
                String totalStock = editTotalStock.getText().toString(); // *
                String remainingStock = editRemainingStock.getText().toString(); // *
                String newTotalStock = editNewTotalStock.getText().toString(); // Get the value of the new total stock EditText field


                // Check if required fields are empty
                if (id.isEmpty() || totalStock.isEmpty() || remainingStock.isEmpty()) { // * Removed stockName check
                    Toast.makeText(StockActivity.this, "Enter required fields *", Toast.LENGTH_SHORT).show();
                    if (id.isEmpty()) {
                        editStockId.setError("Required field *");
                    }
                    if (stockName.isEmpty()) {
                        editStockName.setError("Required field *");
                    }
                    if (totalStock.isEmpty()) {
                        editTotalStock.setError("Required field *");
                    }
                    if (remainingStock.isEmpty()) {
                        editRemainingStock.setError("Required field *");
                    }
                    return;
                }


                // Check if stock ID already exists
                if (dbHelper.isStockIdExists(id)) {
                    Toast.makeText(StockActivity.this, "Stock ID already exists", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Check if total stock is greater than remaining stock
                if (Integer.parseInt(totalStock) < Integer.parseInt(remainingStock)) {
                    Toast.makeText(StockActivity.this, "Total stock should be greater than remaining stock", Toast.LENGTH_SHORT).show();
                    return;
                }


                boolean isInserted = dbHelper.insertData(id, stockName, totalStock, remainingStock); // Pass new total stock to insertData method
                if (isInserted)
                    Toast.makeText(StockActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StockActivity.this, "Failed to Insert Data", Toast.LENGTH_SHORT).show();
            }
        });


        btnShowAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = dbHelper.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "No data found");
                    return;
                }


                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Stock ID: ").append(res.getString(0)).append("\n");
                    buffer.append("Stock Name: ").append(res.getString(1)).append("\n");
                    buffer.append("Total Stock: ").append(res.getInt(2)).append("\n");
                    buffer.append("Remaining Stock: ").append(res.getInt(3)).append("\n\n");
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(StockActivity.this);
                builder.setTitle("All Data");
                builder.setMessage(buffer.toString());
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editDeleteStockId.getText().toString();


                // Check if data with the given ID exists before attempting to delete
                if (!dbHelper.isStockIdExists(id)) {
                    Toast.makeText(StockActivity.this, "Requested data not available to delete", Toast.LENGTH_SHORT).show();
                    return;
                }


                Integer deletedRows = dbHelper.deleteData(id);
                if (deletedRows > 0)
                    Toast.makeText(StockActivity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StockActivity.this, "Failed to Delete Data", Toast.LENGTH_SHORT).show();
            }
        });


        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editUpdateStockId.getText().toString();
                String remainingStock = editUpdateRemainingStock.getText().toString();
                String newTotalStock = editNewTotalStock.getText().toString(); // Get the value of the new total stock EditText field


                // Check if update ID, new total stock, and new remaining stock are not empty
                if (id.isEmpty() || remainingStock.isEmpty() || newTotalStock.isEmpty()) {
                    Toast.makeText(StockActivity.this, "Enter required fields *", Toast.LENGTH_SHORT).show();
                    if (id.isEmpty()) {
                        editUpdateStockId.setError("Required field *");
                    }
                    if (remainingStock.isEmpty()) {
                        editUpdateRemainingStock.setError("Required field *");
                    }
                    if (newTotalStock.isEmpty()) {
                        editNewTotalStock.setError("Required field *");
                    }
                    return;
                }


                // Check if data with the given ID exists before attempting to update
                if (!dbHelper.isStockIdExists(id)) {
                    Toast.makeText(StockActivity.this, "Requested data not available to update", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Check if new total stock is greater than or equal to remaining stock
                if (Integer.parseInt(newTotalStock) < Integer.parseInt(remainingStock)) {
                    Toast.makeText(StockActivity.this, "Total stock should be greater than or equal to remaining stock", Toast.LENGTH_SHORT).show();
                    return;
                }


                boolean isUpdated = dbHelper.updateData(id, remainingStock, newTotalStock);
                if (isUpdated)
                    Toast.makeText(StockActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StockActivity.this, "Failed to Update Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showMessage(String title, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
