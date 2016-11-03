package com.example.nicolas.shoptic;

import android.app.Application;
import android.content.Context;

import com.example.nicolas.shoptic.core.List;
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

    private ArrayList<List> lists = null;
    private ArrayList<Product> products = null;

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
        return products;
    }
}
