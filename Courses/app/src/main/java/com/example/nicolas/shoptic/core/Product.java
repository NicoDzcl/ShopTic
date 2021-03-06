package com.example.nicolas.shoptic.core;

import java.io.Serializable;
import java.util.Collections;

/**
 * Product class : used to represent a product (e.g. orange, banane, casserole, tournevis,...)
 *
 * Created by guilhem on 27/10/16.
 */
public class Product implements Serializable, Comparable<Product>{
    private String name;
    private double price;
    private Category category;
    private boolean userDefined;
    private String imageUri = null;

    /**
     * constructor
     * @param name: Name of the product (e.g. "orange", "Stylo", ...)
     * @param price: Price of the product TODO:Set units (euros, ...) maybe in the settings ?
     * @param category: Category of the product ("alimentaire", ...)
     * @param userDefined: True if the product has been created by the user and so is deletable
     */
    public Product(String name, String imageUri, double price, Category category, boolean userDefined) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.userDefined = userDefined;
        this.imageUri = imageUri;
    }

    /**
     * name attribute getter
     * @return the name of the product (e.g. "orange", "Sac poubelle")
     */
    public String getName() {
        return name;
    }

    /**
     * name attribute setter
     * @param name: name of the product (e.g. "orange")
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Price getter
     * @return the defined price of the product (e.g. 2.3) TODO:define the units
     */
    public double getPrice() {
        return price;
    }

    /**
     * Price setter
     * @param price the price of the product (e.g. 3.5) TODO : define the units
     */
    public void setPrice(double price) {
        this.price = price;
    }


    /**
     * Category getter
     * @return the category of the product (e.g. alimentaire)
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Category setter
     * @param category the category of the product (e.g. alimentaire)
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * userDefined getter
     * @return true if the product has been created by the user (and so is deletable)
     */
    public boolean isUserDefined() {
        return userDefined;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (this == o){
            return true;
        }
        if (!(o instanceof Product)){
            return false;
        }
        return ((Product) o).getName().equals(this.name);
    }

    @Override
    public int compareTo(Product another) {
        if (category.compareTo(another.category) != 0){
            return category.compareTo(another.category);
        }
        return name.compareTo(another.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode() + getCategory().hashCode();
    }
}
