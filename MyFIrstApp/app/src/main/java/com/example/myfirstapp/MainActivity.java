package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;   // import class <= Cannot resolve symbol 'View'
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    // AppCompatActivity == Activity (in older version of sdk level)

    public static final String EXTRA_MESSAGE = "com.example.MyFirstApp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // savedInstanceState -> save info and load for later use
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // Inflate (refer to 2 July video)
        // getMenuInflater().inflate(menu.main, menu);
        // return true;
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view){
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);   // intent: starts another activity
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName2);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);   // constant EXTRA_MESSAGE
        startActivity(intent);
    }

}