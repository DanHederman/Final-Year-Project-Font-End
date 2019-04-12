package com.abc.work;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayRecommendations extends AppCompatActivity {

    /**
     * Display recommendations to user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recommendations);

        TextView recommendations = findViewById(R.id.viewRec);

        recommendations.invalidate();

        //Get recommendations from variable in HomeScreenActivity
        recommendations.setText(HomeScreenActivity.noerrorsString);

        recommendations.setTextColor(Color.WHITE);

        Button homeBtn = findViewById(R.id.homeScreenBtn);

        //Take user back to Home Screen
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TakeMeHomeIntent = new Intent(DisplayRecommendations.this, HomeScreenActivity.class);
                DisplayRecommendations.this.startActivity(TakeMeHomeIntent);
            }
        });
    }
}