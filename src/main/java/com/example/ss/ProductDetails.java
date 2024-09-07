package com.example.ss;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;




public class ProductDetails extends AppCompatActivity {




    EditText productIdEditText, productNameEditText, deleteProductIdEditText;
    TextView manufactureDateTextView, expiryDateTextView, daysLeftTextView;
    Button manufactureDatePickerButton, expiryDatePickerButton, addButton, showAllButton, deleteButton,backToHomePageButton;




    Calendar manufactureCalendar, expiryCalendar;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DatabaseHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        productIdEditText = findViewById(R.id.productIdEditText);
        productNameEditText = findViewById(R.id.productNameEditText);
        manufactureDateTextView = findViewById(R.id.manufactureDateTextView);
        expiryDateTextView = findViewById(R.id.expiryDateTextView);
        daysLeftTextView = findViewById(R.id.daysLeftTextView);
        manufactureDatePickerButton = findViewById(R.id.manufactureDatePickerButton);
        expiryDatePickerButton = findViewById(R.id.expiryDatePickerButton);
        addButton = findViewById(R.id.addButton);
        showAllButton = findViewById(R.id.showAllButton);
        deleteProductIdEditText = findViewById(R.id.deleteProductIdEditText);
        deleteButton = findViewById(R.id.deleteButton);
        Button backToHomePageButton = findViewById(R.id.backToHomePageButton);

        backToHomePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add code to go back to home page
                Intent intent = new Intent(ProductDetails.this, HomeActivity .class); // Replace HomePageActivity with your home page activity
                startActivity(intent);
            }
        });


        manufactureCalendar = Calendar.getInstance();
        expiryCalendar = Calendar.getInstance();
        dbHelper = new DatabaseHelper(this);


        manufactureDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(manufactureCalendar, manufactureDateTextView);
            }
        });


        expiryDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(expiryCalendar, expiryDateTextView);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDatabase();
            }
        });


        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllData();
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }


    private void showDatePicker(final Calendar calendar, final TextView textView) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                textView.setText(sdf.format(calendar.getTime()));
            }
        };




        new DatePickerDialog(ProductDetails.this, dateSetListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void addToDatabase() {
        String productId = productIdEditText.getText().toString().trim();
        String productName = productNameEditText.getText().toString().trim();
        String manufactureDateString = manufactureDateTextView.getText().toString().trim();
        String expiryDateString = expiryDateTextView.getText().toString().trim();


        // Reset error colors
        productIdEditText.setError(null);
        productNameEditText.setError(null);
        manufactureDateTextView.setError(null);
        expiryDateTextView.setError(null);


        // Check for empty fields
        if (productId.isEmpty() || productName.isEmpty() || manufactureDateString.isEmpty() || expiryDateString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            // Highlight required fields in red
            if (productId.isEmpty()) productIdEditText.setError("Required field");
            if (productName.isEmpty()) productNameEditText.setError("Required field");
            if (manufactureDateString.isEmpty()) {
                manufactureDateTextView.setError("Required field");
                showMessage("Error", "Please choose manufacture date");
            }
            if (expiryDateString.isEmpty()) {
                expiryDateTextView.setError("Required field");
                showMessage("Error", "Please choose expiry date");
            }
            return;
        }


        // Check for duplicate product ID
        Cursor cursor = dbHelper.getData(productId);
        if (cursor != null && cursor.getCount() > 0) {
            showMessage("Error", "Product with ID " + productId + " already exists. Enter a new ID.");
            return;
        }


        try {
            Date manufactureDate = sdf.parse(manufactureDateString);
            Date expiryDate = sdf.parse(expiryDateString);


            if (expiryDate.before(manufactureDate)) {
                showMessage("Error", "Expiry date cannot be before manufacture date");
                return;
            }


            if (manufactureDate.after(Calendar.getInstance().getTime())) {
                showMessage("Error", "Manufacture date cannot be ahead of the current date");
                return;
            }


            long daysLeft = calculateDaysLeft(expiryDate);


            boolean inserted = dbHelper.insertData(productId, productName, manufactureDateString, expiryDateString, daysLeft);
            if (inserted) {
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private long calculateDaysLeft(Date expiryDate) {
        long currentTime = System.currentTimeMillis();
        long expiryTime = expiryDate.getTime();
        return (expiryTime - currentTime)  / (1000 * 60 * 60 * 24);
    }


    private void showAllData() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor.getCount() == 0) {
            // No data available
            showMessage("Error", "No data found");
            return;
        }


        StringBuilder data = new StringBuilder();
        while (cursor.moveToNext()) {
            data.append("Product ID: ").append(cursor.getString(1)).append("\n");
            data.append("Product Name: ").append(cursor.getString(2)).append("\n");
            data.append("Manufacture Date: ").append(cursor.getString(3)).append("\n");
            data.append("Expiry Date: ").append(cursor.getString(4)).append("\n");
            data.append("Days left for expiry: ").append(cursor.getLong(5)).append(" days").append("\n\n");
        }


        showMessage("All Data", data.toString());
    }


    private void deleteProduct() {
        String productIdToDelete = deleteProductIdEditText.getText().toString().trim();
        if (productIdToDelete.isEmpty()) {
            Toast.makeText(this, "Please enter a product ID to delete", Toast.LENGTH_SHORT).show();
            return;
        }


        int deletedRows = dbHelper.deleteData(productIdToDelete);
        if (deletedRows > 0) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No matching product found to delete", Toast.LENGTH_SHORT).show();
        }
    }


    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
