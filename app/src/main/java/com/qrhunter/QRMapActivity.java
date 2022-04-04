package com.qrhunter;

import static com.qrhunter.MainActivity.collectables;
import static com.qrhunter.MainActivity.toast;
import static java.lang.Math.abs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qrhunter.databinding.ActivityQrmapBinding;

/**
 * The QRMapActivity shows the user a map centered at their current location, and highlights
 * nearby QR Codes.
 */
public class QRMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityQrmapBinding binding;
    private Geolocation player_location;


    /**
     * Attempts to get the players current location.
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
        if (locationGPS != null) {
            player_location = new Geolocation(locationGPS.getLongitude(), locationGPS.getLatitude());
            populateMap();
        }
        else toast(getApplicationContext(), "Unable to find location.");
    }


    /**
     * Populates the map with QR codes that are within 0.01 Lng and Lat from the current position.
     */
    private void populateMap() {

        // Move the camera to the right position.
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(player_location.getLatitude(), player_location.getLongitude()), 15.0f));

        // Go through each collectable, and if they're within range, add a pin.
        for (Collectable scanned : collectables.getDatabase().values()) {
            Geolocation current = scanned.getLocation();
            if (abs(player_location.getLongitude() - current.getLongitude()) < 0.01 && abs(player_location.getLongitude() - current.getLongitude()) < 0.01) {
                map.addMarker(new MarkerOptions().position(new LatLng(current.getLatitude(), current.getLongitude())).title(scanned.getName()));
            }
        }
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQrmapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }


    @Override public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        storeLocation();
    }


    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {

            // If the user refuses location permission, we can't do anything so exit the activity.
            if (permissions[x].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && requestCode == 1) {
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) storeLocation();
                else {
                    toast(getApplicationContext(), "Location permission is required to use QR Map");
                    finish();
                }
            }
        }
    }
}
