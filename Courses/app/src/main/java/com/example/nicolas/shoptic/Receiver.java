package com.example.nicolas.shoptic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.example.nicolas.shoptic.core.List;

/**
 * Created by nicolas on 10/11/16.
 */
public class Receiver extends BroadcastReceiver {
    ShopTicApplication application;
    List list = null;



    @Override
    public void onReceive(Context context, Intent intent) {
        list = (List) intent.getSerializableExtra(ShopTicApplication.INTENT_MESSAGE_LIST);

        long time = 2000;
        Vibrator vibrator =(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
        application = (ShopTicApplication)  context.getApplicationContext();
        application.createNotification("La liste " + list.getName() + " vous attend !");
    }
}