package com.example.sendingandreceivingbroadcastmessagesapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OutgoingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MainActivity.CUSTOM_INTENT)) {
            System.out.println("*****GOT THE INTENT********");
        }
    }
}
