package com.abc.work;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private static String URL_LOGIN = "http://83.212.126.190/Login.php";

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler((Looper.getMainLooper()));

        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etUname = findViewById(R.id.etUname);

        final Button btnLogin = findViewById(R.id.btnLogin);

        Button scannerbtn = findViewById(R.id.Scanbtn);

        final Button btnRegisterHere = findViewById(R.id.btnRegisterHere);

        btnRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                // finish(); // If you wanna finish the activity
                startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String Username = etUname.length() < 0 ? etUname.getText().toString() : "";
                final String Password = etPassword.length() < 0 ? etPassword.getText().toString(): "";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection conn;

                        try{

                            URL url = new URL("http://83.212.126.206/Login.php");
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);

                            String builder = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(Username) +
                                    "&" + URLEncoder.encode("password", "UTF-8") + "=" URLEncoder.encode(Password);

                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            writer.write((builder));
                            writer.flush();
                            writer.close();
                            os.close();

                            conn.connect();

                            int responseCode = conn.getResponseCode();

                            StringBuilder response = new StringBuilder();

                            if(responseCode == HttpURLConnection.HTTP_OK){

                                String line;
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                while((line = reader.readLine()) != null){
                                    response.append(line).append("\n");
                                }
                            }

                            JSONObject result = new JSONObject(response.toString());
                            boolean success = false;

                            //If no errors, log the user in

                            if(result.has("success") && !result.isNull("success")){
                                success = result.getBoolean("success");
                            }
                            JSONArray errors = new JSONArray();

                            if(result.has("errors") && !result.isNull("errors")){
                                errors = result.getJSONArray("errors");
                            }

                            if(success){
                                finish();
                            }
                            else{
                                final StringBuilder errorsString = new StringBuilder();
                            }
                        }
                        catch(Exception e){
                            Log.e("LoginActivity", e.getLocalizedMessage());
                        }
                    }
                })
            }
        });

        scannerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanner();
            }
        });

    }

    public void openScanner(){

        Intent intent = new Intent(this, Scanner.class);
        startActivity(intent);
    }


}
