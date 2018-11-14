package com.abc.work;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etUname = findViewById(R.id.etUname);
        final  EditText etAge = findViewById(R.id.etAge);
        final TextView welcomeMessage = findViewById(R.id.tvWelcomeMessage);

    }
}
