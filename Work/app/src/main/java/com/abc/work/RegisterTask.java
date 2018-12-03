package com.abc.work;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class RegisterTask extends AsyncTask<String, String, Void> {

    private static final String URL = "http://83.212.126.206/Register.php";
/*
    public RegisterTask() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
}

    @Override
    protected Void doInBackground(String... params) {
        String name = params[0];
        String username = params[1];
        String password = params[2];
        int age = Integer.parseInt(params[3]);

        OutputStream out = null;

        try
        {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            out = new BufferedOutputStream(conn.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(name);
            writer.flush();
            writer.close();
            out.close();

            conn.connect();
        }
        catch(Exception e)
        {
            Log.e("RegisterTask", e.getLocalizedMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }
    */
}
