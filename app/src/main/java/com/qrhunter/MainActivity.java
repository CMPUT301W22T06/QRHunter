package com.qrhunter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DecoratedBarcodeView scanner;
    Collectable scanned;


    /**
     * Stores the location within Collectable, as long as permissions are granted.
     *
     * This function checks for permission and handles it to save location data within the scanned
     * Collectable, calling upload once the data has been stored. Because storeLocation uploads
     * the Collectable, location must be the LAST piece of data given to the Collectable before
     * its uploaded.
     */
    private void storeLocation() {

        // If permission is not granted, we need to ask and then leave
        // onRequestPermissionsResult will handle uploading after the user select a option.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // If permission is already granted, append the location information and upload.
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null)
                scanned.setLocation(new Pair<>(locationGPS.getLatitude(), locationGPS.getLongitude()));
            else
                Toast.makeText(getApplicationContext(), "Unable to find location.", Toast.LENGTH_SHORT).show();
        upload();
    }

    /**
     * Uploads the Collectable, and resumes the scanner.
     *
     * This function helps for avoiding async issues with location permission when creating the
     * Collectable.
     */
    private void upload() {
        // TODO Upload the Collectable to the database.
        scanner.resume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request Camera Permission (Needed for the scanner)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        // Register for the callback when the camera activity returns (It has to be here.)
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            camera_result -> {
                // Results other than OK we can just ignore, the photo already defaults to null.
                if (camera_result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = camera_result.getData();
                    assert(data != null);
                    scanned.setPhoto((Bitmap)data.getExtras().get("data"));
                }
            }
        );

        // Grab the scanner within the activity.
        scanner = findViewById(R.id.main_scanner);
        scanner.decodeContinuous(new BarcodeCallback() {

            // When we successfully scan a code.
            @Override public void barcodeResult(BarcodeResult result) {
                scanned = new Collectable();
                scanned.setId(result.getText());
                // TODO Store the score of the code.

                // Pause the scanner so it doesn't make an infinite amount of popups.
                scanner.pause();

                // Create the popup.
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View context_view = layoutInflater.inflate(R.layout.context_scanned, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                // When the dialog is canceled (IE clicked off of it), save our information.
                alertDialogBuilder.setOnCancelListener(dialog -> {

                    // Geolocation is optional, so make sure the checkbox was explicitly checked before adding it.
                    if (((CheckBox)context_view.findViewById(R.id.context_scanned_save_location)).isChecked())
                        storeLocation();
                    else {
                        upload();
                    }
                });

                // When we hit add picture, spawn a camera instance and get the BitMap taken.
                context_view.findViewById(R.id.context_scanned_add_picture).setOnClickListener(v -> {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraResult.launch(cameraIntent);
                });

                // Create and show the dialog.
                alertDialogBuilder.setView(context_view);
                alertDialogBuilder.create().show();
            }
            @Override public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });
    }

    @Override protected void onResume() {super.onResume(); if (!scanner.isActivated()) scanner.resume();}

    @Override protected void onPause() {super.onPause(); scanner.pause();}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {

            // Camera isn't a big deal, just toast them to let them know it's kinda important.
            if (permissions[x].equals(Manifest.permission.CAMERA) && requestCode == 0 && grantResults[x] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "The camera is needed to scan QR codes!", Toast.LENGTH_SHORT).show();
            }

            // If the user grants location, we should add that location to the Collectable.
            // ! NOTE: This assumes that the only time this permission is checked is when the checkbox is selected in the popup!
            else if (permissions[x].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && requestCode == 1) {
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) storeLocation();
                else {
                    Toast.makeText(getApplicationContext(), "Location permission is required to save location!", Toast.LENGTH_SHORT).show();
                    upload();
                }
            }
        }

    }
}