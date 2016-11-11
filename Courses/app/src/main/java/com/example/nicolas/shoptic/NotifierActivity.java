package com.example.nicolas.shoptic;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NotifierActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button btnDatePicker, btnTimePicker, btnSave;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int smYear, smMonth, smDay, smHour, smMinute;
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
        //ArrayAdapter adapter2 = new ArrayAdapter(this,
        //        android.R.layout.simple_spinner_item, array_spinner2);
        //s2.setAdapter(adapter2);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                createAlarm();
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
                            smYear = year;
                            smMonth = monthOfYear;
                            smDay = dayOfMonth;

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
                            smHour = hourOfDay;
                            smMinute = minute;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private final void createAlarm(){

        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Receiver.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);

        // Set the alarm to start at the chosen time.
        Calendar calendar = Calendar.getInstance();
        calendar.set(smYear, smMonth, smDay, smHour, smMinute);

        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, alarmIntent);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        Toast.makeText(this, "Alarm programmée pour le" + smDay + "-" + (smMonth + 1) + "-" + smYear + " à " + smHour + ":" + smMinute, Toast.LENGTH_SHORT).show();
    }
}