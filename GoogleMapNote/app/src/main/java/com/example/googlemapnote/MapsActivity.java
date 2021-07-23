package com.example.googlemapnote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, Serializable {

    public static final String UNIQUE_ID_COMBINE_LAT_LNG = "com.example.googlemapnote.mapsactivity.UNIQUE_ID_COMBINE_LAT_LNG";
    public static final String GOOGLE_MAP = "com.example.googlemapnote.mapsactivity.COOGLE_MAP";

    private double selectedLat;
    private double selectedLng;
    private double currentLat;
    private double currentLng;
    private boolean hasUserSelectedMarker = false;
    private String toastText;
    private String latLngValue;

    private int userClickToAddMarkerTimes = 0;
    private Marker markerName;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setTitle("Map Location Activity");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }

        // Setting a click event handler for the map
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Remove old marker
                //while (userClickToAddMarkerTimes >= 1){
                if (hasUserSelectedMarker) {
                    markerName.remove();
                }

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);
                markerOptions.draggable(true);   // ???

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                //mMap.clear();

                // Animating to the touched position
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                markerName = mGoogleMap.addMarker(markerOptions);

                // Update latitude and longitude TEMP value
                selectedLat = latLng.latitude;
                selectedLng = latLng.longitude;

                        /* // Increment the times of user click map to add marker
                        userClickToAddMarkerTimes++;*/
                // set user have selected one marker on Google Map
                hasUserSelectedMarker = true;
            }
        });

    }



    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

                // Display traffic.
                mGoogleMap.setTrafficEnabled(true);

                // Sets the map type to be "hybrid"
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                // Update latitude and longitude TEMP value
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    // [START to_drop_ONLY_ONE_pin]
    /** Called when the taps the + btn */
    public void startCreateNoteActivity(View view) {
        // show alert
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (hasUserSelectedMarker) {
            alertDialogBuilder.setMessage("Which location you prefer to create note?");
            alertDialogBuilder.setPositiveButton("Current Location",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            /*//You clicked yes button
                            Toast.makeText(MapsActivity.this,"Create note at current location!!!",Toast.LENGTH_LONG).show();

                            // start new activity by using intent
                            Intent createNoteIntent = new Intent(MapsActivity.this, CreateNoteActivity.class);
                            //createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, selectedLat+";"+selectedLng);
                            createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, currentLat+";"+currentLng);
                            startActivity(createNoteIntent);*/

                            toastText = "Create note at current location!!!";
                            latLngValue = currentLat+";"+currentLng;

                            // start new activity by using intent
                            Intent createNoteIntent = new Intent(MapsActivity.this, CreateNoteActivity.class);
                            // ??? <<<=== ???
                            //createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, selectedLat+";"+selectedLng);
                            //createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, latLngValue);

                            //createNoteIntent.putExtra(GOOGLE_MAP, (Serializable) mGoogleMap);
                            /*Bundle gm = new Bundle(mGoogleMap);
                            startActivity(createNoteIntent);*/
                        }
                    });

            alertDialogBuilder.setNegativeButton("Selected Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    toastText = "Create note at selected location!!!";
                    latLngValue = selectedLat+";"+selectedLng;

                    // start new activity by using intent
                    Intent createNoteIntent = new Intent(MapsActivity.this, CreateNoteActivity.class);
                    //createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, selectedLat+";"+selectedLng);
                    createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, latLngValue);
                    startActivity(createNoteIntent);
                }
            });

            /*// Both of the if-else case run below code block
            Toast.makeText(MapsActivity.this, toastText, Toast.LENGTH_LONG).show();

            // start new activity by using intent
            Intent createNoteIntent = new Intent(MapsActivity.this, CreateNoteActivity.class);
            //createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, selectedLat+";"+selectedLng);
            createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, latLngValue);
            startActivity(createNoteIntent);*/

        } else {
            // ???
            /*alertDialogBuilder.setMessage("Are you sure,
                    You wanted to make decision");*/
            alertDialogBuilder.setMessage("Do you want to create note at your current location?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //You clicked yes button
                            Toast.makeText(MapsActivity.this,"Click note here!!!",Toast.LENGTH_LONG).show();

                            // start new activity by using intent
                            Intent createNoteIntent = new Intent(MapsActivity.this, CreateNoteActivity.class);
                            createNoteIntent.putExtra(UNIQUE_ID_COMBINE_LAT_LNG, currentLat+";"+currentLng);
                            startActivity(createNoteIntent);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //finish();
                    Toast.makeText(MapsActivity.this, "Select preference location here!!!", Toast.LENGTH_SHORT).show();

                    /**
                     * OLD location for setting a click event
                     */
                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    // [END to_drop_ONLY_ONE_pin]
}
