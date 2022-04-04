package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;
import static com.qrhunter.MainActivity.collectables;
import static com.qrhunter.MainActivity.toast;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

/**
 * The HomeActivity is the primary activity after the user has logged in, and allows the user
 * to view their profile, the QR Map, and the scoreboard..
 */
public class HomeActivity extends AppCompatActivity {
    DecoratedBarcodeView scanner;
    Collectable scanned;
    Player player;


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
        @SuppressLint("MissingPermission") Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null)
            scanned.setLocation(new Geolocation(locationGPS.getLatitude(), locationGPS.getLongitude()));
        else toast(getApplicationContext(), "Unable to find location.");
        collectables.add(scanned, this);
        allPlayers.addClaimedID(player.getUsername(), scanned.getId());
    }


    /**
     * Assembles a scanned QR code, waiting for the user to enter photo/geolocation, then uploading
     * it.
     */
    @SuppressLint("SetTextI18n") private void assembleScanned() {
        // Create the popup.
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View context_view = layoutInflater.inflate(R.layout.context_scanned, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        TextView name_hint = context_view.findViewById(R.id.context_scanned_name_helper);
        name_hint.setText("Name:");

        // When the dialog is canceled (IE clicked off of it), save our information.
        alertDialogBuilder.setOnCancelListener(dialog -> {
            if (((CheckBox)context_view.findViewById(R.id.context_scanned_save_location)).isChecked())
                storeLocation();

            EditText name_box = context_view.findViewById(R.id.context_scanned_name);
            String name = name_box.getText().toString();

            if (name.isEmpty()) {
                toast(getApplicationContext(), "Please enter a name!");
                assembleScanned();
            }
            else if (name.length() > 24) {
                toast(getApplicationContext(), "Name too large! Must be 24 characters.");
                assembleScanned();
            }
            else {
                scanned.setName(name);
                collectables.add(scanned, this);
                allPlayers.addClaimedID(player.getUsername(), scanned.getId());
            }
        });

        // When we hit add picture, spawn a camera instance and get the BitMap taken.
        context_view.findViewById(R.id.context_scanned_add_picture).setOnClickListener(v -> cameraResult.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        ((TextView)context_view.findViewById(R.id.context_scanned_score)).setText("Score of Scanned QR: " + scanned.getScore());

        // Create and show the dialog.
        alertDialogBuilder.setView(context_view);
        alertDialogBuilder.create().show();
    }


    /**
     * Resumes the activity (usually a callback from collectable uploading)
     * @param error An associated error, if there was one.
     */
    public void resume(String error) {
        if (!error.isEmpty())
            toast(getApplicationContext(), "Could not upload QR Code: " + error);

        // Resume the scanner.
        scanner.resume();
        building = false;
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the toolbar.
        Toolbar toolbar = findViewById(R.id.player_menu);
        setSupportActionBar(toolbar);

        // retrieves the Player username from the intent
        String username = getIntent().getStringExtra("username");
        player = allPlayers.getPlayer(username);

        // Access the scoreboard.
        Button scoreboardButton = findViewById(R.id.home_scoreboard);
        scoreboardButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SearchMenuActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        });

        // Access the QR Map.
        findViewById(R.id.home_map).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, QRMapActivity.class);
            startActivity(intent);
        });

        // Grab the scanner within the activity.
        scanner = findViewById(R.id.home_scanner);
        if (player == null) toast(this, "start scanner after login!");
        else {
            scanner.decodeContinuous(new BarcodeCallback() {

                // When we successfully scan a code.
                @Override public void barcodeResult(BarcodeResult result) {

                    // Turn the building flag on so the scanner doesn't resume.
                    building = true;
                    scanned = new Collectable();

                    // Create the ID, along with the score.
                    String id = ScoringSystem.hashQR(result.getText());
                    scanned.setId(id);
                    scanned.setScore(ScoringSystem.score(id));

                    // Pause the scanner so it doesn't make an infinite amount of popups.
                    scanner.pause();

                    // Check if the Code already exists within the Firebase, prompt accordingly.
                    collectables.getStore().collection("Scanned").document(scanned.getId()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            // Not sure why this happens, but we need to check for it.
                            if (document == null)
                                toast(getApplicationContext(), "Unknown error");
                            else {
                                // If it exists, we shouldn't overwrite.
                                if (player.getClaimedCollectibleIDs().contains(id)) {
                                    toast(getApplicationContext(), "Already been scanned!");
                                    resume("");
                                }
                                else if (collectables.exists(id)) {
                                    allPlayers.addClaimedID(player.getUsername(), id);
                                    toast(getApplicationContext(), "Adding " + collectables.get(id).getName());
                                    resume("");
                                }
                                else assembleScanned();
                            }
                        }

                        // This happens when the database is empty.
                        else assembleScanned();
                    });
                }
                @Override public void possibleResultPoints(List<ResultPoint> resultPoints) {}
            });
        }
    }


    /**
     * Setups the UserActivity after the user clicks the hot-dog menu on the Toolbar.
     * @param mi The MenuItem
     */
    public void onClickUser(MenuItem mi) {

        // If we're building a collectable, don't let them leave.
        if (building) {
            toast(getApplicationContext(), "Uploading... please wait.");
            return;
        }

        // If the player has yet to login, we can't let them go to the activity.
        if (player!=null) {
            Intent intent = new Intent(HomeActivity.this, UserActivity.class);
            intent.putExtra("username", player.getUsername());
            intent.putExtra("restricted", false);
            startActivity(intent);
        }

        else toast(this, "Please login first");
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override protected void onResume() {
        super.onResume();
        if (!scanner.isActivated() && !building)
            scanner.resume();
    }


    @Override protected void onPause() {super.onPause(); scanner.pause();}


    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {

            // Check if the permission was Location, upload the collectable.
            if (permissions[x].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && requestCode == 1) {
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) storeLocation();
                else {
                    toast(getApplicationContext(), "Location permission is required to save location!");
                    collectables.add(scanned, this);
                }
            }
        }
    }
}