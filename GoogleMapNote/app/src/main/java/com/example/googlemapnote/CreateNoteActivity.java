package com.example.googlemapnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Intent intent = getIntent();
        String latLngStr = intent.getStringExtra(MapsActivity.UNIQUE_LAT_LNG);
        String[] latLngArr = latLngStr.split(";");
        String latitude = latLngArr[0];
        String longitude = latLngArr[1];

        // textViewLatLng
        TextView textViewLatLng = (TextView) findViewById(R.id.textViewLatLng);
        textViewLatLng.setText("latitude: " +latitude+ "; longitude: " +longitude);
    }
}