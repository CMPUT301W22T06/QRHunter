package com.qrhunter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {


    /**
     * Creates a short toast, saving some code redundancy
     * @param context The context to display the toast.
     * @param message The message to toast.
     */
    static public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request Camera Permission (Needed for the scanner)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        // Temporary.
        findViewById(R.id.main_home_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {

            // Camera isn't a big deal, just toast them to let them know it's kinda important.
            if (permissions[x].equals(Manifest.permission.CAMERA) && requestCode == 0 && grantResults[x] != PackageManager.PERMISSION_GRANTED) {
                toast(getApplicationContext(), "The camera is needed to scan QR codes!");
            }
        }

    }
}