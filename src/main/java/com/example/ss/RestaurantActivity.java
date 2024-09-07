package com.example.ss;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RestaurantActivity extends AppCompatActivity {

    private ResDatabaseHelper myDB;
    private EditText idEdit, nameEdit, quanEdit, item1Edit, item2Edit, item3Edit, phoneNumberEdit;
    private Button addButton, deleteButton, showButton, showAllButton, deleteAllButton, updateButton, backToHomePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);

        myDB = new ResDatabaseHelper(RestaurantActivity.this);

        idEdit = findViewById(R.id.idid);
        nameEdit = findViewById(R.id.name);
        quanEdit = findViewById(R.id.email);
        item1Edit = findViewById(R.id.item1);
        item2Edit = findViewById(R.id.item2);
        item3Edit = findViewById(R.id.item3);
        phoneNumberEdit = findViewById(R.id.phoneNumber);

        addButton = findViewById(R.id.addbtn);
        deleteAllButton = findViewById(R.id.deleteallbtn);
        deleteButton = findViewById(R.id.deletebtn);
        showAllButton = findViewById(R.id.showallbtn);
        showButton = findViewById(R.id.showbtn);
        updateButton = findViewById(R.id.updatebtn);

        addData();
        showData();
        updateData();
        deleteData();
        getAllData();
        deleteAllData();
    }

    public void addData() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String id = idEdit.getText().toString();

                    // Check if ID already exists
                    Cursor cursor = myDB.getData(id);
                    if (cursor.getCount() > 0) {
                        showMessage("Error", "ID already exists. Enter a new ID.");
                        return;
                    }

                    boolean isInserted = myDB.insertData(
                            id,
                            nameEdit.getText().toString(),
                            quanEdit.getText().toString(),
                            item1Edit.getText().toString(),
                            item2Edit.getText().toString(),
                            item3Edit.getText().toString(),
                            phoneNumberEdit.getText().toString()
                    );

                    if (isInserted) {
                        showOrderSuccessDialog();
                    } else {
                        Toast.makeText(RestaurantActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (TextUtils.isEmpty(idEdit.getText().toString())) {
            idEdit.setError("ID is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(nameEdit.getText().toString())) {
            nameEdit.setError("Name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(quanEdit.getText().toString())) {
            quanEdit.setError("Address is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(phoneNumberEdit.getText().toString())) {
            phoneNumberEdit.setError("Phone number is required");
            isValid = false;
        } else {
            String phoneNumber = phoneNumberEdit.getText().toString();
            if (!phoneNumber.matches("\\d{10}")) {
                phoneNumberEdit.setError("Enter a valid 10-digit phone number");
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(item1Edit.getText().toString())) {
            item1Edit.setError("Item 1 is required");
            isValid = false;
        }

        return isValid;
    }


    // Method to show data from the database
    public void showData() {
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEdit.getText().toString();
                if (id.equals(String.valueOf(""))) {
                    idEdit.setError("Please Enter Id");
                    return;
                }
                Cursor cursor = myDB.getData(id);
                String data = null;

                if (cursor.moveToNext()) {
                    data = "ID : " + cursor.getString(0) + "\n" +
                            "NAME : " + cursor.getString(1) + "\n" +
                            "EMAIL : " + cursor.getString(2) + "\n" +
                            "ITEM1 : " + cursor.getString(3) + "\n" +
                            "ITEM2 : " + cursor.getString(4) + "\n" +
                            "ITEM3 : " + cursor.getString(5) + "\n" +
                            "PHONE : " + cursor.getString(6) + "\n";
                    showMessage("DATA", data);
                } else {
                    showMessage("DATA", "There is no Data");
                }
            }
        });
    }

    // Method to update data in the database
    public void updateData() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEdit.getText().toString();

                // Check if ID exists
                Cursor cursor = myDB.getData(id);
                if (cursor.getCount() == 0) {
                    showMessage("Error", "Requested ID is not available to update.");
                    return;
                }

                boolean isUpdated = myDB.updateData(
                        id,
                        nameEdit.getText().toString(),
                        quanEdit.getText().toString(),
                        item1Edit.getText().toString(),
                        item2Edit.getText().toString(),
                        item3Edit.getText().toString(),
                        phoneNumberEdit.getText().toString()
                );
                if (isUpdated) {
                    Toast.makeText(RestaurantActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RestaurantActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }









    // Method to delete data from the database
    public void deleteData() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEdit.getText().toString();
                if (id.equals(String.valueOf(""))) {
                    idEdit.setError("Please Enter Id");
                    return;
                }
                Integer var = myDB.deleteData(id);

                if (var > 0) {
                    Toast.makeText(RestaurantActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RestaurantActivity.this, "Id not available to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showOrderSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Order Successful");
        builder.setMessage("Your order has been placed successfully.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    // Method to get all data from the database
    public void getAllData() {
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDB.getAllData();
                StringBuilder buffer = new StringBuilder();
                if (cursor.getCount() == 0) {
                    showMessage("Data", "Nothing found");
                    return;
                }
                while (cursor.moveToNext()) {
                    buffer.append("ID : ").append(cursor.getString(0)).append("\n");
                    buffer.append("NAME : ").append(cursor.getString(1)).append("\n");
                    buffer.append("ADDRESS : ").append(cursor.getString(2)).append("\n");
                    buffer.append("ITEM1 : ").append(cursor.getString(3)).append("\n");
                    buffer.append("ITEM2 : ").append(cursor.getString(4)).append("\n");
                    buffer.append("ITEM3 : ").append(cursor.getString(5)).append("\n");
                    buffer.append("PHONE : ").append(cursor.getString(6)).append("\n\n");

                }
                showMessage("DATA", buffer.toString());
            }
        });
    }

    // Method to delete all data from the database
    public void deleteAllData() {
        deleteAllButton.setOnClickListener(v -> {
            Integer var = myDB.deleteAllData();
            if (var > 0) {
                Toast.makeText(RestaurantActivity.this, "All Data has been deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RestaurantActivity.this, "Deletion Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to display a message in AlertDialog
    public void showMessage(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }
}
