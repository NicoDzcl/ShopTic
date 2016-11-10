package com.example.nicolas.shoptic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

/**
 * Created by nicolas on 10/11/16.
 */
public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Vibrate for 2 seconds
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
        System.out.println("hd");
    }
}