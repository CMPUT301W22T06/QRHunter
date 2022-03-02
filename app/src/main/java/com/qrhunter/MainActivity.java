package com.qrhunter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * We need access to the camera if we want to scan barcodes. If the user declines to
         * accept the permission, request again and send a Toast to let them know that they need
         * to enable the permission. This will also toast when first requesting the permission,
         * which can let the user know why the permission is needed.
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "This program requires access to the camera to scan QR Codes!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }

        /*
         * The results from the camera activity. We use this to grab the BitMap and store it into
         * the Collectable.
         */
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
            @Override
            public void barcodeResult(BarcodeResult result) {

                // Create/Overwrite the collectable.
                scanned = new Collectable();

                // Pause the scanner so it doesn't make an infinite amount of popups.
                scanner.pause();

                // Create the popup.
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View context_view = layoutInflater.inflate(R.layout.context_scanned, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setOnCancelListener(
                        // Upload the Collectable to the database.
                        dialogInterface -> scanner.resume()
                );

                alertDialogBuilder.setView(context_view);
                AlertDialog alert = alertDialogBuilder.create();

                // When we hit add picture, spawn a camera instance and get the BitMap taken.
                context_view.findViewById(R.id.context_scanned_add_picture).setOnClickListener(v -> {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraResult.launch(cameraIntent);
                });

                // Set the ID (and add the score later on)
                scanned.setId(result.getText());
                alert.show();
            }
            @Override public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!scanner.isActivated())
            scanner.resume();
    }

    @Override
    protected void onPause() {super.onPause(); scanner.pause();}
}