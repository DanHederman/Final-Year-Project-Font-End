package com.abc.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddReview extends AppCompatActivity {

    public static JSONArray errors = new JSONArray();
    public int review;
    public String bookISBN;

    private static SeekBar ratingsBar;
    private static TextView seeRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        rate();

        //Set variable for barcode and set barcode variable in HomeScreen to null
        bookISBN = HomeScreenActivity.barcode;
        HomeScreenActivity.barcode = null;
        seeRating.setTextColor(Color.WHITE);

        //Buttons
        Button postReview = findViewById(R.id.postReviewBtn);
        Button homeScreen = findViewById(R.id.homeScreenBtn);

        //Take user back to homescreen
        homeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TakeMeHomeIntent = new Intent(AddReview.this, HomeScreenActivity.class);
                AddReview.this.startActivity(TakeMeHomeIntent);
            }
        });

        //Pass review to server
        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Always run on new thread
                 * connect & pass user login info to the login.php file
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection conn;

                        try {
                            URL url = new URL("http://83.212.126.206/pythonreviewpass.php");
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);

                            review = ratingsBar.getProgress();
                            String r = Integer.toString(review);

                            //Log.w("Check Builder", review);

                            String builder = URLEncoder.encode("param", "UTF-8") + "=" + URLEncoder.encode(MainActivity.Final_user_id) +
                                    "&" + URLEncoder.encode("paramm", "UTF-8") + "=" + URLEncoder.encode(bookISBN) +
                                    "&" + URLEncoder.encode("parammm", "UTF-8") + "=" + URLEncoder.encode(r);
                            Log.w("Check Builder", builder);

                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            writer.write(builder);
                            writer.flush();
                            writer.close();
                            os.close();

                            conn.connect();

                            int responseCode = conn.getResponseCode();
                            StringBuilder response = new StringBuilder();

                            if(responseCode == HttpURLConnection.HTTP_OK) {
                                String line;
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                while((line = reader.readLine()) != null) {
                                    response.append(line).append("\n");
                                }
                            }

                            Log.w("check result", response.toString());

                            JSONObject result = new JSONObject(response.toString());

                            boolean success = false;

                            //If no errors return to home screen, else display errors

                            if(result.has("success") && !result.isNull("success"))
                                success = result.getBoolean("success");

                            if(result.has("errors") && !result.isNull("errors"))
                                errors = result.getJSONArray("errors");

                            Log.w("review success", response.toString());

                            if(success) {

                                Toast.makeText(AddReview.this, "Review added successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AddReview.this, "Error: Internal server issue", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch(Exception e)
                        {
                            Log.e("HomeScreenActivity", e.getLocalizedMessage());
                        }
                        Intent TakeMeHomeIntent = new Intent(AddReview.this, HomeScreenActivity.class);
                        AddReview.this.startActivity(TakeMeHomeIntent);
                    }
                }).start();
            }
        });
    }

    /**
     *  A users rating of a book that has been scanned is gathered from
     *  a seek bar that also displays the rating above it before the
     *  user posts teh rating to the server
     */

    public void rate(){

        ratingsBar = findViewById(R.id.Rate);
        seeRating = findViewById(R.id.seeRatings);

        seeRating.setText("Rating: " + ratingsBar.getProgress() + "/" + ratingsBar.getMax());

        ratingsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int rateprogress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rateprogress = progress;
                seeRating.setText("Rating: " + ratingsBar.getProgress() + "/" + ratingsBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
