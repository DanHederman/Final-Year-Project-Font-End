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

public class scanbook extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    /**
     * Class to scan the barcode of a book
     */
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(scanbook.this, "Permission granted", Toast.LENGTH_LONG).show();
            }
            else{
                requestPermission();
            }
        }
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(scanbook.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String []{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Toast.makeText(scanbook.this, "Please scan the book barcode", Toast.LENGTH_LONG).show();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(checkPermission()){
                if(scannerView == null){

                    scannerView = new ZXingScannerView((this));
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else{
                requestPermission();
            }
        }
    }

    public void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        HomeScreenActivity.barcode = (result.getText());
        Toast.makeText(scanbook.this, "Barcode: " + result.getText().toString(), Toast.LENGTH_LONG).show();
        if(HomeScreenActivity.barcode != null){
            Intent AddReviewIntent = new Intent(scanbook.this, AddReview.class);
            scanbook.this.startActivity(AddReviewIntent);
        }
        onBackPressed();
    }
}