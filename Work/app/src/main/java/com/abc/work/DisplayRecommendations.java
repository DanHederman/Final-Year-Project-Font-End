package com.abc.work;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayRecommendations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recommendations);

        TextView recommendations = findViewById(R.id.viewRec);

        recommendations.setText(HomeScreenActivity.noerrors1);
    }
}
