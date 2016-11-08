package com.example.nicolas.shoptic.core;

/**
 * Association class between a list and a product. Used to represent the many to many association
 * and to add details to this association (number of product added and units)
 *
 * Created by guilhem on 27/10/16.
 */

public class ListItem {
    /**
     * Units available in order to count occurence of a product inside a list
     */
    public enum ItemUnit{
        PIECE,
        DIZAINE,
        LITRE,
        GRAMME,
        KILOGRAMME,
        PACK,
        PAQUET,
        DOUZAINE,
        BOUTEILLE
    }

    private int quantity;
    private ItemUnit unit;
    private Product product;
    private List list;

    public ListItem(int quantity, ItemUnit unit, Product product, List list) {
        this.quantity = quantity;
        this.unit = unit;
        this.product = product;
        this.list = list;
    }

    public List getList() {
        return list;
    }
}
