package com.example.nicolas.shoptic;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.NotificationCompat;
import android.util.TypedValue;

import com.example.nicolas.shoptic.core.Category;
import com.example.nicolas.shoptic.core.Frequency;
import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.ListItem;
import com.example.nicolas.shoptic.core.Product;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeMap;

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

    public String getQuantityProductInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                return item.getQuantity() + " " + item.getQuantityUnits();
            }
        }

        return "";
    }

    public int getIntQuantityProductInList(Product p, List l) {
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)) {
                return item.getQuantity();
            }
        }
        return 1;
    }

    public int getPositionUnitsProductInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)) {
                return getAllQuantityUnits().indexOf(item.getQuantityUnits());
            }
        }
        return 0;
    }

    public void setQuantityProductInList(Product p, List l, int quantity, String unit){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                item.setQuantity(quantity);
                item.setUnit(getUnitFromString(unit));
                saveListItems();
                break;
            }
        }
    }

    private ListItem.ItemUnit getUnitFromString(String s){
        return ListItem.ItemUnit.values()[getAllQuantityUnits().indexOf(s.toLowerCase())];
    }

    public ArrayList<String> getAllQuantityUnits(){
        ArrayList<String> toReturn = new ArrayList<>();
        for (ListItem.ItemUnit u: ListItem.ItemUnit.values()){
            toReturn.add(u.toString().toLowerCase());
        }
        return toReturn;
    }

    public void createNotification(){
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_share)
                        .setContentTitle("ShopTic")
                        .setContentText("Vous devez faire vos courses!");

        Intent resultIntent = new Intent(this, MainActivity.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 1;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public String getTextFromFrequency(Frequency fr){
        switch (fr){
            case ONCE:
                return "Une seule fois";
            case HEBDO:
                return "Toutes les semaines";
            case MONTHLY:
                return "Tous les mois";
            case ANNUALLY:
                return "Tous les ans";
            default:
                return "";
        }
    }

    public Frequency getFrequencyFromText(String str){
        for (Frequency f: Frequency.values()) {
            getTextFromFrequency(f);
            if(str.equals(getTextFromFrequency(f))){
                return f;
            }
        }
        return Frequency.ONCE;
    }

    public int getTimeInMilliForRepeatition(Frequency fr){
        switch (fr){
            case ONCE:
                return 0;
            case HEBDO:
                return 1000*60*60*24*7;
            case MONTHLY:
                return 1000*60*60*24*7*30;
            case ANNUALLY:
                return 1000*60*60*24*365;
            default:
                return 0;
        }
    }

}
