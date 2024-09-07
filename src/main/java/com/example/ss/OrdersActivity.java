package com.example.ss;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;




import androidx.appcompat.app.AppCompatActivity;




public class OrdersActivity extends AppCompatActivity {

    private ResDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        myDB = new ResDatabaseHelper(this);
        displayData();
    }

    private void displayData() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        Cursor cursor = myDB.getAllData();
        if (cursor.getCount() == 0) {
            // No data available
            return;
        }

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            String item1 = cursor.getString(3);
            String item2 = cursor.getString(4);
            String item3 = cursor.getString(5);
            String phoneNumber = cursor.getString(6);

            TableRow tableRow = new TableRow(this);
            TextView textViewId = new TextView(this);
            TextView textViewName = new TextView(this);
            TextView textViewAddress = new TextView(this);
            TextView textViewItems = new TextView(this);
            TextView textViewPhone = new TextView(this);

            textViewId.setGravity(Gravity.CENTER);
            textViewName.setGravity(Gravity.CENTER);
            textViewAddress.setGravity(Gravity.CENTER);
            textViewItems.setGravity(Gravity.CENTER);
            textViewPhone.setGravity(Gravity.CENTER);

            textViewId.setText(id);
            textViewName.setText(name);
            textViewAddress.setText(address);
            textViewItems.setText(item1 + "\n" + item2 + "\n" + item3 + "\n");
            textViewPhone.setText(phoneNumber); // Corrected line

            tableRow.addView(textViewId);
            tableRow.addView(textViewName);
            tableRow.addView(textViewAddress);
            tableRow.addView(textViewItems);
            tableRow.addView(textViewPhone); // Added line

            tableLayout.addView(tableRow);
        }
    }
}

