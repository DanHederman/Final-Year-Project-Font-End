package com.abc.work;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayRecommendations extends AppCompatActivity {

    public String r;

    /**
     * Display recommendations to user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recommendations);

        TextView recommendations = findViewById(R.id.viewRec);

//        for(int i = 0; i <= 8; i++) {
//            myTextView.append(RecArray[i]);
//        }

        r = HomeScreenActivity.noerrorsString.toString();

        String rec [] = r.split("\\n");

        recommendations.setText(r);

        recommendations.setTextColor(Color.WHITE);

        Button homeBtn = findViewById(R.id.homeScreenBtn);

        String RecArray [] = HomeScreenActivity.noerrorsString.toString().split("\n");

        Log.w("Array", RecArray[0]);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TakeMeHomeIntent = new Intent(DisplayRecommendations.this, HomeScreenActivity.class);
                DisplayRecommendations.this.startActivity(TakeMeHomeIntent);
            }
        });
    }
}