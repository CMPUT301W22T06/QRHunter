package com.qrhunter;

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

public class QRMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityQrmapBinding binding;
    private Geolocation player_location;

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
            player_location = new Geolocation(locationGPS.getLatitude(), locationGPS.getLongitude());
            populateMap();
        }
        else MainActivity.toast(getApplicationContext(), "Unable to find location.");
    }


    private void populateMap() {
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(player_location.getLatitude(), player_location.getLongitude())));
        for (Collectable scanned : HomeActivity.collectables.getDatabase().values()) {
            Geolocation current = scanned.getLocation();
            if (abs(player_location.getLongitude() - current.getLongitude()) > 10 && abs(player_location.getLongitude() - current.getLongitude()) > 10) {
                map.addMarker(new MarkerOptions().position(new LatLng(current.getLatitude(), current.getLongitude())).title(scanned.getId()));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQrmapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        storeLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {
            if (permissions[x].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && requestCode == 1) {
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) storeLocation();
                else {
                    MainActivity.toast(getApplicationContext(), "Location permission is required to use QR Map");
                    finish();
                }
            }
        }
    }
}