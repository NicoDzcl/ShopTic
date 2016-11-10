package com.example.nicolas.shoptic;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by guilhem on 30/10/16.
 */
public class ShopTicApplication extends Application {

    private static final String LISTS_SAVE_FILE = "lists_file";
    private static final String PRODUCT_SAVE_FILE = "product_file";
    private static final String LISTITEM_SAVE_FILE = "listitem_file";
    public static final String INTENT_MESSAGE_LIST = "com.example.nicolas.shoptic.LIST";

    private ArrayList<List> lists = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<ListItem> listItems = new ArrayList<>();

    public ArrayList<List> getLists() {
        if (lists.size() == 0){
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

    public boolean addList(List list){
        for (List l: getLists()){
            if (l.equals(list)){
                return false;
            }
        }
        lists.add(list);
        saveLists();
        return true;
    }

    public void deleteList(int position){
        ArrayList<Integer> positionToRemove = new ArrayList<>();
        for (int i = 0; i < listItems.size(); i++){
            if (listItems.get(i).getList().getName().equals(lists.get(position).getName())){
                positionToRemove.add(i);
            }
        }
        for (Integer i: positionToRemove){
            listItems.remove((int)i);
        }
        lists.remove(position);
        saveListItems();
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
        if (products.size() == 0){
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
                products.add(new Product("Stylo", 0., new Category("Fourniture", false), false));
                products.add(new Product("Gomme", 0., new Category("Fourniture", false), false));
                products.add(new Product("Feutre", 0., new Category("Fourniture", false), false));
                products.add(new Product("Assiette", 0., new Category("Vaisselle", false), false));
                products.add(new Product("Frites", 0., new Category("Surgelé", false), false));
            }
        }
        Collections.sort(products);
        return products;
    }

    public ArrayList<ListItem> getListItems() {
        if (listItems.size() == 0){
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
        return listItems;

    }

    public ArrayList<ListItem> getItemsInList(List l){

        ArrayList<ListItem> toReturn = new ArrayList<>();
        for (ListItem item: getListItems()){
            if (Objects.equals(item.getList().getName(), l.getName())){
                toReturn.add(item);
            }
        }
        return toReturn;
    }

    public ArrayList<Product> getProductsInList(List l){
        ArrayList<Product> toReturn = new ArrayList<>();
        for (ListItem i: getItemsInList(l)){
            toReturn.add(i.getProduct());
        }
        Collections.sort(toReturn);
        return toReturn;
    }

    public boolean isProductInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                return true;
            }
        }
        return false;
    }

    public void addProductToList(Product p, List l){
        listItems.add(new ListItem(1, ListItem.ItemUnit.PIECE, p, l));
        saveListItems();
    }

    public void removeProductFromList(Product p, List l){
        int positionToRemove = -1;
        for (int i = 0; i < getListItems().size(); i++){
            ListItem li = getListItems().get(i);
            if (p.equals(li.getProduct()) && l.equals(li.getList())){
                positionToRemove = i;
                break;
            }
        }
        if (positionToRemove != -1) {
            listItems = getListItems();
            listItems.remove(positionToRemove);
            saveLists();
        }
    }

    private void saveListItems(){
        FileOutputStream fos = null;
        try {
            fos = getApplicationContext().openFileOutput(LISTITEM_SAVE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream os = null;
            os = new ObjectOutputStream(fos);
            os.writeObject(listItems);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPixelsFromDPs(int dps){
        Resources r = this.getResources();
        int px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }

    public TreeMap<Category, Integer> getCategoriesFromItems(ArrayList<Product> products){
        TreeMap<Category, Integer> ret = new TreeMap<>();

        for (Product p: products){
            if (ret.containsKey(p.getCategory())){
                ret.put(p.getCategory(), ret.get(p.getCategory()) + 1);
            }else{
                ret.put(p.getCategory(), 1);
            }
        }
        return ret;
    }

    public void toggleItemInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                item.toggleChecked();
                saveListItems();
                break;
            }
        }
    }

    public boolean isItemChecked(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                return item.isChecked();
            }
        }
        return false;
    }


}
