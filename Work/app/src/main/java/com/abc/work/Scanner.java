package com.abc.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    /**
     * Instanciates the scanner
     * @param savedInstanceState resumes the scanner state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {

                Toast.makeText(Scanner.this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
    }

    /**
     *
     * @return The permissions of the app
     */

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(Scanner.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Method to request permissions for the app
     */

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    /**
     * Method to start the scanner
     * @param requestCode Takes in requests
     * @param permission Takes in the permissions
     * @param grantResults Tskes in the grant results
     */


    /**
     * Method to stop the app asking permission on every startup of the app
     */

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermission()) {
                if (scannerView == null) {

                    scannerView = new ZXingScannerView((this));
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    /**
     * Method to stop the camera when the app is closed
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    /**
     * Method to display app Message Alerts to  the user
     * @param message type of the message
     * @param listener the actual message
     */

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(Scanner.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", listener)
                .create()
                .show();

    }

    /**
     * Method to handle the actions of the user
     * @param result passes in the result to be decided upon
     */

    @Override
    public void handleResult(Result result) {

        final String scanResult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Scan result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scannerView.resumeCameraPreview(Scanner.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();

        Toast.makeText(Scanner.this, "Code:" + scanResult, Toast.LENGTH_LONG).show();
    }
}