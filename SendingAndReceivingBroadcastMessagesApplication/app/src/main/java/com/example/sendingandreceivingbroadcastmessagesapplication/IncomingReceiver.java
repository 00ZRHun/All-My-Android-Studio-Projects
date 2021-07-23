package com.example.sendingandreceivingbroadcastmessagesapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// IncomingReceiver class
//public class MainActivity extends AppCompatActivity {
public class IncomingReceiver extends BroadcastReceiver {
    //public static final String CUSTOM_INTENT = “com.mobisys.android.intent.action.TEST”;
    public static final String CUSTOM_INTENT = "com.example.sendingandreceivingbroadcasemessage.application.MainActivity.CUSTOM_INTENT";

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        *//*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("abc");*//*
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("*****HIT OUTGOING****************");
        Intent i = new Intent();
        i.setAction(CUSTOM_INTENT);
        context.sendBroadcast(i);
    }


}