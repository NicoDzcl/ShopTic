package com.example.nicolas.shoptic;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NotifierActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button btnDatePicker, btnTimePicker, btnSave;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String array_spinner[];
    private String array_spinner2[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifier_activity);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnSave = (Button)findViewById(R.id.buttonSave);


        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        array_spinner = new String[]{"Une seule fois", "Hebdomadaire", "Mensuelle", "Annuelle"};
        array_spinner2 = new String[]{"Notification", "E-mail"};
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);
        Spinner s2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter adapter2 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner2);
        s2.setAdapter(adapter2);

        btnSave.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {
                affNotif();
            }
        });
    }


    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            btnTimePicker.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private final void createNotification(){
        AlarmManager alarms = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_share)
                        .setContentTitle("ShopTic")
                        .setContentText("Vous devez faire vos courses!");

        Intent resultIntent = new Intent(this, Receiver.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        alarms.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, resultPendingIntent) ;



    }
    
    public void affNotif(){
        int seconds = 2;
// Create an intent that will be wrapped in PendingIntent
        Intent intent = new Intent(this, Receiver.class);

// Create the pending intent and wrap our intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

// Get the alarm manager service and schedule it to go off after 3s
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);

        Toast.makeText(this, "Alarm set in " + seconds + " seconds", Toast.LENGTH_LONG).show();

    }

    public void aff(){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), Receiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);

// Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 3);

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);


    }
}