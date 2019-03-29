package com.abc.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class HomeScreenActivity extends AppCompatActivity {

    public static String barcode;
    public static String noerrors1;
    private Handler handler;

    public final StringBuilder noerrorsString = new StringBuilder();
    public final StringBuilder errorsString = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button scannerbtn = findViewById(R.id.scannerbtn);
        Button cb = findViewById(R.id.crap);
        Button rec = findViewById(R.id.recbtn);

        scannerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner();
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkcode();
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
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
                            //83.212.127.188
                            URL url = new URL("http://83.212.126.206/pythonpass.php");
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);

                            String builder = URLEncoder.encode("param", "UTF-8") + "=" + URLEncoder.encode(MainActivity.Final_user_id);
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
                            JSONArray errors = new JSONArray();
                            JSONArray noerrors = new JSONArray();
                            boolean success = false;

                            //If no errors return to home screen, else display errors

                            if(result.has("success") && !result.isNull("success"))
                                success = result.getBoolean("success");

                            if(result.has("errors") && !result.isNull("errors"))
                                errors = result.getJSONArray("errors");

                            if(result.has("noerrors") && !result.isNull("noerrors"))
                                noerrors = result.getJSONArray("noerrors");

                            if(success) {
                                final StringBuilder noerrorsString = new StringBuilder();
                                if(noerrors.length() > 0){
                                    for(int i = 0; i < noerrors.length(); ++i) {
                                        noerrorsString.append(noerrors.getString(i)).append("\n");
                                    }
                                }
                                Log.w("Rec", noerrorsString.toString());
                                noerrors1 = noerrors.toString();
                                //noerrors1 = noerrors.toString().substring(0, noerrors.length() - 29);
                                Log.w("This is recommendations", noerrors.toString());
                                Log.w("This recommendations1", noerrors1);

                                Intent DisplayRecIntent = new Intent(HomeScreenActivity.this, DisplayRecommendations.class);
                                HomeScreenActivity.this.startActivity(DisplayRecIntent);

//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //Toast.makeText(RegisterActivity.this, errorsString.toString(), Toast.LENGTH_LONG);
//
//                                new AlertDialog.Builder(HomeScreenActivity.this)
//                                        .setTitle("Recommendations")
//                                        .setMessage(noerrorsString.toString())
//                                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        }).create()
//                                        .show();
//                            }
//                        });

                            } else {
                                final StringBuilder errorsString = new StringBuilder();
                                if(errors.length() > 0) {
                                    for(int i = 0; i < errors.length(); ++i) {
                                        errorsString.append(errors.getString(i)).append("\n");
                                    }
                                }

                                // Always handle UI on Main thread
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(RegisterActivity.this, errorsString.toString(), Toast.LENGTH_LONG);

                                        new AlertDialog.Builder(HomeScreenActivity.this)
                                                .setTitle("Register Failed")
                                                .setMessage(errorsString.toString())
                                                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).create()
                                                .show();
                                    }
                                });
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

    public static void getRec(){

    }

    public void openScanner(){
        Intent intent = new Intent( this, scanbook.class);
        startActivity(intent);
    }

    public void checkcode(){
        Log.d("barcode = ", barcode);
        //Log.d("recommended = ", noerrorsString.toString());
    }
}
