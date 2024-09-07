package com.example.ss;

import static android.app.ProgressDialog.show;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class st_reset extends AppCompatActivity {

    TextView username;
    EditText pass, repass;
    Button confirm;
    stDBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.st_reset);

        username = (TextView) findViewById(R.id.username_reset_text);
        pass = (EditText) findViewById(R.id.password_reset);
        repass = (EditText) findViewById(R.id.repassword_reset);
        confirm = (Button) findViewById(R.id.btnconfirm);
        DB = new stDBHelper(this);

        Intent intent = getIntent();
        username.setText(intent.getStringExtra ("username"));

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String password = pass.getText().toString();
                String repassword = repass.getText().toString();
                if (password.equals(repassword))
                {
                    Boolean checkpasswordupdate = DB.updatepassword(user, password);
                    if (checkpasswordupdate == true) {
                        Intent intent = new Intent(getApplicationContext(), fragment_home.class);
                        startActivity(intent);
                        Toast.makeText(st_reset.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(st_reset.this, "Password not updated successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(st_reset.this, "Passwords not matching", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
