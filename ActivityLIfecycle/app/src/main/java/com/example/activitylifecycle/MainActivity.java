package com.example.activitylifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends TracerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonGoto2ndActivity = (Button) findViewById(R.id.buttonGoto2ndActivity);
        buttonGoto2ndActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto2ndActivity();
            }
        });

    }

    //public void onClick(View view) {      // original default version
    private void goto2ndActivity() {   // modified version called by onClick(View v)
        //Intent intent = new Intent(this, SecondActivity.class);
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }
}