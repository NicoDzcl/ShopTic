package com.example.nicolas.shoptic.core;

import java.io.Serializable;
import android.net.Uri;
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
    private Calendar frequency;

    /**
     * constructor
     * @param name: name of the list
     * @param reminderDate: Date of the reminder
     * @param frequency: Frequency (just once, weekly, ...)
     */
    public List(String name, String image_path, Calendar reminderDate, Calendar frequency) {
        this.name = name;
        this.image_path = image_path;
        this.reminderDate = reminderDate;
        this.frequency = frequency;
    }

    public List(String name, String image_path) {
        this.name = name;
        this.image_path = image_path;
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
}
