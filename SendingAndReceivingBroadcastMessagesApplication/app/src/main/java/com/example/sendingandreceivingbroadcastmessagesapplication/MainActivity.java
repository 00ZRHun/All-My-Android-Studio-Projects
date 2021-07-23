package com.example.sendingandreceivingbroadcastmessagesapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

// IncomingReceiver class
public class MainActivity extends AppCompatActivity {
    //public class MainActivity extends BroadcastReceiver {
    public static final String CUSTOM_INTENT = "com.example.sendingandreceivingbroadcastmessage.application.MainActivity.CUSTOM_INTENT";
    private BroadcastReceiver _refreshReceiver = new OutgoingReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(CUSTOM_INTENT);
        this.registerReceiver(_refreshReceiver, filter);

        // [START send_broadcast]
        try {
            //Intent in = new Intent("SOMEACTION");
            Intent in = new Intent(CUSTOM_INTENT);
            sendBroadcast(in);
            System.out.println("successful send the broadcast.");
        } catch (Exception e) {
            System.out.println(e);
        }
        // [END send_broadcast]

    }

    /*@Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("*****HIT OUTGOING****************");
        Intent i = new Intent();
        i.setAction(CUSTOM_INTENT);
        context.sendBroadcast(i);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this._refreshReceiver);
    }


}