package com.example.nicolas.shoptic.core;

import java.util.Calendar;

/**
 * List Class : Class used to represent a To-buy List
 *
 * Created by guilhem on 27/10/16.
 */
public class List {
    private String name;
    private Calendar reminderDate;
    private Calendar frequency;

    /**
     * constructor
     * @param name: name of the list
     * @param reminderDate: Date of the reminder
     * @param frequency: Frequency (just once, weekly, ...)
     */
    public List(String name, Calendar reminderDate, Calendar frequency) {
        this.name = name;
        this.reminderDate = reminderDate;
        this.frequency = frequency;
    }


}
