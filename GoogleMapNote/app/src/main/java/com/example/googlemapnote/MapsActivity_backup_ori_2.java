package com.example.googlemapnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class MapsActivity_backup_ori_2 extends FragmentActivity implements OnMapReadyCallback {

    public static final String POSITION_OF_LAT_LNG = "com.example.googlemapnote.POSITION_OF_LAT_LNG";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = MapsActivity_backup_ori_2.class.getSimpleName();
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private GoogleMap mMap;
    //GoogleMap mMap; ???
    private boolean locationPermissionGranted;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;


    //
    private double userPreferenceLat = 0;
    private double userPreferenceLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a PlaceClient
        // Initialize the SDK
        //Places.initialize(getApplicationContext(), "AIzaSyB0CVThQriqeBGM7FnhDx_Sa0PIN6cy8MY");
        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /*//Places.initialize(getApplicationContext(), getString("${MAPS_API_KEY)");
        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        PlacesClient placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Pin the marker at the user current location
        /*LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // I suppressed the missing-permission warning because this wouldn't be executed in my
        // case without location services being enabled
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        userPreferenceLat = lastKnownLocation.getLatitude();
        userPreferenceLng = lastKnownLocation.getLongitude();*/

        /*//LatLng userLastKnownLocation = new LatLng(userPreferenceLat, userPreferenceLong);
        LatLng userLastKnownLocation = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(userLastKnownLocation).title("I'm here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLastKnownLocation));*/

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }





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
                        Toast.makeText(MapsActivity_backup_ori_2.this,"Click note here!!!",Toast.LENGTH_LONG).show();

                        // start new activity by using intent
                        Intent createNoteIntent = new Intent(MapsActivity_backup_ori_2.this, CreateNoteActivity.class);
                        createNoteIntent.putExtra(POSITION_OF_LAT_LNG, userPreferenceLat+";"+userPreferenceLng);
                        startActivity(createNoteIntent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                Toast.makeText(MapsActivity_backup_ori_2.this, "Select preference location here!!!", Toast.LENGTH_SHORT).show();

                // Setting a click event handler for the map
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    /*// === find current place ===
                    // Use fields to define the data types to return.
                    List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

                    // Use the builder to create a FindCurrentPlaceRequest.
                    FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

                    // Call findCurrentPlace and handle the response (first check that the user has granted permission).
                    if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
                        placeResponse.addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                FindCurrentPlaceResponse response = task.getResult();
                                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                    Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                            placeLikelihood.getPlace().getName(),
                                            placeLikelihood.getLikelihood()));
                                }
                            } else {
                                Exception exception = task.getException();
                                if (exception instanceof ApiException) {
                                    ApiException apiException = (ApiException) exception;
                                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                                }
                            }
                        });
                    } else {
                        // A local method to request required permissions;
                        // See https://developer.android.com/training/permissions/requesting
                        getLocationPermission();
                    }*/

                    @Override
                    public void onMapClick(LatLng latLng) {

                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Clears the previously touched position
                        //mMap.clear();

                        // Animating to the touched position
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);
                    }
                });
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }


}