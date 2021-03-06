package com.example.nicolas.shoptic.core;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Category class : The category used to group and manage products more easily
 * (e.g. "Bricolage", "Alimentaire", ...)
 *
 * Created by guilhem on 27/10/16.
 */
public class Category implements Serializable, Comparable<Category>{
    private String name;
    private boolean userDefined;

    /**
     * @param name: The name/label of the category
     * @param userDefined: If the Category is created by the user, set to True (i.e. it is deletable)
     */
    public Category(String name, boolean userDefined) {
        this.name = name;
        this.userDefined = userDefined;
    }

    /**
     * name attribute getter
     * @return The name of the category
     */
    public String getName() {
        return name;
    }


    /**
     * name attribute setter
     * @param name: The new name wanted
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * userDefined attribute getter
     * @return True if the category has been made by the user (i.e. and so it is deletable)
     */
    public boolean isUserDefined(){
        return userDefined;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (this == o){
            return true;
        }
        if (!(o instanceof Category)){
            return false;
        }
        return ((Category) o).getName().equals(this.name);
    }

    @Override
    public int compareTo(Category another) {
        if (this.equals(another)){
            return 0;
        }
        return this.name.compareTo(((Category) another).getName());
    }
}
