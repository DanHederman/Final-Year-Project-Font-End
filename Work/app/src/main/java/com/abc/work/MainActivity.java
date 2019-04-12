package com.abc.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    /**
     * Class to login or allow user to register
     */
    private Handler handler;
    public static String Final_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler((Looper.getMainLooper()));

        //Fields for login
        final EditText etUnameLog= findViewById(R.id.etUnameLog);
        final EditText etPasswordLog = findViewById(R.id.etPasswordLog);

        //Buttons
        final Button btnLogin = findViewById(R.id.btnLogin);
        final Button btnRegisterHere = findViewById(R.id.btnRegisterHere);

        /**
         * Open register user activity
         */
        btnRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                // finish(); // If you wanna finish the activity
                startActivity(registerIntent);
            }
        });

        /**
         * Log user in
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String username = etUnameLog.getText().toString();
                final String password = etPasswordLog.getText().toString();
                //Log.w("Username", String.valueOf(etLogUsername));

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
                            URL url = new URL("http://83.212.126.206/Login.php");
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);

                            String builder = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username) +
                                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password);
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

                            JSONObject result = null;
                            result = new JSONObject(response.toString());

                            boolean success = false;

                            /**
                             * If no errors, log the user in, if errors occur, display to user
                             */

                            Log.w("errors check", result.toString());

                            if(result.has("success") && !result.isNull("success"))
                                success = result.getBoolean("success");
                            JSONArray errors = new JSONArray();

                            if(result.has("errors") && !result.isNull("errors"))
                                errors = result.getJSONArray("errors");

                            if(success) {
                                // finishAffinity(); // Close all open activities
                                Final_user_id = result.getString("user_id");
                                Log.d("user id: ", Final_user_id);
                                Intent loginIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
                                MainActivity.this.startActivity(loginIntent);
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

                                        new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Login Failed")
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
                            Log.e("RegisterActivity", e.getLocalizedMessage());
                        }
                    }
                }).start();
            }
        });
    }
}