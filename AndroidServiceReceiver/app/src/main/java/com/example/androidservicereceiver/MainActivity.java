package com.example.androidservicereceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = "Start Service";
    Button btnStartService, btnStopService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START add code here\ 
        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
        // [END add code here\
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");

        // [START start_service]

        //ContextCompat.startService();   // ??? NOT WORK <- CANNOT RUN EVEN ???
        //ContextCompat.startForegroundService(this, serviceIntent);

        //getActivity().startService();   // ??? NOT WORK <- CANNOT RUN EVEN ??? ???
        // startService(serviceIntent);   // simplified version (implicit context of this)
        MainActivity.this.startService(serviceIntent);
        // [END start_service]

        // [START bind_service]
        //bindService(serviceIntent, );   // ??? NOT WORK <- CANNOT RUN EVEN ??? ???
        // [END bind_service]
    }

    private void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);

        // [START stop_service]
        //stopService(serviceIntent);   // simplified version (implicit context of this)
        this.stopService(serviceIntent);
        // [END stop_service]

        // [START stop_self]
        //stopSelf();   // ??? NOT WORK <- CANNOT RUN EVEN ??? ???
        // [END stop_self]
    }

    // [START test_when_destroy]
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy method");   // It can happen when we close the app (in emulator [NOT in editor])
    }
    // [END test_when_destroy]
}