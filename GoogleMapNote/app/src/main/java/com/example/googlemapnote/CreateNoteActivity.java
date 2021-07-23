package com.example.googlemapnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateNoteActivity extends AppCompatActivity {

    private double latitudeDouble;
    private double longitudeDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        /** [START get_data_from_MapsActivity_intent] */
        Intent getDataFromMapsActivityIntent = getIntent();

        // get data of UNIQUE_LAT_LNG and GOOGLE_MAP
        String uniqueIdCombineLatLng = getDataFromMapsActivityIntent.getStringExtra(MapsActivity.UNIQUE_ID_COMBINE_LAT_LNG);
        String googleMap = getDataFromMapsActivityIntent.getStringExtra(MapsActivity.GOOGLE_MAP);

        // separate latitudeDouble and longitudeDouble from UNIQUE_LAT_LNG
        String[] latLngArr = uniqueIdCombineLatLng.split(";");
        String latitudeStr = latLngArr[0];
        String longitudeStr = latLngArr[1];
        latitudeDouble = Double.parseDouble(latitudeStr);
        longitudeDouble = Double.parseDouble(longitudeStr);

        //
        //GoogleMap mMap = (GoogleMap) googleMap;

        // textViewLatLng
        TextView textViewLatLng = (TextView) findViewById(R.id.textViewLatLng);
        textViewLatLng.setText("latitude: " +latitudeDouble+ "; longitude: " +longitudeDouble);
        /** [END get_data_from_MapsActivity_intent] */
    }

    /*public void saveDone(View view) {
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Create a LatLng object
        LatLng latLng = new LatLng(latitudeDouble, longitudeDouble);
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
    }*/
}