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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    DecoratedBarcodeView scanner;
    Collectable scanned;
    FirebaseFirestore database;
    FirebaseStorage storage;
    MessageDigest digest;

    /*
     * Taking a Photo when a QR is scanned is an intent, which means onResume will be called when
     * the activity returns, this would resume the scanner while the popup is still open.
     *
     * When assembleScanned is called, this is turned on, and the boolean is turned back off
     * when the Collectable has been uploaded.
     */
    boolean building = false;


    // Register for the callback when the camera activity returns.
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
        else MainActivity.toast(getApplicationContext(), "Unable to find location.");
        upload();
    }


    /**
     * Assembles a scanned QR code, waiting for the user to enter photo/geolocation, then uploading
     * it.
     */
    private void assembleScanned() {

        // Prevent the scanner from turning back on when returning from the CAPTURE intent.
        building = true;

        // Create the popup.
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View context_view = layoutInflater.inflate(R.layout.context_scanned, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        // When the dialog is canceled (IE clicked off of it), save our information.
        alertDialogBuilder.setOnCancelListener(dialog -> {
            if (((CheckBox)context_view.findViewById(R.id.context_scanned_save_location)).isChecked())
                storeLocation();
            else upload();
        });

        // When we hit add picture, spawn a camera instance and get the BitMap taken.
        context_view.findViewById(R.id.context_scanned_add_picture).setOnClickListener(v -> {
            cameraResult.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        });

        // Create and show the dialog.
        alertDialogBuilder.setView(context_view);
        alertDialogBuilder.create().show();
    }


    /**
     * Uploads the Collectable, and resumes the scanner.
     *
     * This function helps for avoiding async issues with location permission when creating the
     * Collectable.
     */
    private void upload()  {

        // Our hashmap to upload basic information.
        HashMap<String, Object> pack = new HashMap<>();
        HashMap<String, Object> comment_stub = new HashMap<>();

        // Photos are special, we use Storage rather than Firestore
        if (scanned.getPhoto() != null) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // TODO We can change the quality attribute to solve US 09.01.01
            scanned.getPhoto().compress(Bitmap.CompressFormat.JPEG, 100, output);

            // Write it. Since its detached from the main base, use the ID to associate it.
            storage.getReference(scanned.getId())
                    .putBytes(output.toByteArray())
                    .addOnFailureListener(e -> MainActivity.toast(getApplicationContext(), "Network Error: " + e.getMessage()));
        }

        // Pack all the rest of the information into the hashmap.
        pack.put("Score", scanned.getScore());
        pack.put("Location", scanned.getLocation());

        // Upload our pack into the Firestore.
        database.collection("Scanned")
                .document(scanned.getId())
                .set(pack)
                .addOnFailureListener(e -> MainActivity.toast(getApplicationContext(), "Network Error: " + e.getMessage()));

        /*
         * Because the comments are going to be a collection of String, String entries,
         * we cannot embed it directly within the structure of the Firebase (Still in the class,
         * though).
         *
         * However, when a new item is scanned, it isn't going to have any comments, but
         * apparently you can't make a blank document, as Firebase will delete it. Therefore,
         * we'll just make a stub.
         */
        comment_stub.put("exists", true);
        database.collection("Comments")
                .document(scanned.getId())
                .set(comment_stub)
                .addOnFailureListener(e -> MainActivity.toast(getApplicationContext(), "Network Error: " + e.getMessage()));

        // Resume the scanner.
        scanner.resume();
        building = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        try {digest = MessageDigest.getInstance("SHA-256");}
        catch (NoSuchAlgorithmException e) {MainActivity.toast(getApplicationContext(), "Cannot find hashing instance!");}



        // Grab the scanner within the activity.
        scanner = findViewById(R.id.home_scanner);
        scanner.decodeContinuous(new BarcodeCallback() {

            // When we successfully scan a code.
            @Override public void barcodeResult(BarcodeResult result) {
                scanned = new Collectable();

                /*
                 * We need to transform the ID so it doesn't contains '/', so I've just hashed it
                 * so that it will always be unique. I would assume, once the scoring system
                 * is implemented, we can move this work onto those functions.
                 */
                byte[] hash = digest.digest(result.getText().getBytes(StandardCharsets.UTF_8));
                scanned.setId(new BigInteger(1, hash).toString(16));

                // TODO Store the score of the code.

                // Pause the scanner so it doesn't make an infinite amount of popups.
                scanner.pause();

                // Check if the Code already exists within the Firebase, prompt accordingly.
                database.collection("Scanned").document(scanned.getId()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        // Not sure why this happens, but we need to check for it.
                        if (document == null) MainActivity.toast(getApplicationContext(), "Unknown error");
                        else {
                            // If it exists, we shouldn't overwrite.
                            if (document.exists()) {
                                MainActivity.toast(getApplicationContext(), "Already been scanned!");
                                scanner.resume();
                            }
                            else assembleScanned();
                        }
                    }
                    else MainActivity.toast(getApplicationContext(), "Network Error: " + task.getException());
                });
            }
            @Override public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });
    }

    @Override protected void onResume() {
        super.onResume();
        if (!scanner.isActivated() && !building)
            scanner.resume();
    }


    @Override protected void onPause() {super.onPause(); scanner.pause();}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {

            if (permissions[x].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && requestCode == 1) {
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) storeLocation();
                else {
                    MainActivity.toast(getApplicationContext(), "Location permission is required to save location!");
                    upload();
                }
            }
        }

    }
}