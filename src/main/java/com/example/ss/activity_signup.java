package com.example.ss;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class activity_signup extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup;
    TextView       signin;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (TextView) findViewById(R.id.btnsignin);
        ToggleButton togglePassword = findViewById(R.id.togglePassword);

        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                // Check if username or password fields are empty
                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(activity_signup.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if password length is at least 8 characters
                    if (pass.length() < 8) {
                        Toast.makeText(activity_signup.this, "Password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    } else {
                        // Check if passwords match
                        if (pass.equals(repass)) {
                            // Check if username already exists
                            Boolean checkuser = DB.checkusername(user);
                            if (!checkuser) {
                                // Insert new user data into the database
                                Boolean insert = DB.insertData(user, pass);
                                if (insert) {
                                    Toast.makeText(activity_signup.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(activity_signup.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity_signup.this, "Username already exists! Please pick another one", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity_signup.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),activity_login.class);
                startActivity(intent);
            }
        });

        togglePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                password.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // Hide password
                password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Move cursor to the end of the text
            password.setSelection(password.getText().length());
        });


    }
}
