package com.qrhunter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        scanner = findViewById(R.id.main_scanner);
        scanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {

                // Pause the scanner so it doesn't make an infinite amount of popups.
                scanner.pause();

                // Load the context menu.
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View context_view = layoutInflater.inflate(R.layout.context_scanned, null);

                // Build the dialog.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                // On dismiss/cancel, we need to re-enable the scanner.
                alertDialogBuilder.setOnCancelListener(dialogInterface -> scanner.resume());

                alertDialogBuilder.setView(context_view);
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
            @Override public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });

        // Set up button to go to Search Menu activity
        Button scoreboardButton = findViewById(R.id.main_scoreboard);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchMenuActivity.class);
                startActivity(intent);
            }
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