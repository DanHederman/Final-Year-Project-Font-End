package com.abc.work;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etUname = findViewById(R.id.etUname);

        final Button btnLogin = findViewById(R.id.btnLogin);

        final Button btnRegisterHere = findViewById(R.id.btnRegisterHere);

        btnRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                // finish(); // If you wanna finish the activity
                startActivity(registerIntent);
            }
        });
    }
}
