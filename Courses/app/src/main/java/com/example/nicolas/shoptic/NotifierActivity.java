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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nicolas.shoptic.core.Frequency;
import com.example.nicolas.shoptic.core.List;

import java.util.Calendar;

public class NotifierActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button btnDatePicker, btnTimePicker, btnSave;
    TextView txtLstName, txtIsAlarm;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int smYear, smMonth, smDay, smHour, smMinute;
    private Frequency fr;
    private List list;
    private ShopTicApplication app;
    private Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = (List) getIntent().getSerializableExtra(ShopTicApplication.INTENT_MESSAGE_LIST);
        fr = Frequency.ONCE;
        setContentView(R.layout.notifier_activity);

        app = (ShopTicApplication) getApplicationContext();

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnSave = (Button)findViewById(R.id.buttonSave);
        txtLstName = (TextView)findViewById(R.id.txt_list);
        txtIsAlarm = (TextView)findViewById(R.id.txt_isAlarm);

        txtLstName.setText(list.getName());
        System.out.println(list.getAlarm());
        if (list.getAlarm() != null) {
            if (list.getAlarm()) {
                //txtIsAlarm.setText("Une alarme est programmée pour le " + list.getReminderDate().toString() + "avec pour récurence " + app.getTextFromFrequency(list.getFrequency()));
            }
        }
            else {
            txtIsAlarm.setText("Il n'y a pas d'alarme programmée");
        }


        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        String[] array_spinner = new String[Frequency.values().length];
        int i = 0;
        for (Frequency f: Frequency.values()) {
            array_spinner[i] = app.getTextFromFrequency(f);
            i++;
        }

        s = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array_spinner);
        assert s != null;
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
        String str = s.getSelectedItem().toString();
        fr = app.getFrequencyFromText(str);
        list.setFrequency(fr);


        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Receiver.class);
        intent.putExtra(ShopTicApplication.INTENT_MESSAGE_LIST, list);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getApplicationContext(), list.getIdentifier(), intent, 0);

        // Set the alarm to start at the chosen time.
        Calendar calendar = Calendar.getInstance();
        calendar.set(smYear, smMonth, smDay, smHour, smMinute);
        list.setReminderDate(calendar);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), app.getTimeInMilliForRepeatition(fr), alarmIntent);

        calendar.setTimeInMillis(list.getReminderDate().getTimeInMillis());

        app.changeAlertState(list, true);
        txtIsAlarm.setText("Une alarme est programmée pour le " + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR) + " à " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " avec pour récurence " + app.getTextFromFrequency(list.getFrequency()));

        Toast.makeText(this, "Alarm programmée pour le" + smDay + "-" + (smMonth + 1) + "-" + smYear + " à " + smHour + ":" + smMinute + " et sera répétée " + app.getTextFromFrequency(fr), Toast.LENGTH_SHORT).show();
    }
}