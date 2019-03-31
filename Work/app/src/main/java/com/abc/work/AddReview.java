package com.abc.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class AddReview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static JSONArray errors = new JSONArray();
    public String review;
    public String bookISBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        //Set variable for barcode and set barcode variable in HomeScreen to null
        bookISBN = HomeScreenActivity.barcode;
        HomeScreenActivity.barcode = null;

        //Buttons
        Button postReview = findViewById(R.id.postReviewBtn);
        Button homeScreen = findViewById(R.id.homeScreenBtn);

        //Spinner to allow user to select rating for book
        Spinner reviewSpinner = findViewById(R.id.reviewSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewSpinner.setAdapter(adapter);
        reviewSpinner.setOnItemSelectedListener(this);

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

                            String builder = URLEncoder.encode("param", "UTF-8") + "=" + URLEncoder.encode(MainActivity.Final_user_id) +
                                    "&" + URLEncoder.encode("paramm", "UTF-8") + "=" + URLEncoder.encode(bookISBN) +
                                    "&" + URLEncoder.encode("parammm", "UTF-8") + "=" + URLEncoder.encode(review);
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
                    }
                }).start();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        review = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
