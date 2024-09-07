package com.example.ss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class activity_password extends AppCompatActivity {
    EditText username;
    Button reset;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);

        username = (EditText) findViewById(R.id.username_reset);
        reset = (Button) findViewById(R.id.btnreset);
        DB = new DBHelper(this);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();

                // Check if username is empty
                if (user.equals("")) {
                    Toast.makeText(activity_password.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if username exists in the database
                Boolean checkuser = DB.checkusername(user);
                if (checkuser) {
                    // Username exists, proceed to reset password
                    Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                } else {
                    // Username does not exist
                    Toast.makeText(activity_password.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}