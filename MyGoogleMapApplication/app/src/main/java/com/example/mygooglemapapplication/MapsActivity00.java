package com.example.mygooglemapapplication;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity00 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified (/notification) when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Th register for the map callback
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
    // Update the map configuration at runtime.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set up a map after adding it dynamically
        /*GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        SupportMapFragment.newInstance(GoogleMapOptions options);*/



        mMap = googleMap;

        // Add a marker in Sydney and move the camera.
        LatLng sydney = new LatLng(-33.852, 151.211);
        LatLng sydney2 = new LatLng(-35, 150);
        LatLng kyoto = new LatLng(35.0016, 135.7681);

        // Set the map type to Hybrid.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker on the map coordinates.                         <= Center the camera on Kyoto Japan.
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Sydney2"));
        mMap.addMarker(new MarkerOptions()
            .position(kyoto)
            .title("Kyoto"));

        // Move the camera to the mao coordinates and zoom in closer.   <= Enable the hybrid map type.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kyoto));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.                                             <= Turn on the traffic layer
        mMap.setTrafficEnabled(true);

        // Sets the map type to be "hybrid"
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        // LatLng sydney = new LatLng(-33.852, 151.211);
        /*googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMarkerClickListener(this);
    }


    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount += 1;
            marker.setTag(clickCount);
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behaciour to occur (which is for the camera to move such that the
        // marker is centered and fof the marker's info window to open, if it has one).
        return false;
    }
}