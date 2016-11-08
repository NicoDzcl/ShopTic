package com.example.nicolas.shoptic;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.example.nicolas.shoptic.core.Category;
import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.ListItem;
import com.example.nicolas.shoptic.core.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by guilhem on 30/10/16.
 */
public class ShopTicApplication extends Application {

    private static final String LISTS_SAVE_FILE = "lists_file";
    private static final String PRODUCT_SAVE_FILE = "product_file";
    private static final String LISTITEM_SAVE_FILE = "listitem_file";
    public static final String INTENT_MESSAGE_LIST = "com.example.nicolas.shoptic.LIST";

    private ArrayList<List> lists = null;
    private ArrayList<Product> products = null;
    private ArrayList<ListItem> listItems = null;

    public ArrayList<List> getLists() {
        if (lists == null){
            try {
                FileInputStream fis = getApplicationContext().openFileInput(LISTS_SAVE_FILE);
                ObjectInputStream is = new ObjectInputStream(fis);
                lists = (ArrayList<List>) is.readObject();
                is.close();
                fis.close();
            } catch (IOException|ClassNotFoundException e) {
                lists = new ArrayList<>();
            }
        }
        return lists;
    }

    public void addList(List list){
        lists.add(list);
        saveLists();
    }

    public void deleteList(int position){
        lists.remove(position);
        saveLists();
    }

    public void saveLists(){
        FileOutputStream fos = null;
        try {
            fos = getApplicationContext().openFileOutput(LISTS_SAVE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream os = null;
            os = new ObjectOutputStream(fos);
            os.writeObject(lists);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Product> getProducts() {
        if (products == null){
            try {
                FileInputStream fis = getApplicationContext().openFileInput(PRODUCT_SAVE_FILE);
                ObjectInputStream is = new ObjectInputStream(fis);
                products = (ArrayList<Product>) is.readObject();
                is.close();
                fis.close();
            } catch (IOException|ClassNotFoundException e) {
                products = new ArrayList<>();
                products.add(new Product("Orange", 0., new Category("Alimentaire", false), false));
                products.add(new Product("Citron", 0., new Category("Alimentaire", false), false));
                products.add(new Product("Bière", 0., new Category("Alcool", false), false));
                products.add(new Product("Vin Rouge", 0., new Category("Alcool", false), false));
            }
        }
        return products;
    }

    public ArrayList<ListItem> getItemsInList(List l){
        if (listItems == null){
            try {
                FileInputStream fis = getApplicationContext().openFileInput(LISTITEM_SAVE_FILE);
                ObjectInputStream is = new ObjectInputStream(fis);
                listItems = (ArrayList<ListItem>) is.readObject();
                is.close();
                fis.close();
            } catch (IOException|ClassNotFoundException e) {
                listItems = new ArrayList<>();
            }
        }
        ArrayList<ListItem> toReturn = new ArrayList<>();
        for (ListItem item: listItems){
            if (item.getList() == l){
                toReturn.add(item);
            }
        }
        return toReturn;
    }

    public void addProductToList(Product p, List l){

    }
    public int getPixelsFromDPs(int dps){
        Resources r = this.getResources();
        int px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }
}
