package com.example.googlemapnote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

public class MapsActivity_CanWork_ExceptGetCurrentLocation extends FragmentActivity implements OnMapReadyCallback {
    public static final String UNIQUE_LAT_LNG = "com.example.googlemapnote.mapsactivity.UNIQUE_LAT_LNG";

    private static final String TAG = MapsActivity_CanWork_ExceptGetCurrentLocation.class.getSimpleName();

    private GoogleMap mMap;
    private double currentSelectedLat = 0;
    private double currentSelectedLng = 0;

    private int userClickToAddMarkerTimes = 0;
    private Marker markerName;

    //private FusedLocationProviderClient fusedLocationProviderClient;
    private FusedLocationProviderClient fusedLocationClient;

    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    private static final int DEFAULT_ZOOM = 15;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);


    /*private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;




    //GoogleMap mMap; ???
    private boolean locationPermissionGranted;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a PlaceClient
        /*// Initialize the SDK
        //Places.initialize(getApplicationContext(), "AIzaSyB0CVThQriqeBGM7FnhDx_Sa0PIN6cy8MY");
        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        MenuItem item = (MenuItem) findViewById(R.id.option_get_place);
        onOptionsItemSelected(item);

        // Get the location of the device and set the position of the map.
        getDeviceLocation();*/

        // Prompt the user for permission.
        getLocationPermission();

        Toast.makeText(MapsActivity_CanWork_ExceptGetCurrentLocation.this,"CHECKING PERMISSON!!!",Toast.LENGTH_LONG).show();
        if (locationPermissionGranted) {
            Toast.makeText(MapsActivity_CanWork_ExceptGetCurrentLocation.this,"GOT!!!",Toast.LENGTH_LONG).show();
            Task<Location> locationResult = fusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } else {
            Toast.makeText(MapsActivity_CanWork_ExceptGetCurrentLocation.this,"NOOOT!!!",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    // [END maps_current_place_location_permission]

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    // [START maps_current_place_get_device_location]
    private void getDeviceLocation() {
        /*fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });*/
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    // [END maps_current_place_get_device_location]



    /** Called when the taps the + btn */
    public void startCreateNoteActivity(View view) {
        // show alert
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        /*alertDialogBuilder.setMessage("Are you sure,
                You wanted to make decision");*/
        //OriText: Are you sure, You wanted to make decision
        alertDialogBuilder.setMessage("Do u want to create note at your current location?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //You clicked yes button
                        Toast.makeText(MapsActivity_CanWork_ExceptGetCurrentLocation.this,"Click note here!!!",Toast.LENGTH_LONG).show();

                        // start new activity by using intent
                        Intent createNoteIntent = new Intent(MapsActivity_CanWork_ExceptGetCurrentLocation.this, CreateNoteActivity.class);
                        createNoteIntent.putExtra(UNIQUE_LAT_LNG, currentSelectedLat+";"+currentSelectedLng);
                        startActivity(createNoteIntent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                Toast.makeText(MapsActivity_CanWork_ExceptGetCurrentLocation.this, "Select preference location here!!!", Toast.LENGTH_SHORT).show();

                // Setting a click event handler for the map
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                            while (userClickToAddMarkerTimes >= 1){
                                markerName.remove();
                                userClickToAddMarkerTimes = 0;
                            }

                            userClickToAddMarkerTimes++;

                            currentSelectedLat = latLng.latitude;
                            currentSelectedLng = latLng.longitude;


                            // Creating a marker
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Setting the position for the marker
                            markerOptions.position(latLng);
                            markerOptions.draggable(true);

                            // Setting the title for the marker.
                            // This will be displayed on taping the marker
                            markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                            // Clears the previously touched position
                            //mMap.clear();

                            // Animating to the touched position
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                            // Placing a marker on the touched position
                            markerName = mMap.addMarker(markerOptions);

                    }
                });
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}