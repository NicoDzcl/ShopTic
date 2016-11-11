package com.example.nicolas.shoptic.core;

import java.io.Serializable;
import java.util.Calendar;

/**
 * List Class : Class used to represent a To-buy List
 *
 * Created by guilhem on 27/10/16.
 */
public class List implements Serializable{
    private String name;
    private String image_path;
    private Calendar reminderDate;
    private Frequency frequency;
    private int identifier;
    private String isAlarm = "false";

    /**
     * constructor
     * @param name: name of the list
     * @param reminderDate: Date of the reminder
     * @param frequency: Frequency (just once, weekly, ...)
     */
    public List(String name, String image_path, Calendar reminderDate, Frequency frequency) {
        this.name = name;
        this.image_path = image_path;
        this.reminderDate = reminderDate;
        this.frequency = frequency;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        String identifier = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + Integer.toString(c.get(Calendar.HOUR)) + Integer.toString(c.get(Calendar.MINUTE)) + Integer.toString(c.get(Calendar.SECOND));

        this.identifier = Integer.parseInt(identifier);
    }

    public List(String name, String image_path) {
        this.name = name;
        this.image_path = image_path;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        String identifier = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + Integer.toString(c.get(Calendar.HOUR)) + Integer.toString(c.get(Calendar.MINUTE)) + Integer.toString(c.get(Calendar.SECOND));

        this.identifier = Integer.parseInt(identifier);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (this == o){
            return true;
        }
        if (!(o instanceof List)){
            return false;
        }
        return ((List) o).getName().equals(this.name);
    }

    public Calendar getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Calendar reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public String getAlarm() {
        return isAlarm;
    }

    public void setAlarm(String alarm) {
        isAlarm = alarm;
        System.out.println(alarm);
    }
}
