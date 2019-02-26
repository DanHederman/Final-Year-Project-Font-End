package com.abc.work;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class RegisterActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         // Handle UI stuff on main thread
        handler = new Handler(Looper.getMainLooper());


        final EditText etAge = findViewById(R.id.etAge);
        final EditText etUname = findViewById(R.id.etUname);
        final EditText etName = findViewById(R.id.etName);
        final EditText etPassword = findViewById(R.id.etPassword);

        final Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String name = etName.length() > 0 ? etName.getText().toString() : "";
                final String username = etUname.length() > 0 ? etUname.getText().toString() : "";
                final String password = etPassword.length() > 0? etPassword.getText().toString() : "";
                final String age = etAge.length() > 0? etAge.getText().toString() : "0";

                // Do Request on Separate thread
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        HttpURLConnection conn;

                        try
                        {
                            URL url = new URL("http://83.212.127.188/Register.php");
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);

                            int a;
                            try
                            {
                                a = Integer.parseInt(age);
                            }
                            catch(Exception e)
                            {
                                a = 0;
                            }

                            String builder = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name) +
                                    "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username) +
                                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password) +
                                    "&" + URLEncoder.encode("age", "UTF-8") + "=" + a;

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

                            JSONObject result = new JSONObject(response.toString());

                            boolean success = false;

                            //If no errors return to home screen, else display errors

                            if(result.has("success") && !result.isNull("success"))
                                success = result.getBoolean("success");
                            JSONArray errors = new JSONArray();

                            if(result.has("errors") && !result.isNull("errors"))
                                errors = result.getJSONArray("errors");

                            if(success) {
                                // finishAffinity(); // Close all open activities
                                finish();
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

                                        new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("Register Failed")
                                                .setMessage(errorsString.toString())
                                                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        if(errorsString.toString() == "Please fill in a name"){
                                                            etName.requestFocus();
                                                        }
                                                        if(errorsString.toString() == "Please use a positive integer"){
                                                            etAge.requestFocus();
                                                        }

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